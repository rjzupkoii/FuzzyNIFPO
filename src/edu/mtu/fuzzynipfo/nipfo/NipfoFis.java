package edu.mtu.fuzzynipfo.nipfo;

import java.util.List;

import net.sourceforge.jFuzzyLogic.FIS;

/**
 * Expose the FIS as a singleton to save on model overhead.
 */
public class NipfoFis {
	private FIS fis;
	private static NipfoFis instance = new NipfoFis();
	
	private final static String fileName = "fcl/harvest.fcl";
	
	/**
	 * Constructor.
	 */
	private NipfoFis() {
		fis = FIS.load(fileName, true);
	}
	
	/**
	 * Get the instance of the FIS.
	 */
	public static NipfoFis getInstance() {
		return instance;
	}
	
	/**
	 * Use the FIS to evaluate NIPFO decision making.
	 * 
	 * @param variables The NIPFO attitudes.
	 * @return The result of the FIS evaluation.
	 */
	public double evaluate(List<NipfoAttitude> variables) {
		// Load the attitudes
		for (NipfoAttitude attitude : variables) {
			fis.setVariable(attitude.getLabel(), attitude.getValue());	
		}
		
		// Evaluate and return the results
		fis.evaluate();		
		double result = fis.getVariable("result").getValue();
		return result;
	}
	
	/**
	 * Return true if the defuzzified score given evaluates to 'yes' (i.e., should harvest).
	 */
	public static boolean isYes(double score) {
		return (score >= 8.0);
	}
}
