package com.ukma.davydenko.indexbuilder.premuterm;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class PremutermIndexEntry {
	private String premuterm;
	private MyArray<String> terms;
	
	public PremutermIndexEntry(String premuterm, MyArray<String> terms) {
		super();
		this.setPremuterm(premuterm);
		this.setTerms(terms);
	}

	public String getPremuterm() {
		return premuterm;
	}

	public void setPremuterm(String premuterm) {
		this.premuterm = premuterm;
	}

	public MyArray<String> getTerms() {
		return terms;
	}

	public void setTerms(MyArray<String> terms) {
		this.terms = terms;
	}

	@Override
	public String toString() {
		return "PremutermIndexEntry [premuterm=" + premuterm + ", terms="
				+ terms + "]";
	}
}
