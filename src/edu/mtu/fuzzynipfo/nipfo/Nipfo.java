package edu.mtu.fuzzynipfo.nipfo;

import java.util.ArrayList;
import java.util.List;

import edu.mtu.environment.Forest;
import edu.mtu.environment.Stand;
import edu.mtu.steppables.LandUseGeomWrapper;
import edu.mtu.steppables.ParcelAgent;
import edu.mtu.steppables.ParcelAgentType;
import edu.mtu.steppables.marketplace.AggregateHarvester;
import edu.mtu.wup.model.Harvesting;

@SuppressWarnings("serial")
public class Nipfo extends ParcelAgent  {

	private final static double harvestDbh = Harvesting.SawtimberDbh;
	
	private boolean harvested = false;
	private double score = -1.0;
	private List<NipfoAttitude> attitudes;
			
	public Nipfo(ParcelAgentType type, LandUseGeomWrapper lu) {
		super(type, lu);
		attitudes = new ArrayList<NipfoAttitude>();
	}

	@Override
	public void doHarvestedOperation() {
		harvested = true;
	}

	@Override
	protected void doPolicyOperation() {
		// TODO Auto-generated method stub	
	}

	@Override
	protected void doHarvestOperation() {
		// Get the result of the FIS
		if (score == -1) {
			NipfoFis fis = NipfoFis.getInstance();
			score = fis.evaluate(attitudes);
		}
		
		// Return if we should not harvest
		if (NipfoFis.isNo(score)) {
			return;
		}
		
		// Stochastically harvest on a maybe
		if (NipfoFis.isMaybe(score)) {
			if (state.random.nextDouble(true, true) <= score) {
				return;
			}
		}

		// Request the harvest, but only if it meets the minimum for the harvester
		List<Stand> stands = Harvesting.getHarvestableStands(getParcel(), harvestDbh);
		double area = stands.size() * Forest.getInstance().getAcresPerPixel();
		if (area < AggregateHarvester.MinimumHarvestArea) {
			return;
		}
		AggregateHarvester.getInstance().requestHarvest(this, stands);	
	}
		
	public void addAttitude(String label, int value) {
		attitudes.add(new NipfoAttitude(label, value));
	}
	
	public void addAttitude(String label, double value) {
		attitudes.add(new NipfoAttitude(label, value));
	}

	public boolean didHarvest() {
		 return harvested;
	}
	
	public void setAttitudes(List<NipfoAttitude> value) {
		attitudes = value;
	}
}
