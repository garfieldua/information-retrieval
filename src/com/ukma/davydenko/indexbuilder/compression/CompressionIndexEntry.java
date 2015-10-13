package com.ukma.davydenko.indexbuilder.compression;

import java.io.Serializable;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class CompressionIndexEntry implements Serializable, Comparable<CompressionIndexEntry> {
	private static final long serialVersionUID = -6900048458183060169L;
	
	private String term;
	private int frequency;
	private MyArray<Byte> postingsList;
	
	public CompressionIndexEntry(String term, int frequency, MyArray<Byte> postingsList) {
		super();
		this.term = term;
		this.frequency = frequency;
		this.postingsList = postingsList;
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
	
	public MyArray<Byte> getPostingsList() {
		return postingsList;
	}
	
	public void setPostingsList(MyArray<Byte> postingsList) {
		this.postingsList = postingsList;
	}

	@Override
	public int compareTo(CompressionIndexEntry o) {
		return this.term.compareTo(o.term);
	}
	
}


