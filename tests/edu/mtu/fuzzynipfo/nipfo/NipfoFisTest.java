package edu.mtu.fuzzynipfo.nipfo;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class NipfoFisTest {
	
	private static final double epsilon = 1e-15;

	private static double dontHarvestScore = 0.0;
	private static List<NipfoAttitude> dontHarvestAttitudes = new ArrayList<NipfoAttitude>();
	static {
		dontHarvestAttitudes.add(new NipfoAttitude("parcel", 50.0));
		dontHarvestAttitudes.add(new NipfoAttitude("certification", 0.0));
		dontHarvestAttitudes.add(new NipfoAttitude("tax", 0.0));
		dontHarvestAttitudes.add(new NipfoAttitude("management", 0.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_CLIM", 5.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_DEV", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_FIRE", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_TAX", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_TRES", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_LOG_SALE", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_BEA", 5.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_CHILD", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_HUNT", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_INV", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_NAT", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_PRI", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_REC", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_TIM", 1.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_WAT", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("OBJ_WIL", 2.0));
	}
	
	private static double harvestScore = 8.730801603206329;
	private static List<NipfoAttitude> harvestAttitudes = new ArrayList<NipfoAttitude>();
	static {
		harvestAttitudes.add(new NipfoAttitude("parcel", 100.0));
		harvestAttitudes.add(new NipfoAttitude("certification", 1.0));
		harvestAttitudes.add(new NipfoAttitude("tax", 1.0));
		harvestAttitudes.add(new NipfoAttitude("management", 1.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_CLIM", 2.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_DEV", 2.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_FIRE", 2.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_TAX", 2.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_TRES", 2.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_LOG_SALE", 2.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_BEA", 2.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_CHILD", 2.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_HUNT", 2.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_INV", 2.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_NAT", 2.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_PRI", 2.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_REC", 2.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_TIM", 5.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_WAT", 2.0));
		harvestAttitudes.add(new NipfoAttitude("OBJ_WIL", 2.0));
	}
	
	/**
	 * Test to make sure the FIS results in the NIPFO not harvesting.
	 */
	@Test
	public void evaulateDontHarvestTest() {
		NipfoFis fis = NipfoFis.getInstance();
		
		// Check to make sure the NIPFO does not harvest
		double result = fis.evaluate(dontHarvestAttitudes);
		Assert.assertEquals(dontHarvestScore, result, epsilon);

	}
	
	/**
	 * Test to make sure the FIS results in the NIFPO harvesting.
	 */
	@Test
	public void evaluateHarvestTest() {
		NipfoFis fis = NipfoFis.getInstance();
		
		// Check to make sure the NIPFO harvest
		double result = fis.evaluate(harvestAttitudes);
		Assert.assertEquals(harvestScore, result, epsilon);
		
	}
}
