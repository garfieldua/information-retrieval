package com.ukma.davydenko.indexbuilder.entities;

import java.io.Serializable;
import java.util.BitSet;

public class MatrixEntry implements Serializable {
	private static final long serialVersionUID = 2282844681078839375L;
	
	private String term;
	private BitSet entranceVector;
	
	public MatrixEntry(String term, BitSet entranceVector) {
		super();
		this.setTerm(term);
		this.setEntranceVector(entranceVector);
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public BitSet getEntranceVector() {
		return entranceVector;
	}

	public void setEntranceVector(BitSet entranceVector) {
		this.entranceVector = entranceVector;
	}
}
