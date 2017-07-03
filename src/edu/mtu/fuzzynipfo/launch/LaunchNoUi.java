package edu.mtu.fuzzynipfo.launch;

import edu.mtu.fuzzynipfo.Model;
import edu.mtu.simulation.ForestSim;

public class LaunchNoUi {
	public static void main(String[] args) {
		ForestSim.load(Model.class, args);
	}
}
