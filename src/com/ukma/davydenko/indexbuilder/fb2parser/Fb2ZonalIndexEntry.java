package com.ukma.davydenko.indexbuilder.fb2parser;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.zonal.ZonalIndexElem;

public class Fb2ZonalIndexEntry {
	private String term;
	private MyArray<Fb2ZonalIndexElem> zonalPostingsList;
	
	public Fb2ZonalIndexEntry(String term, MyArray<Fb2ZonalIndexElem> zonalPostingsList) {
		this.setTerm(term);
		this.setZonalPostingsList(zonalPostingsList);
	}

	public Fb2ZonalIndexEntry() {
		zonalPostingsList = new MyArray<>();
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public MyArray<Fb2ZonalIndexElem> getZonalPostingsList() {
		return zonalPostingsList;
	}

	public void setZonalPostingsList(MyArray<Fb2ZonalIndexElem> zonalPostingsList) {
		this.zonalPostingsList = zonalPostingsList;
	}
}
