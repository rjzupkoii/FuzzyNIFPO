package edu.mtu.fuzzynipfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ec.util.MersenneTwisterFast;
import edu.mtu.environment.GrowthModel;
import edu.mtu.fuzzynipfo.nipfo.Nipfo;
import edu.mtu.fuzzynipfo.nipfo.NipfoFis;
import edu.mtu.fuzzynipfo.nipfo.ParcelSize;
import edu.mtu.policy.PolicyBase;
import edu.mtu.simulation.ForestSim;
import edu.mtu.simulation.Scorecard;
import edu.mtu.steppables.LandUseGeomWrapper;
import edu.mtu.steppables.ParcelAgent;
import edu.mtu.steppables.ParcelAgentType;
import edu.mtu.wup.model.WesternUpEvenAgedWholeStand;
import edu.mtu.wup.model.scorecard.WupScorecard;

@SuppressWarnings("serial")
public class Model extends ForestSim {
	
	private WupScorecard scorecard = null;

	public Model(long seed) {
		super(seed);
	}

	@Override
	public ParcelAgent createEconomicAgent(MersenneTwisterFast random, LandUseGeomWrapper lu) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ParcelAgent createEcosystemsAgent(MersenneTwisterFast random, LandUseGeomWrapper lu) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDefaultCoverFile() {
		return ModelParameters.defaultCoverFile;
	}

	@Override
	public String getDefaultOutputDirectory() {
		return ModelParameters.outputDirectory;
	}

	@Override
	public String getDefaultParcelFile() {
		return ModelParameters.defaultParcelFile;
	}

	@Override
	public GrowthModel getGrowthModel() {
		return new WesternUpEvenAgedWholeStand(getRandom());
	}

	@Override
	public int getHarvestCapacity() {
		return ModelParameters.loggingCapacity;
	}

	@Override
	public Object getModelParameters() {
		return new ModelParameters();
	}

	@Override
	public PolicyBase getPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Scorecard getScoreCard() {
		if (scorecard == null) {
			scorecard = new WupScorecard(getOutputDirectory());
		}
		return scorecard;
	}

	@Override
	public void initialize() {
		try {
			NipfoFis.getInstance().loadAttitudeData(ModelParameters.nwos);
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + ex.getMessage());
			System.exit(-1);
		} catch (IOException ex) {
			System.err.println("Error reading from file: " + ex.getMessage());
			System.exit(-1);
		}
	}

	@Override
	public boolean useAggregateHarvester() {
		return true;
	}
	
	@Override
	protected ParcelAgent createAgent(LandUseGeomWrapper lu, double probablity) {
		// Create the agent
		Nipfo agent = new Nipfo(ParcelAgentType.OTHER, lu);
		agent.setPhaseInRate(ModelParameters.LandTenurePhaseInRate);
		
		// Assign the agent's parcel
		agent = (Nipfo)createAgentParcel(agent);

		// Assign the parcel area attribute
		double area = agent.getParcelArea();
		agent.addAttitude("parcel", area);
		ParcelSize size = ParcelSize.getSize(area);
		
		// Assign the attributes to the agent based upon the parcel assigned
		NipfoFis fis = NipfoFis.getInstance();	
		for (String attitude : fis.getAttitudeList()) {
			// Get the map and the sorted list of keys
			HashMap<Integer, Double> map = fis.getAttitudeMap(attitude, size);
			List<Integer> keys = new ArrayList<Integer>(map.keySet());
			Collections.sort(keys);

			// Find the bin for the random value and assign it
			double rng = random.nextDouble(true, true);
			for (int key : keys) {
				if (rng <= map.get(key)) {
					agent.addAttitude(attitude, key);
					break;
				}
			}
		}
		
		return agent;
	}
}
