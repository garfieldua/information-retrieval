package com.ukma.davydenko.indexbuilder.premuterm;

import java.util.Arrays;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;
import com.ukma.davydenko.indexbuilder.trigram.TrigramIndexEntry;

public class PremutermIndexBuilder {
	public static MyArray<String> getPermuterm(String term) {
		MyArray<String> result = new MyArray<>();
		
		String newTerm = term + "$";
		
		for (int i = 0; i < newTerm.length(); ++i) {
			char firstChar = newTerm.charAt(0);
			result.add(newTerm);
			newTerm = newTerm.substring(1, newTerm.length()) + firstChar;
		}
		
		return result;
	}
	
	public static MyArray<PremutermIndexPair> getPremutermPairs(MyArray<IndexEntry> index) {
		MyArray<PremutermIndexPair> pairs = new MyArray<>();
		
		for (int i = 0; i < index.size(); ++i) {
			MyArray<String> grams = PremutermIndexBuilder.getPermuterm(index.get(i).getTerm());
			
			for (int j = 0; j < grams.size(); ++j) {
				pairs.add(new PremutermIndexPair(grams.get(j), index.get(i).getTerm()));
			}
		}
		
		Arrays.sort(pairs.getRawArray(), 0, pairs.size());
		
		return pairs;
	}
	
}
