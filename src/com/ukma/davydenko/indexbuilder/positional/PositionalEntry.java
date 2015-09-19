package com.ukma.davydenko.indexbuilder.positional;

public class PositionalEntry implements Comparable<PositionalEntry> {
	private String term;
	private int docID;
	private int position;
	
	public PositionalEntry(String term, int docID, int position) {
		super();
		this.setTerm(term);
		this.setDocID(docID);
		this.setPosition(position);
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

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public int compareTo(PositionalEntry o) {
		int i = term.compareTo(o.term);
	    if (i != 0) return i;

	    i = Integer.compare(docID, o.docID);
	    if (i != 0) return i;

	    return Integer.compare(position, o.position);
	}

	@Override
	public String toString() {
		return "PositionalEntry [term=" + term + ", docID=" + docID
				+ ", position=" + position + "]";
	}
}
