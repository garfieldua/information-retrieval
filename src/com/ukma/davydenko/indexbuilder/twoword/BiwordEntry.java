package com.ukma.davydenko.indexbuilder.twoword;

public class BiwordEntry implements Comparable<BiwordEntry> {
	private String term1;
	private String term2;
	private int docID;
	
	public BiwordEntry(String term1, String term2, int docID) {
		super();
		this.setTerm1(term1);
		this.setTerm2(term2);
		this.docID = docID;
	}

	public String getTerm1() {
		return term1;
	}

	public void setTerm1(String term1) {
		this.term1 = term1;
	}

	public String getTerm2() {
		return term2;
	}

	public void setTerm2(String term2) {
		this.term2 = term2;
	}
	
	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}
	
	@Override
	public int compareTo(BiwordEntry o) {
		int i = term1.compareTo(o.term1);
	    if (i != 0) return i;

	    i = term2.compareTo(o.term2);
	    if (i != 0) return i;

	    return Integer.compare(docID, o.docID);
	}

	@Override
	public String toString() {
		return "TWEntry [term1=" + term1 + ", term2=" + term2 + ", docID="
				+ docID + "]";
	}

}
