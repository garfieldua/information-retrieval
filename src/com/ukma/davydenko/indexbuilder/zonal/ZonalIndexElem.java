package com.ukma.davydenko.indexbuilder.zonal;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class ZonalIndexElem {
	private int docID;
	private MyArray<ZonalEnum> zones;
	
	public ZonalIndexElem(int docID, MyArray<ZonalEnum> zones) {
		this.setDocID(docID);
		this.setZones(zones);
	}

	public ZonalIndexElem() {
		zones = new MyArray<>();
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public MyArray<ZonalEnum> getZones() {
		return zones;
	}

	public void setZones(MyArray<ZonalEnum> zones) {
		this.zones = zones;
	}
}
