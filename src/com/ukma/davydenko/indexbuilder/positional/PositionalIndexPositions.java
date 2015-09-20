package com.ukma.davydenko.indexbuilder.positional;

import java.io.Serializable;
import java.util.List;

public class PositionalIndexPositions implements Serializable {
	private static final long serialVersionUID = -6283499394761061595L;
	
	private int docID;
	private List<Integer> positions;
	
	public PositionalIndexPositions(int docID, List<Integer> positions) {
		super();
		this.setDocID(docID);
		this.setPositions(positions);
	}

	public int getDocID() {
		return docID;
	}

	public void setDocID(int docID) {
		this.docID = docID;
	}

	public List<Integer> getPositions() {
		return positions;
	}

	public void setPositions(List<Integer> positions) {
		this.positions = positions;
	}

	@Override
	public String toString() {
		return "PositionalIndexPositions [docID=" + docID + ", positions="
				+ positions + "]";
	}
}
