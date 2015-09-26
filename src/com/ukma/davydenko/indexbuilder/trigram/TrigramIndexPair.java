package com.ukma.davydenko.indexbuilder.trigram;

public class TrigramIndexPair implements Comparable<TrigramIndexPair> {
	private String gram;
	private String term;
	
	public TrigramIndexPair(String gram, String term) {
		super();
		this.setGram(gram);
		this.setTerm(term);
	}

	public String getGram() {
		return gram;
	}

	public void setGram(String gram) {
		this.gram = gram;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	@Override
	public int compareTo(TrigramIndexPair o) {
		int i = term.compareTo(o.term);
	    if (i != 0) return i;

	    i = gram.compareTo(o.gram);
	    if (i != 0) return i;
	    
		return i;
	}	
}
