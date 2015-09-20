package com.ukma.davydenko.indexbuilder.positional;

import java.io.Serializable;
import java.util.List;

public class PositionalIndexEntry implements Serializable {
	private static final long serialVersionUID = -399205775915356561L;
	
	private String term;
	private int frequency;
	//private Map<Integer, ArrayList<Integer>> postingsList;
	private List<PositionalIndexPositions> postingsList;
	
	public PositionalIndexEntry(String term, int frequency, List<PositionalIndexPositions> postingsList) {
		super();
		this.setTerm(term);
		this.setFrequency(frequency);
		this.setPostingsList(postingsList);
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public List<PositionalIndexPositions> getPostingsList() {
		return postingsList;
	}

	public void setPostingsList(List<PositionalIndexPositions> postingsList) {
		this.postingsList = postingsList;
	}

	@Override
	public String toString() {
		return "PositionalIndexEntry [term=" + term + ", frequency="
				+ frequency + ", postingsList=" + postingsList + "]";
	}
}
