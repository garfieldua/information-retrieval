package com.ukma.davydenko.indexbuilder.compression;

import java.io.Serializable;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class CompressionIndexEntry implements Serializable {
	private static final long serialVersionUID = -6900048458183060169L;
	
	private int frequency;
	private int termPos;
	private byte[] postingsList;
	
	public CompressionIndexEntry(int frequency, int termPos, byte[] postingsList) {
		super();
		this.termPos = termPos;
		this.frequency = frequency;
		this.postingsList = postingsList;
	}
	
	public int getFrequency() {
		return frequency;
	}
	
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	public byte[] getPostingsList() {
		return postingsList;
	}
	
	public void setPostingsList(byte[] postingsList) {
		this.postingsList = postingsList;
	}

	public int getTermPos() {
		return termPos;
	}

	public void setTermPos(int termPos) {
		this.termPos = termPos;
	}
	
}


