package com.ukma.davydenko.indexbuilder.zonal;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class ZonalIndexEntry {
	private String term;
	private MyArray<ZonalIndexElem> zonalPostingsList;
	
	public ZonalIndexEntry(String term, MyArray<ZonalIndexElem> zonalPostingsList) {
		this.setTerm(term);
		this.setZonalPostingsList(zonalPostingsList);
	}

	public ZonalIndexEntry() {
		zonalPostingsList = new MyArray<>();
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public MyArray<ZonalIndexElem> getZonalPostingsList() {
		return zonalPostingsList;
	}

	public void setZonalPostingsList(MyArray<ZonalIndexElem> zonalPostingsList) {
		this.zonalPostingsList = zonalPostingsList;
	}
}
