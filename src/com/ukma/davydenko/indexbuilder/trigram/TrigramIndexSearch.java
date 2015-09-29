package com.ukma.davydenko.indexbuilder.trigram;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;
import com.ukma.davydenko.indexbuilder.logic.QueryProcessor;
import com.ukma.davydenko.utils.Consts;
import com.ukma.davydenko.utils.Utils;

public class TrigramIndexSearch {
	private MyArray<TrigramIndexEntry> trigramIndex;
	private MyArray<IndexEntry> index;
	private String folderName;

	public TrigramIndexSearch(MyArray<TrigramIndexEntry> trigramIndex, MyArray<IndexEntry> index, String folderName) {
		this.trigramIndex = trigramIndex;
		this.index = index;
		this.folderName = folderName;
	}
	
	public int binarySearch(String gram) {
        return binarySearch(gram, 0, trigramIndex.size());
    }

    private int binarySearch(String gram, int min, int max) {
        if (min > max) {
            return -1;
        }
        
        int mid = (max + min) / 2;
        
        if (trigramIndex.get(mid).getGram().equals(gram)) {
            return mid;
        } else if(trigramIndex.get(mid).getGram().compareTo(gram) > 0) {
            return binarySearch(gram, min, mid - 1);
        } else {
            return binarySearch(gram, mid + 1, max);
        }
    }
	
    private MyArray<String> decoupleTerm(String term) {
    	int k = 3;
    	MyArray<String> result = new MyArray<>();
    	
    	int curIndex = -1;
		String gram = null;
		
		do {
			gram = term.substring(++curIndex, curIndex + k);
			
			result.add(gram);
		} while (curIndex + k < term.length());
    	
    	return result;
    }
    
	
	
	public void startTrigramIndexSearch() {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String input;
		boolean quit = false;

		do {
			try {
				System.out.println();
				System.out.println("Type a query to search in trigram index");
				System.out.println("To quit, type 'q'");
				
				System.out.print(">");
				input = in.readLine();
				if (input.equals("q")) {
					quit = true;
				} else {
					if (input.contains("*")) {
						String[] originalTerms = input.toLowerCase().replaceAll("(\\*)" + Consts.punctRegex, Consts.punctReplacement).split(Consts.splitRegexPos);
						
						input = "$" + input + "$";
						
						String[] terms = input.toLowerCase().replaceAll("(\\*\\$)" + Consts.punctRegex, Consts.punctReplacement).split(Consts.splitRegexGram);

						//System.out.println(Arrays.toString(originalTerms));
						//System.out.println(Arrays.toString(terms));
						
						// decouple grams if needed
						MyArray<String> degramedTerms = new MyArray<>();
						for (String term : terms) {
							if (term.length() > 3) {
								MyArray<String> decoupledTerms = decoupleTerm(term);
								for (int i = 0; i < decoupledTerms.size(); ++i) {
									degramedTerms.add(decoupledTerms.get(i));
								}
							} else if (!term.equals("$")) {
								degramedTerms.add(term);
							}
						}
						System.out.println(Arrays.toString(degramedTerms.getRawArray()));
						
						MyArray<String> curTerms = new MyArray<>();
						
						int i = 0;
						if (degramedTerms.size() == 1) {
							curTerms = trigramIndex.get(binarySearch(degramedTerms.get(i))).getTerms();
						} else {
							while (i < degramedTerms.size() - 1) {
								if (i == 0) {
									curTerms = Utils.getTermsIntersection(trigramIndex.get(binarySearch(degramedTerms.get(i))).getTerms(), trigramIndex.get(binarySearch(degramedTerms.get(i+1))).getTerms());
								} else {
									curTerms = Utils.getTermsIntersection(curTerms, trigramIndex.get(binarySearch(degramedTerms.get(i+1))).getTerms());
								}
								
								++i;
							}
						}
						
						// filtering false positive results
						MyArray<String> filtered = new MyArray<>();
						for (int j = 0; j < curTerms.size(); ++j) {
							boolean flag = true;
							for (int k = 0; k < originalTerms.length; ++k) {
								// checking correctness of beginning of word
								if (k == 0) {
									if (!curTerms.get(j).substring(0, originalTerms[k].length()).equals(originalTerms[k])) {
										flag = false;
									}
								}
								// checking correctness of ending of word
								else if (k == originalTerms.length - 1) {
									if (!curTerms.get(j).substring(curTerms.get(j).length()-originalTerms[k].length(), curTerms.get(j).length()).equals(originalTerms[k])) {
										flag = false;
									}
								}
								// need to check containing of substring
								else {
									if (!curTerms.get(j).contains(originalTerms[k])) {
										flag = false;
									}
								}
							}
							
							if (flag == true) {
								filtered.add(curTerms.get(j));
							}
						}
						
						// retrieving doc id's
						QueryProcessor qp = new QueryProcessor(index, folderName);
						for (int j = 0; j < filtered.size(); ++j) {
							System.out.println(filtered.get(j) + " : " + qp.processQuery(filtered.get(j)));
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
