package edu.mtu.fuzzynipfo.nipfo;

public enum ParcelSize {
	Small,
	Medium,
	Large;
	
	// Fuzzify the parcels : {'small', 'medium', 'large'}
	public static ParcelSize getSize(double acres) {
		if (acres <= 45.0) {
			return Small;
		}
		if (acres <= 95.0) {
			return Medium;
		}
		return Large;
	}
	
	public static ParcelSize getSize(String value) {
		if (value.equals("small")) {
			return Small;
		}
		if (value.equals("medium")) {
			return Medium;
		}
		return Large;
	}
}
