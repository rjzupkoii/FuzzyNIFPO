package edu.mtu.fuzzynipfo.nipfo;

public class NipfoAttitude {
	private String label;
	private double value;
	
	public NipfoAttitude(String label, double value) {
		this.label = label;
		this.value = value;
	}

	public String getLabel() {
		return label;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
