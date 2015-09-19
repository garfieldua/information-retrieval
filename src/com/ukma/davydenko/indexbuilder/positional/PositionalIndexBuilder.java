package com.ukma.davydenko.indexbuilder.positional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PositionalIndexBuilder {
	static String splitRegex = "[^a-zA-Z]+";
	
	public static List<PositionalEntry> processEntries(String pathName) {
		List<PositionalEntry> entries = new ArrayList<>();
		
		try {
			Files.walk(Paths.get(new File(pathName).getAbsolutePath())).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			    	int docID = Integer.parseInt(filePath.getFileName().toString().replaceFirst("[.][^.]+$", ""));
			    	// instantiate current word position to 0 for each document
			    	int currPosition = 0;
			    	
			    	try {
			    		BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
			    		String currentLine;
			    		String prevWord = null;
			    		
			    		while ((currentLine = br.readLine()) != null) {
							String[] words = currentLine.toLowerCase().replaceAll("\\p{Punct}", "").split(splitRegex);							
							
							for (String word: words) {
								++currPosition;
								entries.add(new PositionalEntry(word, docID, currPosition));
							}
						}
			    		
			    		br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			    }
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Collections.sort(entries);
		
		
		return entries;
	}
	
	public static List<PositionalIndexEntry> buildIndex(List<PositionalEntry> entries) {
		List<PositionalIndexEntry> index = new ArrayList<>();
		
		//Map<Integer, ArrayList<Integer>> postingsList = new TreeMap<Integer, ArrayList<Integer>>();
		String lastTerm = entries.get(0).getTerm();
		int lastDocID = entries.get(0).getDocID();
		
		List<Integer> positions = new ArrayList<>();
		List<PositionalIndexPositions> postingsList = new ArrayList<>();
		
		for (PositionalEntry posEntry : entries) {
			String currTerm = posEntry.getTerm();
			int currDoc = posEntry.getDocID();
			int position = posEntry.getPosition();
			
			if (posEntry.getTerm().equals(lastTerm) && lastDocID == currDoc) {
				positions.add(position);
			} else if (posEntry.getTerm().equals(lastTerm) && lastDocID != currDoc) {
				// saving last processed docID
				postingsList.add(new PositionalIndexPositions(lastDocID, positions));
				
				// for new record
				lastDocID = currDoc;
				
				positions = new ArrayList<>();
				positions.add(position);
			} else {
				// saving last processed docID
				postingsList.add(new PositionalIndexPositions(lastDocID, positions));
				// saving last processed term
				index.add(new PositionalIndexEntry(lastTerm, postingsList.size(), postingsList));
				
				// for new record
				lastTerm = currTerm;
				lastDocID = currDoc;
				
				positions = new ArrayList<>();
				positions.add(position);
				
				postingsList = new ArrayList<>();
				// ?????
			}
		}
		
		postingsList.add(new PositionalIndexPositions(lastDocID, positions));
		index.add(new PositionalIndexEntry(lastTerm, postingsList.size(), postingsList));
		
		return index;
	}
}
