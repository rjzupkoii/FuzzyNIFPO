package edu.mtu.fuzzynipfo;

import ec.util.MersenneTwisterFast;
import edu.mtu.environment.GrowthModel;
import edu.mtu.policy.PolicyBase;
import edu.mtu.simulation.ForestSim;
import edu.mtu.simulation.Scorecard;
import edu.mtu.steppables.LandUseGeomWrapper;
import edu.mtu.steppables.ParcelAgent;

@SuppressWarnings("serial")
public class Model extends ForestSim {

	public Model(long seed) {
		super(seed);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ParcelAgent createEconomicAgent(MersenneTwisterFast arg0, LandUseGeomWrapper arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParcelAgent createEcosystemsAgent(MersenneTwisterFast arg0, LandUseGeomWrapper arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultCoverFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultOutputDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultParcelFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GrowthModel getGrowthModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHarvestCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getModelParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PolicyBase getPolicy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Scorecard getScoreCard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean useAggregateHarvester() {
		// TODO Auto-generated method stub
		return false;
	}
}