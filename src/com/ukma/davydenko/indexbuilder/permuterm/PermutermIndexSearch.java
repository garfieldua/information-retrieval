package com.ukma.davydenko.indexbuilder.permuterm;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;
import com.ukma.davydenko.indexbuilder.logic.QueryProcessor;
import com.ukma.davydenko.indexbuilder.suffix.TrieVocabulary;

public class PermutermIndexSearch {
	private TrieVocabulary trie;
	private MyArray<PermutermIndexPair> premutermIndex;
	private MyArray<IndexEntry> index;
	private String folderName;
	
	
	public PermutermIndexSearch(TrieVocabulary trie,
			MyArray<PermutermIndexPair> premutermIndex,
			MyArray<IndexEntry> index, String folderName) {
		this.trie = trie;
		this.premutermIndex = premutermIndex;
		this.index = index;
		this.folderName = folderName;
	}

	// rotates string, adds '$' sign
	private String prepareQuery (String query) {
		String preparedQuery = query + "$";
		
		while (preparedQuery.charAt(preparedQuery.length()-1) != '*') {
			char firstChar = preparedQuery.charAt(0);
			preparedQuery = preparedQuery.substring(1, preparedQuery.length()) + firstChar;
		}
		
		return preparedQuery;
	}
	
	public int binarySearch(String word) {
        return binarySearch(word, 0, premutermIndex.size());
    }

    private int binarySearch(String word, int min, int max) {
        if (min > max) {
            return -1;
        }
        
        int mid = (max + min) / 2;
        
        if (premutermIndex.get(mid).getPremuterm().equals(word)) {
            return mid;
        } else if(premutermIndex.get(mid).getPremuterm().compareTo(word) > 0) {
            return binarySearch(word, min, mid - 1);
        } else {
            return binarySearch(word, mid + 1, max);
        }
    }
	
	public void startPermutermSearch() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String input;
		boolean quit = false;

		do {
			try {
				System.out.println();
				System.out.println("Type a query to search in permuterm index");
				System.out.println("To quit, type 'q'");
				
				System.out.print(">");
				input = in.readLine();
				
				QueryProcessor qp = new QueryProcessor(index, folderName);
				if (input.equals("q")) {
					quit = true;
				} else {
					//String[] originalTerms = input.toLowerCase().replaceAll("(\\*)" + Consts.punctRegex, Consts.punctReplacement).split(Consts.splitRegexPos);
					String preparedQuery = prepareQuery(input);
					preparedQuery = preparedQuery.substring(0, preparedQuery.length()-1);
					
					MyArray<String> premuterms = trie.getAllSuffixes(preparedQuery);

					// finding corresponding terms for found premutations
					MyArray<String> terms = new MyArray<>();
					for (int i = 0; i < premuterms.size(); ++i) {
						terms.add(premutermIndex.get(binarySearch(premuterms.get(i))).getTerm());
					}
					
					for (int i = 0; i < terms.size(); ++i) {
						System.out.println(terms.get(i) + " : " + qp.processQuery(terms.get(i)));
					}
				}
			} catch (Exception e) {
				System.out.println("Invalid expression or no result");
				e.printStackTrace();
			}
		} while (!quit);
	}
}
