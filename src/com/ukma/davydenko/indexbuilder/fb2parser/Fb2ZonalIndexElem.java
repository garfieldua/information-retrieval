package com.ukma.davydenko.indexbuilder.fb2parser;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class Fb2ZonalIndexElem {
	private int docID;
	private MyArray<Fb2ZonalEnum> zones;
	
	public Fb2ZonalIndexElem(int docID, MyArray<Fb2ZonalEnum> zones) {
		this.setDocID(docID);
		this.setZones(zones);
	}

	public Fb2ZonalIndexElem() {
		zones = new MyArray<>();
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public MyArray<Fb2ZonalEnum> getZones() {
		return zones;
	}

	public void setZones(MyArray<Fb2ZonalEnum> zones) {
		this.zones = zones;
	}
}
