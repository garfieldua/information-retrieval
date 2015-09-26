package com.ukma.davydenko.indexbuilder.trigram;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class TrigramIndexBuilder {
	public static MyArray<String> createKGram(int k, String term) {
		MyArray<String> result = new MyArray<>();
		
		String newTerm = "$" + term + "$";
		
		int curIndex = -1;
		String gram = null;
		
		do {
			gram = newTerm.substring(++curIndex, curIndex + k);
			result.add(gram);
		} while (curIndex + k < newTerm.length());
		
		return result;
	}
	
	
}
