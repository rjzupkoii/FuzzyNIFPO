package edu.mtu.fuzzynipfo.nipfo;

import java.util.ArrayList;
import java.util.List;

import edu.mtu.environment.Stand;
import edu.mtu.steppables.LandUseGeomWrapper;
import edu.mtu.steppables.ParcelAgent;
import edu.mtu.steppables.ParcelAgentType;
import edu.mtu.steppables.marketplace.AggregateHarvester;
import edu.mtu.wup.model.Harvesting;

@SuppressWarnings("serial")
public class Nipfo extends ParcelAgent  {

	private double harvestDbh = Harvesting.SawtimberDbh;
	
	private List<NipfoAttitude> attitudes;
	private double score = -1.0;
		
	public Nipfo(ParcelAgentType type, LandUseGeomWrapper lu) {
		super(type, lu);
		attitudes = new ArrayList<NipfoAttitude>();
	}

	@Override
	public void doHarvestedOperation() {
		// TODO Auto-generated method stub
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
		if (!NipfoFis.isYes(score)) {
			if (state.random.nextDouble(true, true) * 10.0 > score) {
				return;
			}
		}

		// Request the harvest
		List<Stand> stands = Harvesting.getHarvestableStands(getParcel(), harvestDbh);
		AggregateHarvester.getInstance().requestHarvest(this, stands);	
	}
	
	public void addAttitude(String label, int value) {
		attitudes.add(new NipfoAttitude(label, value));
	}
	
	public void setAttitudes(List<NipfoAttitude> value) {
		attitudes = value;
	}

	public void addAttitude(String label, double value) {
		attitudes.add(new NipfoAttitude(label, value));
	}
}
