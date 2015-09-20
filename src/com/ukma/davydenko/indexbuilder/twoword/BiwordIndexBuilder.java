package com.ukma.davydenko.indexbuilder.twoword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BiwordIndexBuilder {
	static String splitRegex = "[^a-zA-Z]+";
	
	public static List<BiwordEntry> processEntries(String pathName) {
		List<BiwordEntry> entries = new ArrayList<>();
		
		try {
			Files.walk(Paths.get(new File(pathName).getAbsolutePath())).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			    	int docID = Integer.parseInt(filePath.getFileName().toString().replaceFirst("[.][^.]+$", ""));
			    	
			    	try {
			    		BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
			    		String currentLine;
			    		String prevWord = null;
			    		
			    		while ((currentLine = br.readLine()) != null) {
							String[] words = currentLine.toLowerCase().replaceAll("\\p{Punct}", "").split(splitRegex);							
							
							for (int i = 0; i < words.length; ++i) {
								if (i == 0) {
									prevWord = words[i];
								} else {
									entries.add(new BiwordEntry(prevWord, words[i], docID));
									prevWord = words[i];
								}
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
	
	public static List<BiwordIndexEntry> buildIndex(List<BiwordEntry> entries) {
		List<BiwordIndexEntry> index = new ArrayList<>();

		String lastTerm1 = entries.get(0).getTerm1();
		String lastTerm2 = entries.get(0).getTerm2();
		
		// need to create new post arr if lastTerm1 or lastTerm2 doesn't equal 
		List<Integer> postArr = new ArrayList<>();
		
		for (BiwordEntry twEntry : entries) {
			String currTerm1 = twEntry.getTerm1();
			String currTerm2 = twEntry.getTerm2();
			int currDoc = twEntry.getDocID();
			
			if (lastTerm1.equals(currTerm1) && lastTerm2.equals(currTerm2)) {
				if (!postArr.contains(currDoc)) {
					postArr.add(currDoc);
				}
			} else {
				// saving last processed word
				index.add(new BiwordIndexEntry(lastTerm1, lastTerm2, postArr.size(), postArr));
				
				// for new record
				lastTerm1 = currTerm1;
				lastTerm2 = currTerm2;
				
				postArr = new ArrayList<>();
				postArr.add(currDoc);
			}
			
		}
		
		index.add(new BiwordIndexEntry(lastTerm1, lastTerm2, postArr.size(), postArr));
		
		return index;
	}
}
