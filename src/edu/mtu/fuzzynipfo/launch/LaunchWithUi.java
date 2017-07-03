package edu.mtu.fuzzynipfo.launch;

import edu.mtu.fuzzynipfo.Model;
import edu.mtu.simulation.ForestSimException;
import edu.mtu.simulation.ForestSimWithUI;

public class LaunchWithUi {
	public static void main(String[] args) throws ForestSimException {
		Model model = new Model(System.currentTimeMillis());
		ForestSimWithUI fs = new ForestSimWithUI(model);
		fs.load();
	}
}
