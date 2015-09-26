package com.ukma.davydenko.indexbuilder.premuterm;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class PremutermIndexBuilder {
	public static MyArray<String> permuterm(String term) {
		MyArray<String> result = new MyArray<>();
		
		String newTerm = term + "$";
		
		for (int i = 0; i < newTerm.length(); ++i) {
			char firstChar = newTerm.charAt(0);
			result.add(newTerm);
			newTerm = newTerm.substring(1, newTerm.length()) + firstChar;
		}
		
		return result;
	}
}
