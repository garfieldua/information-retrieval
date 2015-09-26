package com.ukma.davydenko.indexbuilder.trigram;

import java.util.Arrays;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;

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
	
	public static MyArray<TrigramIndexPair> getTrigramPairs (MyArray<IndexEntry> index) {
		MyArray<TrigramIndexPair> pairs = new MyArray<>();
		
		for (int i = 0; i < index.size(); ++i) {
			if (index.get(i).getTerm().length() >= 1) {
				MyArray<String> grams = TrigramIndexBuilder.createKGram(3, index.get(i).getTerm());
				
				for (int j = 0; j < grams.size(); ++j) {
					pairs.add(new TrigramIndexPair(grams.get(j), index.get(i).getTerm()));
				}
			}
		}
		
		Arrays.sort(pairs.getRawArray(), 0, pairs.size());
		
		return pairs;
	}
	
	public static MyArray<TrigramIndexEntry> buildIndex (MyArray<TrigramIndexPair> pairs) {
		MyArray<TrigramIndexEntry> index = new MyArray<>();
		
		MyArray<String> terms = new MyArray<>();
		String lastGram = pairs.get(0).getGram();
		
		for (int i = 0; i < pairs.size(); ++i) {
			if (lastGram.equals(pairs.get(i).getGram())) {
				terms.add(pairs.get(i).getTerm());
			} else {
				// saving last processed gram
				index.add(new TrigramIndexEntry(lastGram, terms));
				
				// for new record
				lastGram = pairs.get(i).getGram();
				terms = new MyArray<>();
				terms.add(pairs.get(i).getTerm());
			}
		}
		
		return index;
	}
}
