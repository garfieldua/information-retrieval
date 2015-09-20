package com.ukma.davydenko.indexbuilder.twoword;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TwoWordIndexSearch {
	private List<Integer> allDocIDs;
	private List<TwoWordIndexEntry> index;
	
	static String splitRegex = "[^a-zA-Z]+";
	
	public TwoWordIndexSearch(List<TwoWordIndexEntry> index, String folder) {
		this.index = index;
		this.allDocIDs = new ArrayList<>();
		
		try {
			Files.walk(Paths.get(new File(folder).getAbsolutePath())).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			    	int docID = Integer.parseInt(filePath.getFileName().toString().replaceFirst("[.][^.]+$", ""));
			    	allDocIDs.add(docID);
			    }
			});
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Collections.sort(allDocIDs);
	}
	
	public int binarySearch(String term1, String term2) {
        return binarySearch(term1, term2, 0, index.size());
    }

    private int binarySearch(String term1, String term2, int min, int max) {
        if (min > max) {
            return -1;
        }
        
        int mid = (max + min) / 2;
        
        if (index.get(mid).getTerm1().equals(term1) && index.get(mid).getTerm2().equals(term2)) {
            return mid;
        } else if(index.get(mid).getTerm1().equals(term1) && index.get(mid).getTerm2().compareTo(term2) > 0) {
            return binarySearch(term1, term2, min, mid - 1);
        } else if(index.get(mid).getTerm1().equals(term1) && index.get(mid).getTerm2().compareTo(term2) < 0) {
            return binarySearch(term1, term2, mid + 1, max);
        } else if(index.get(mid).getTerm1().compareTo(term1) > 0) {
            return binarySearch(term1, term2, min, mid - 1);
        } else {
            return binarySearch(term1, term2, mid + 1, max);
        }
    }
    
    public List<Integer> intersect(List<Integer> list1, List<Integer> list2) {
    	List<Integer> resultList = new ArrayList<>();
    	
    	int i = 0;
    	int j = 0;
    	
    	while (i < list1.size() && j < list2.size()) {
    		if (list1.get(i) == list2.get(j)) {
    			resultList.add(list1.get(i));
    			++i;
    			++j;
    		} else if (list1.get(i) < list2.get(j)) {
    			++i;
    		} else {
    			++j;
    		}
    	}
    	
    	return resultList;
    }
    
    public void startTwoIndexSearch() {
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String input;
		boolean quit = false;

		do {
			try {
				System.out.print(">");
				input = in.readLine();
				if (input.equals("q")) {
					quit = true;
				} else {
					String[] terms = input.toLowerCase().replaceAll("\\p{Punct}", "").split(splitRegex);
					
					//System.out.println(Arrays.toString(terms));
					
					List<Integer> entry = new ArrayList<>();
					for (int i = 0; i < terms.length-1; ++i) {
						//System.out.println(terms[i] + ' ' + terms[i+1]);
						if (i == 0) {
							entry = index.get(binarySearch(terms[i], terms[i+1])).getPostingsList();
						}
						else {
							entry = intersect(entry, index.get(binarySearch(terms[i], terms[i+1])).getPostingsList());
						}
					}
					
					System.out.println(entry);
				}
			} catch (Exception e) {
				System.out.println("Invalid expression or no result");
			}
		} while (!quit);
    }
}
