package edu.mtu.fuzzynipfo.nipfo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import net.sourceforge.jFuzzyLogic.FIS;

/**
 * Expose the FIS as a singleton to save on model overhead.
 */
public class NipfoFis {
	private FIS fis;
	private static NipfoFis instance = new NipfoFis();
	
	private final static String fileName = "fcl/harvest.fcl";
	
	private HashMap<String, HashMap<ParcelSize, HashMap<Integer, Double>>> attitudes; 
	
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
	 * Get the list of attitudes that have been loaded.
	 * 
	 * @return A list of attitude names.
	 */
	public List<String> getAttitudeList() {
		if (attitudes == null) {
			throw new IllegalStateException("NWOS attitdues have not been loaded.");
		}
		
		return new ArrayList<String>(attitudes.keySet());
	}
	
	/**
	 * Get the map of attitude values and odds.
	 * 
	 * @param attitude The name of the attitude.
	 * @param size The linguistic label of the parcel size.
	 * @return The map of values and their odds.
	 */
	public HashMap<Integer, Double> getAttitudeMap(String attitude, ParcelSize size) {
		if (attitudes == null) {
			throw new IllegalStateException("NWOS attitudes have not been loaded.");
		}
		
		return attitudes.get(attitude).get(size);
	}
	
	/**
	 * Return true if the defuzzified score given evaluates to 'yes' (i.e., should harvest).
	 */
	public static boolean isYes(double score) {
		return (score >= 8.0);
	}
	
	/**
	 * Load the contents of the files to the attitudes map once.
	 * 
	 * @param path to the directory with the files.
	 */
	public void loadAttitudeData(String path) throws IOException {
		attitudes = new HashMap<String, HashMap<ParcelSize,HashMap<Integer, Double>>>();
		
		for (File file : new File(path).listFiles()) {
			// Get the base name
			String name = file.getName();
			name = name.substring(0, name.indexOf('.') - 1);
			
			// Place the loaded map
			attitudes.put(name, loadAttitudeMap(file));
		}
	}
	
	/**
	 * Load the attitudes from the file provided.
	 */
	private HashMap<ParcelSize, HashMap<Integer, Double>> loadAttitudeMap(File file) throws IOException {
		HashMap<ParcelSize, HashMap<Integer, Double>> map = new HashMap<ParcelSize, HashMap<Integer,Double>>();
		
		// Load the CSV file, discarding the header
		CSVReader reader = new CSVReader(new FileReader(file));
		String[] enteries = reader.readNext();
		
		// Read the contents of the file
		while ((enteries = reader.readNext()) != null) { 
			// Note the size and prepare the map
			ParcelSize size = ParcelSize.getSize(enteries[1]);
			if (!map.containsKey(size)) {
				map.put(size, new HashMap<Integer, Double>());
			}
			
			// Add the entry
			int value = Integer.parseInt(enteries[0]);
			double odds = Double.parseDouble(enteries[2]);
			map.get(size).put(value, odds);
		}
		
		// Close and return
		reader.close();
		return map;
	}
}
