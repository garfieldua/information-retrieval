package com.ukma.davydenko.indexbuilder.suffix;

import java.util.Arrays;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.suffix.Alphabet;

public class TrieVocabulary implements Vocabulary {
	
	private char character;
	private boolean isWord = false;
	// The number of possible children is the number of letters in the alphabet
	private TrieVocabulary[] children = new TrieVocabulary[Alphabet.LOWERCASE.size()];
	// This is the number of actual children
	private int numChildren = 0;
	
	
	public boolean add(String s) {
		char first = s.charAt(0);
		int index = Alphabet.LOWERCASE.getIndex(first);
		
		TrieVocabulary child = children[index];
		if (child == null) {
			child = new TrieVocabulary();
			child.character = first;
			children[index] = child;
			numChildren++;
		}
		if (s.length() == 1) {
			if (child.isWord) {
				// The word is already in the trie
				return false;
			}
			child.isWord = true;
			return true;
		} else {
			// Recurse into sub-trie
			return child.add(s.substring(1));
		}
	}

	/**
	 * Searches for a string in this trie
	 * @param s
	 * @return
	 */
	public boolean contains(String s) {
		TrieVocabulary n = getNode(s);
		return n != null && n.isWord;
	}
	
	/**
	 * Searches for a string prefix in this trie
	 * @param s
	 * @return
	 */
	public boolean isPrefix(String s) {
		TrieVocabulary n = getNode(s);
		return n != null && n.numChildren > 0;
	}

	/**
	 * Returns the node corresponding to the string
	 * @param s
	 * @return
	 */
	public TrieVocabulary getNode(String s) {
		TrieVocabulary node = this;
		for (int i = 0; i < s.length(); i++) {
			int index = Alphabet.LOWERCASE.getIndex(s.charAt(i));
			TrieVocabulary child = node.children[index];
			if (child == null) {
				// There is no such word
				return null;
			}
			node = child;
		}
		return node;
	}

	private MyArray<String> getAllSuffixesHelper(String s, MyArray<String> result) {
		TrieVocabulary t = getNode(s);
		
		String ss = s;
		for (int i = 0; i < t.children.length; ++i) {
			if (t.children[i] != null) {
				ss = s + t.children[i].character;
				if (t.children[i].hasChildren()) {
					getAllSuffixesHelper(ss, result);
				}
				
				if (t.children[i].isWord) {
					result.add(ss);
					ss = s;
				}
			}
			
		}
		return result;
	}
	
	public MyArray<String> getAllSuffixes (String s) {
		MyArray<String> result = new MyArray<>();
		
		return getAllSuffixesHelper(s, result);
	}
	
	public boolean isWord() {
		return isWord;
	}
	
	public boolean hasChildren() {
		return numChildren > 0;
	}
}