package com.ukma.davydenko.indexbuilder.positional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ukma.davydenko.utils.Consts;

public class PositionalIndexSearch {	
	private List<PositionalIndexEntry> index;
	
	public PositionalIndexSearch(List<PositionalIndexEntry> index) {
		super();
		this.index = index;
	}

	public int binarySearch(String word) {
        return binarySearch(word, 0, index.size());
    }

    private int binarySearch(String word, int min, int max) {
        if (min > max) {
            return -1;
        }
        
        int mid = (max + min) / 2;
        
        if (index.get(mid).getTerm().equals(word)) {
            return mid;
        } else if(index.get(mid).getTerm().compareTo(word) > 0) {
            return binarySearch(word, min, mid - 1);
        } else {
            return binarySearch(word, mid + 1, max);
        }
    }
	
    class PositionalIndexTriple {
		int docID;
		int position1;
		int position2;
		
		public PositionalIndexTriple(int docID, int position1, int position2) {
			this.docID = docID;
			this.position1 = position1;
			this.position2 = position2;
		}

		@Override
		public String toString() {
			return "PositionalIndexTriple [docID=" + docID + ", position1="
					+ position1 + ", position2=" + position2 + "]";
		}
		
	}
    
    public List<PositionalIndexTriple> posIntersect(List<PositionalIndexPositions> p1, List<PositionalIndexPositions> p2, int k) {
    	List<PositionalIndexTriple> resultList = new ArrayList<>();
    	
    	// for p1
    	int i = 0;
    	// for p2
    	int j = 0;
    	
    	while (i < p1.size() && j < p2.size()) {
    		if (p1.get(i).getDocID() == p2.get(j).getDocID()) {
    			List<Integer> l = new ArrayList<>();
    			List<Integer> pp1 = p1.get(i).getPositions();
    			List<Integer> pp2 = p2.get(j).getPositions();
    			
    			// for pp1
    			int m = 0;
    			// for pp2
    			int n = 0;
    			
    			while (m < pp1.size()) {
    				while (n < pp2.size()) {
    					if (Math.abs(pp1.get(m) - pp2.get(n)) <= k) {
    						l.add(pp2.get(n));
    					} else if (pp2.get(n) > pp1.get(m)) {
    						break;
    					}
    					
    					++n;
    				}
					while (!l.isEmpty() && Math.abs(l.get(0) - pp1.get(m)) > k) {
						// check this!
						l.remove(l.get(0));
					}
					
					for (int ps : l) {
						resultList.add(new PositionalIndexTriple(p1.get(i).getDocID(), pp1.get(m), ps));
					}
					
					++m;
    				}
    			
    			
    			++i;
    			++j;
    		}
    		else if (p1.get(i).getDocID() < p2.get(j).getDocID()) {
    			++i;
    		} else {
    			++j;
    		}
    	}
    	
    	return resultList;
    }
    
	public void startPosIndexSearch() {
		int k = 1;
		
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
					String[] terms = input.toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement).split(Consts.splitRegex);

					System.out.println(Arrays.toString(terms));
					
					//List<PositionalIndexTriple> resultList = posIntersect(index.get(binarySearch(terms[0])).getPostingsList(), index.get(binarySearch(terms[1])).getPostingsList(), k);
					List<PositionalIndexTriple> resultList = new ArrayList<>();
					for (int i = 0; i < terms.length-1; ++i) {
						System.out.println(terms[i] + ' ' + terms[i+1]);
						resultList = posIntersect(index.get(binarySearch(terms[i])).getPostingsList(), index.get(binarySearch(terms[i+1])).getPostingsList(), k);
						for (PositionalIndexTriple res : resultList) {
							System.out.println(res);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Invalid expression or no result");
			}
		} while (!quit);
	}
}
