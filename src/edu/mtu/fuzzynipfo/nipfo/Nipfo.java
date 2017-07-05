package edu.mtu.fuzzynipfo.nipfo;

import java.util.List;

import edu.mtu.steppables.LandUseGeomWrapper;
import edu.mtu.steppables.ParcelAgent;
import edu.mtu.steppables.ParcelAgentType;

@SuppressWarnings("serial")
public class Nipfo extends ParcelAgent  {

	List<NipfoAttitude> attitudes;
	
	public Nipfo(ParcelAgentType type, LandUseGeomWrapper lu) {
		super(type, lu);
	}

	@Override
	public void doHarvestedOperation() {
		// Get the result of the FIS
		NipfoFis fis = NipfoFis.getInstance();
		double result = fis.evaluate(attitudes);
		
		// Request a harvest if appropriate
		if (!NipfoFis.isYes(result)) {
			return;
		}
		
	}

	@Override
	protected void doPolicyOperation() {
		
		// TODO Write policy operation code
	
	}

	@Override
	protected void doHarvestOperation() {
		// TODO Auto-generated method stub
		
	}
	
	public void setAttitudes(List<NipfoAttitude> value) {
		attitudes = value;
	}
}
