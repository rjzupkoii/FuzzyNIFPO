package edu.mtu.fuzzynipfo.nipfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class NipfoFisTest {
	
	private static final double epsilon = 1e-15;
		
	private static double dontHarvestScore = 0.22567480577136512;
	private static List<NipfoAttitude> dontHarvestAttitudes = new ArrayList<NipfoAttitude>();
	static {
		dontHarvestAttitudes.add(new NipfoAttitude("parcel", 20.0));
		dontHarvestAttitudes.add(new NipfoAttitude("certification", 0.0));
		dontHarvestAttitudes.add(new NipfoAttitude("tax", 0.0));
		dontHarvestAttitudes.add(new NipfoAttitude("management", 0.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CUT_LOG_SALE", 0.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_CLIM", 5.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_DEV", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_FIRE", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_TAX", 2.0));
		dontHarvestAttitudes.add(new NipfoAttitude("CNC_TRES", 2.0));
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
	
	private static double harvestScore = 0.9219632107023416;
	private static List<NipfoAttitude> harvestAttitudes = new ArrayList<NipfoAttitude>();
	static {
		harvestAttitudes.add(new NipfoAttitude("parcel", 100.0));
		harvestAttitudes.add(new NipfoAttitude("certification", 1.0));
		harvestAttitudes.add(new NipfoAttitude("tax", 1.0));
		harvestAttitudes.add(new NipfoAttitude("management", 1.0));
		harvestAttitudes.add(new NipfoAttitude("CUT_LOG_SALE", 0.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_CLIM", 2.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_DEV", 2.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_FIRE", 2.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_TAX", 2.0));
		harvestAttitudes.add(new NipfoAttitude("CNC_TRES", 2.0));
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
	
	// Truth table for fuzzified results, {'no', 'maybe', 'yes'}
	private static HashMap<Double, Boolean[]> truthTable = new HashMap<Double, Boolean[]>();
	static {
		truthTable.put(0.0, new Boolean[] { true, false, false } );
		truthTable.put(0.1, new Boolean[] { true, false, false } );
		truthTable.put(0.2, new Boolean[] { true, false, false } );
		truthTable.put(0.3, new Boolean[] { true, false, false } );
		truthTable.put(0.4, new Boolean[] { true, false, false } );
		truthTable.put(0.5, new Boolean[] { false, true, false } );
		truthTable.put(0.6, new Boolean[] { false, true, false } );
		truthTable.put(0.7, new Boolean[] { false, true, false } );
		truthTable.put(0.8, new Boolean[] { false, true, false } );
		truthTable.put(0.9, new Boolean[] { false, false, true } );
		truthTable.put(1.0, new Boolean[] { false, false, true } );
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
	
	/**
	 * Test to make sure 'maybe' works.
	 */
	@Test
	public void isMaybeTest() {
		for (double key : truthTable.keySet()) {
			boolean expected = truthTable.get(key)[1];
			Assert.assertEquals(expected, NipfoFis.isMaybe(key));
		}
	}
	
	/**
	 * Test to make sure 'no' works.
	 */
	@Test
	public void isNoTest() {
		for (double key : truthTable.keySet()) {
			boolean expected = truthTable.get(key)[0];
			Assert.assertEquals(expected, NipfoFis.isNo(key));
		}
	}
	
	/**
	 * Test to make sure 'yes' work.
	 */
	@Test
	public void isYesTest() {
		for (double key : truthTable.keySet()) {
			boolean expected = truthTable.get(key)[2];
			Assert.assertEquals(expected, NipfoFis.isYes(key));
		}
	}
}
