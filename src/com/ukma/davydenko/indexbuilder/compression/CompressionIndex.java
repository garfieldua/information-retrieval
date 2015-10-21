package com.ukma.davydenko.indexbuilder.compression;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class CompressionIndex {
	private String vocabulary;
	private MyArray<CompressionIndexEntry> index;
	
	public CompressionIndex() {
		vocabulary = new String();
		index = new MyArray<>();
	}
	
	public CompressionIndex(String vocabulary, MyArray<CompressionIndexEntry> index) {
		this.vocabulary = vocabulary;
		this.index = index;
	}
	
	public String getVocabulary() {
		return vocabulary;
	}
	
	public void setVocabulary(String vocabulary) {
		this.vocabulary = vocabulary;
	}
	
	public MyArray<CompressionIndexEntry> getIndex() {
		return index;
	}
	
	public void setIndex(MyArray<CompressionIndexEntry> index) {
		this.index = index;
	}
}
