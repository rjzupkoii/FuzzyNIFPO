package edu.mtu.fuzzynipfo;

import edu.mtu.simulation.parameters.ParameterBase;

public class ModelParameters extends ParameterBase {
	// Path to default GIS files used in the simulation
	public static final String defaultCoverFile = "shapefiles/WUP Land Cover/WUPLandCover.asc";
	public static final String defaultParcelFile = "file:shapefiles/WUP Parcels/WUPParcels.shp";
	
	// Path to the cleaned NWOS data
	public static final String nwos = "nwos/clean";
	
	// Path to output directory
	public static final String outputDirectory = "out/fis";
	
	// About 10% of NIPFOs
	public static final int loggingCapacity = 2500;
	
	// The number of NIPFOs that should be phased in per time-step, prevents unrealistic saw-toothing
	public final static double LandTenurePhaseInRate = 0.02;
	
	public ModelParameters() {
		setFinalTimeStep(200);
	}
}
