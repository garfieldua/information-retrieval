package com.ukma.davydenko.indexbuilder.zonal;

public enum ZonalEnum {
	TITLE (0.7),
	AUTHOR (0.3);

	private double zoneWeight;
	
	ZonalEnum(double zoneWeight) {
		this.zoneWeight = zoneWeight;
	}
	
	public double getZoneWeight() {
		return this.zoneWeight;
	}
}
