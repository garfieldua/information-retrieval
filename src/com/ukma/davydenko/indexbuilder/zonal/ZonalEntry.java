package com.ukma.davydenko.indexbuilder.zonal;

public class ZonalEntry implements Comparable<ZonalEntry> {
	private String term;
	private int docID;
	private ZonalEnum zone;
	
	public ZonalEntry (String term, int docID, ZonalEnum zone) {
		this.term = term;
		this.docID = docID;
		this.setZone(zone);
	}
	
	public String getTerm() {
		return term;
	}
	
	public void setTerm(String term) {
		this.term = term;
	}
	
	public int getDocID() {
		return docID;
	}
	
	public void setDocID(int docID) {
		this.docID = docID;
	}
	
	public ZonalEnum getZone() {
		return zone;
	}

	public void setZone(ZonalEnum zone) {
		this.zone = zone;
	}

	@Override
	public int compareTo(ZonalEntry o) {
		if (!this.term.equals(o.term)) {
			return this.term.compareTo(o.term);
		}
		else if (!(this.docID == o.docID)) {
			return new Integer(this.docID).compareTo(new Integer(o.docID));
		} else {
			return this.zone.compareTo(o.zone);
		}
	}
	
}
