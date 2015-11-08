package com.ukma.davydenko.indexbuilder.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.Entry;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;

public class ClusterBuilder {
	private MyArray<IndexEntry> index;
	private MyArray<Entry> entries;
	private int totalIds;
	
	private int[][] freq;
	private double[][] normFreq;
	
	public ClusterBuilder(MyArray<IndexEntry> index, MyArray<Entry> entries, int totalIds) {
		this.index = index;
		this.entries = entries;
		this.totalIds = totalIds;
	}

	private int binarySearch(String word) {
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
	
    private void normalizeTable() {
    	normFreq = new double[totalIds+1][index.size()];
    	double[] normLength = new double[totalIds+1];
    	
		for (int i = 0; i < freq.length; ++i) {
			long docScore = 0;
			for (int j = 0; j < freq[i].length; ++j) {
				docScore += freq[i][j] * freq[i][j];
			}
			normLength[i] = Math.sqrt(docScore);
			//System.out.println(docScore);
		}
    	
		for (int i = 0; i < freq.length; ++i) {
			for (int j = 0; j < freq[i].length; ++j) {
				normFreq[i][j] = freq[i][j]/normLength[i];
			}
		}
    }
    
    private void buildTable() {
    	//freq = new int[index.size()][totalIds+1];
    	freq = new int[totalIds+1][index.size()];
    	
    	String lastWord = entries.get(0).getTerm();
    	int lastDoc = entries.get(0).getDocID();
    	int counter = 0;
    	
		for (int i = 0; i < entries.size(); ++i) {
			String currWord = entries.get(i).getTerm();
			int currDoc = entries.get(i).getDocID();
			
			if (lastWord.equals(currWord) && currDoc == lastDoc) {
				++counter;
			} else {
				// searching word position and inserting into table
				freq[lastDoc][binarySearch(lastWord)] = counter;
				
				// for new word
				lastWord = currWord;
				lastDoc = currDoc;
				counter = 1;
			}
		}
		
		freq[lastDoc][binarySearch(lastWord)] = counter;
    }
    
    private double computeDotProduct(double[] vec1, double[] vec2) {
    	double res = 0;
    	
    	for (int i = 0; i < vec1.length; ++i) {
    		res += vec1[i] * vec2[i];
    	}
    	
    	return res;
    }
    
	public HashMap<Integer, List<Integer>> clusterize() {
		buildTable();
		normalizeTable();
		
		HashMap<Integer, List<Integer>> res = new HashMap<Integer, List<Integer>>();
		
		List<Integer> originalList = new ArrayList<Integer>();
		List<Integer> shuffledList = new ArrayList<Integer>();
		List<Integer> pickedList = new ArrayList<Integer>();
		
		for (int i = 0; i <= totalIds; ++i) {
			originalList.add(new Integer(i));
			shuffledList.add(new Integer(i));
		}
		
		Collections.shuffle(shuffledList);
		
		//System.out.println();
		for (int i = 0; i < Math.sqrt(totalIds); ++i) {
			pickedList.add(shuffledList.get(i));
			//System.out.println(shuffledList.get(i));
		}
		
		// this is leading list
		originalList.removeAll(pickedList);
		//System.out.println();
		
		// going thru non-pickedList, searching for best leader
		for (Integer docID:originalList) {
			//System.out.println(docID);
			
			double biggestSim = 0;
			int followerId = 0;
			for (Integer picked:pickedList) {
				double diff = computeDotProduct(normFreq[docID], normFreq[picked]);
				if (diff > biggestSim) {
					biggestSim = diff;
					followerId = picked;
				}
				//System.out.println(computeDotProduct(normFreq[docID], normFreq[picked] )+":"+picked);
			}
			//System.out.println(biggestSim + " : " + followerId + " : " + docID);
			
			if (res.containsKey(followerId)) {
				List<Integer> list = res.get(followerId);
				list.add(docID);
			} else {
				List<Integer> list = new ArrayList<>();
				list.add(docID);
				res.put(followerId, list);
			}
		}
		
		// there is possible to have leaders without followers
		for (Integer picked:pickedList) {
			if (!res.containsKey(picked)) {
				List<Integer> list = new ArrayList<>();
				res.put(picked, list);
			}
		}
		
		return res;
	}
}
