package com.ukma.davydenko.indexbuilder.fb2parser;

public enum Fb2ZonalEnum {
	TITLE (0.2),
	AUTHOR (0.3),
	BODY (0.5);

	private double zoneWeight;
	
	Fb2ZonalEnum(double zoneWeight) {
		this.zoneWeight = zoneWeight;
	}
	
	public double getZoneWeight() {
		return this.zoneWeight;
	}
}
