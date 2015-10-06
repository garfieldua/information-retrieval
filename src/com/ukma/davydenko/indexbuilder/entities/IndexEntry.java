package com.ukma.davydenko.indexbuilder.entities;

import java.io.Serializable;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class IndexEntry implements Serializable, Comparable<IndexEntry> {
	private static final long serialVersionUID = -6900048458180060169L;
	
	private String term;
	private int frequency;
	private MyArray<Integer> postingsList;
	
	public IndexEntry(String term, int frequency, MyArray<Integer> postingsList) {
		super();
		this.term = term;
		this.frequency = frequency;
		this.postingsList = postingsList;
	}
	
	public IndexEntry(String str) {
		String[] groups = str.split(" ");
		String[] docIDs = groups[1].split(",");

		MyArray<Integer> docIDarr = new MyArray<>();
		
		for (String docID : docIDs) {
			docIDarr.add(Integer.parseInt(docID));
		}
		
		this.term = groups[0];
		this.postingsList = docIDarr;
		this.frequency = docIDarr.size();
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
	
	public MyArray<Integer> getPostingsList() {
		return postingsList;
	}
	
	public void setPostingsList(MyArray<Integer> postingsList) {
		this.postingsList = postingsList;
	}

	@Override
	public int compareTo(IndexEntry o) {
		return this.term.compareTo(o.term);
	}
	
	
}
