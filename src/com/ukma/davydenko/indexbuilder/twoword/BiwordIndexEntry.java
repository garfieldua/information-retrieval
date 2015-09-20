package com.ukma.davydenko.indexbuilder.twoword;

import java.io.Serializable;
import java.util.List;

public class BiwordIndexEntry implements Serializable, Comparable<BiwordIndexEntry> {
	private static final long serialVersionUID = -8858691814389944141L;
	
	private String term1;
	private String term2;
	private int frequency;
	private List<Integer> postingsList;
	
	public BiwordIndexEntry(String term1, String term2, int frequency, List<Integer> postingsList) {
		super();
		this.setTerm1(term1);
		this.setTerm2(term2);
		this.setFrequency(frequency);
		this.setPostingsList(postingsList);
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

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public List<Integer> getPostingsList() {
		return postingsList;
	}

	public void setPostingsList(List<Integer> postingsList) {
		this.postingsList = postingsList;
	}
	
	@Override
	public int compareTo(BiwordIndexEntry o) {
		int i = term1.compareTo(o.term1);
	    if (i != 0) return i;

	    i = term2.compareTo(o.term2);
	    if (i != 0) return i;
	    
		return i;
	}

	@Override
	public String toString() {
		return "IndexTwoWordEntry [term1=" + term1 + ", term2=" + term2
				+ ", frequency=" + frequency + ", postingsList=" + postingsList
				+ "]";
	}
	
}
