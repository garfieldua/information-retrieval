package com.ukma.davydenko.indexbuilder.trigram;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class TrigramIndexEntry {
	private String gram;
	private MyArray<String> terms;
	
	public TrigramIndexEntry(String gram, MyArray<String> terms) {
		super();
		this.setGram(gram);
		this.setTerms(terms);
	}

	public String getGram() {
		return gram;
	}

	public void setGram(String gram) {
		this.gram = gram;
	}

	public MyArray<String> getTerms() {
		return terms;
	}

	public void setTerms(MyArray<String> terms) {
		this.terms = terms;
	}

	@Override
	public String toString() {
		return "TrigramIndexEntry [gram=" + gram + ", terms=" + terms + "]";
	}
}
