package com.ukma.davydenko.indexbuilder.entities;

public class Entry implements Comparable<Entry> {
	private String term;
	private int docID;
	
	public Entry (String term, int docID) {
		this.term = term;
		this.docID = docID;
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

	@Override
	public int compareTo(Entry o) {
		if (!this.term.equals(o.term)) {
			return this.term.compareTo(o.term);
		}
		else {
			return new Integer(this.docID).compareTo(new Integer(o.docID));
		}
	}

	@Override
	public String toString() {
		return "Entry [term=" + term + ", docID=" + docID + "]";
	}
}
