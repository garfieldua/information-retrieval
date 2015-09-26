package com.ukma.davydenko.indexbuilder.premuterm;

public class PremutermIndexPair implements Comparable<PremutermIndexPair> {
	private String premuterm;
	private String term;
	
	public PremutermIndexPair(String premuterm, String term) {
		super();
		this.setPremuterm(premuterm);
		this.setTerm(term);
	}

	public String getPremuterm() {
		return premuterm;
	}

	public void setPremuterm(String premuterm) {
		this.premuterm = premuterm;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	@Override
	public int compareTo(PremutermIndexPair o) {
		int i = premuterm.compareTo(o.premuterm);
	    if (i != 0) return i;

	    i = term.compareTo(o.term);
	    if (i != 0) return i;
	    
		return i;
	}

	@Override
	public String toString() {
		return "PremutermIndexPair [premuterm=" + premuterm + ", term=" + term
				+ "]";
	}
}
