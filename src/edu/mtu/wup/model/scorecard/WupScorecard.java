package edu.mtu.wup.model.scorecard;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import edu.mtu.fuzzynipfo.nipfo.Nipfo;
import edu.mtu.measures.ForestMeasuresParallel;
import edu.mtu.simulation.ForestSim;
import edu.mtu.simulation.Scorecard;
import edu.mtu.steppables.ParcelAgent;
import edu.mtu.steppables.marketplace.AggregateHarvester;
import edu.mtu.utilities.BufferedCsvWriter;
import edu.mtu.utilities.Constants;
import sim.field.geo.GeomVectorField;
import sim.io.geo.ShapeFileExporter;

public class WupScorecard implements Scorecard {

	// TODO Migrate to command line configuration
	private final static boolean writeGis = false;
	
	private final static int captureInterval = 20;
	private final static String nipfoFile = "/nipfo%1$d";			// ArcGIS disapproves of .shp.shp
	
	private BufferedCsvWriter[] writers;
	private String outputDirectory;
	private String filesDirectory;
	
	public WupScorecard(String directory) {
		outputDirectory = directory;
		Date dt = new Date();
		filesDirectory = outputDirectory + "/" + dt.getTime();
	}
	
	public void processTimeStep(ForestSim state) {
		try {
			writeCarbonSequestration(state.getParcelAgents());
			writeHarvesting();
			
			// Check the step and flush and export GIS as needed
			if (state.schedule.getSteps() % captureInterval == 0) {
				for (int ndx = 0; ndx < writers.length; ndx++) {
					writers[ndx].flush();
				}
				writeGisFiles(state);
			}
		} catch (IOException ex) {
			System.err.println("Unhandled IOException: " + ex.toString());
			System.exit(-1);
		} catch (InterruptedException ex) {
			System.err.println("Unhandled InterruptedException: " + ex.toString());
			System.exit(-1);
		}
	}

	public void processInitialization(ForestSim state) {
		try {
			// Bootstrap any relevant paths
			File directory = new File(filesDirectory);
			directory.mkdirs();

			// Create the buffered file writers
			writers = new BufferedCsvWriter[Indicators.IndicatorCount];
			for (Indicators indicator : Indicators.values()) {
				writers[indicator.getValue()] = new BufferedCsvWriter(outputDirectory + indicator.getFileName(), true);
			}			
		} catch (IOException ex) {
			System.err.println("Unhandled IOException: " + ex.toString());
			System.exit(-1);
		}	
	}
		
	public void processFinalization(ForestSim state) {
		try {
			for (int ndx = 0; ndx < writers.length; ndx++) {
				writers[ndx].close();
			}
			writeGisFiles(state);
			writeHarvested(state);
		} catch (IOException ex) {
			System.err.println("Unhandled IOException: " + ex.toString());
			System.exit(-1);
		}
	}
		
	/**
	 * Find the approximate amount of carbon in the woody biomass.
	 * 
	 * @param biomass The woody biomass in kg (dry tons)
	 * @return The carbon content of the vegetation in gigatons (Gt)
	 */
	private double carbonInBiomassEstiamte(double biomass) {
		// Use the approximation given by (Magnussen & Reed, 2015) 
		return 0.475 * biomass; 
	}
	
	
	// Society: Aesthetics, Environment: Habitat Connectivity
	private void writeGisFiles(ForestSim state) throws IOException {
		if (!writeGis) {
			return;
		}
		
		// Have the agents update their parcels
		for (ParcelAgent agent : state.getParcelAgents()) {
			agent.updateShapefile();
			state.updateAgentGeography(agent);
		}
		
		// Store the parcels to disk
		String fileName = String.format(filesDirectory + nipfoFile, state.schedule.getSteps());
		GeomVectorField parcels = state.getParcelLayer();
		ShapeFileExporter.write(fileName, parcels);
	}
	
	// Total agents who harvested, total size of harvested parcels
	private void writeHarvested(ForestSim state) throws IOException {
		int agents = 0;
		double area = 0;
		
		// Note the data
		for (ParcelAgent pa : state.getParcelAgents()) {
			Nipfo agent = (Nipfo)pa;
			if (agent.didHarvest()) {
				agents++;
				area += agent.getParcelArea();
			}
		}
		
		// Write the data to disk
		FileWriter writer = new FileWriter(outputDirectory + "/harvested.csv", true);
		writer.write(state.getParcelAgents().size() + ",");
		writer.write(agents + ",");
		writer.write(area + System.lineSeparator());
		writer.close();
	}

	// Environment: Carbon Sequestration
	private void writeCarbonSequestration(List<ParcelAgent> agents) throws IOException, InterruptedException {		
		double biomass = ForestMeasuresParallel.calculateBiomass();
		double carbon = carbonInBiomassEstiamte(biomass);
		writers[Indicators.CarbonGlobal.getValue()].write(carbon);
		
		biomass = ForestMeasuresParallel.calculateBiomass(agents);
		carbon = carbonInBiomassEstiamte(biomass);
		writers[Indicators.CarbonAgents.getValue()].write(carbon);
	}
	
	// Economic: Woody Biomass Availability, Reliability / consistent supply of woody biomass
	private void writeHarvesting() throws IOException {
		AggregateHarvester harvester = AggregateHarvester.getInstance();
		
		double biomass = harvester.getTotalBiomass() / Constants.KilogramToMetricTon;		
		writers[Indicators.HarvestedBiomass.getValue()].write(biomass);
		
		biomass = harvester.getStemBiomass() / Constants.KilogramToMetricTon;
		writers[Indicators.HarvestedStems.getValue()].write(biomass);
		        
		writers[Indicators.HarvestDemand.getValue()].write(harvester.getHarvestedRequested());
		writers[Indicators.HarvestedParcels.getValue()].write(harvester.getPracelsHarvested());
	}
}
