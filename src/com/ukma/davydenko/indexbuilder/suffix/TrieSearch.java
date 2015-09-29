package com.ukma.davydenko.indexbuilder.suffix;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;
import com.ukma.davydenko.indexbuilder.logic.QueryProcessor;
import com.ukma.davydenko.utils.Consts;
import com.ukma.davydenko.utils.Utils;

public class TrieSearch {
	
	private TrieVocabulary directTrie;
	private TrieVocabulary reverseTrie;
	private MyArray<IndexEntry> index;
	private String folderName;
	
	public TrieSearch(TrieVocabulary directTrie, TrieVocabulary reverseTrie, MyArray<IndexEntry> index, String folderName) {
		this.setDirectTrie(directTrie);
		this.setReverseTrie(reverseTrie);
		this.setIndex(index);
		this.setFolderName(folderName);
	}

	public TrieVocabulary getDirectTrie() {
		return directTrie;
	}

	public void setDirectTrie(TrieVocabulary directTrie) {
		this.directTrie = directTrie;
	}

	public TrieVocabulary getReverseTrie() {
		return reverseTrie;
	}

	public void setReverseTrie(TrieVocabulary reverseTrie) {
		this.reverseTrie = reverseTrie;
	}
	
	public MyArray<IndexEntry> getIndex() {
		return index;
	}

	public void setIndex(MyArray<IndexEntry> index) {
		this.index = index;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	public void startTrieSearch() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String input;
		boolean quit = false;

		do {
			try {
				System.out.println();
				System.out.println("Type a query to search in suffix trie");
				System.out.println("To quit, type 'q'");
				
				System.out.print(">");
				input = in.readLine();
				
				QueryProcessor qp = new QueryProcessor(index, folderName);
				if (input.equals("q")) {
					quit = true;
				} else {
					String[] originalTerms = input.toLowerCase().replaceAll("(\\*)" + Consts.punctRegex, Consts.punctReplacement).split(Consts.splitRegexPos);
					
					// handling three cases
					// case 1: query in form of term* - using direct trie
					if (input.indexOf('*') == input.length()-1) {
						MyArray<String> terms = directTrie.getAllSuffixes(originalTerms[0]);
						for (int i = 0; i < terms.size(); ++i) {
							System.out.println(terms.get(i) + " : " + qp.processQuery(terms.get(i)));
						}
					}
					// case 2: query in form of *term - using reverse trie
					else if (input.indexOf('*') == 0) {
						String toSearch = new StringBuilder(originalTerms[1]).reverse().toString();
						System.out.println(toSearch);
						MyArray<String> terms = reverseTrie.getAllSuffixes(toSearch);
						for (int i = 0; i < terms.size(); ++i) {
							String backReverse = new StringBuilder(terms.get(i)).reverse().toString();
							System.out.println(backReverse + " : " + qp.processQuery(backReverse));
						}
					}
					// case 3: query if form of te*rm - using both direct and reverse trie
					else {
						String leftPart = originalTerms[0];
						String rightPart = new StringBuilder(originalTerms[1]).reverse().toString();
						
						MyArray<String> leftPartTerms = directTrie.getAllSuffixes(leftPart);
						MyArray<String> rightPartTerms = reverseTrie.getAllSuffixes(rightPart);
						
						// reversing rightPartTerms
						MyArray<String> rightPartTermsRev = new MyArray<String>();
						for (int i = 0; i < rightPartTerms.size(); ++i) {
							rightPartTermsRev.add(new StringBuilder(rightPartTerms.get(i)).reverse().toString());
						}
						
						// need to sort them
						Arrays.sort(rightPartTermsRev.getRawArray(), 0, rightPartTermsRev.size());
						
						MyArray<String> intersection = Utils.getTermsIntersection(leftPartTerms, rightPartTermsRev);
						
						for (int i = 0; i < intersection.size(); ++i) {
							System.out.println(intersection.get(i) + " : " + qp.processQuery(intersection.get(i)));
						}
					}
					
				}
			} catch (Exception e) {
				System.out.println("Invalid expression or no result");
				e.printStackTrace();
			}
		} while (!quit);
	}
}
