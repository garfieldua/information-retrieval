package com.ukma.davydenko.indexbuilder.fb2parser;

import com.ukma.davydenko.indexbuilder.fb2parser.Fb2ZonalEnum;

public class Fb2ZonalEntry implements Comparable<Fb2ZonalEntry> {
	private String term;
	private int docID;
	private Fb2ZonalEnum zone;
	
	public Fb2ZonalEntry (String term, int docID, Fb2ZonalEnum zone) {
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
	
	public Fb2ZonalEnum getZone() {
		return zone;
	}

	public void setZone(Fb2ZonalEnum zone) {
		this.zone = zone;
	}

	@Override
	public int compareTo(Fb2ZonalEntry o) {
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
