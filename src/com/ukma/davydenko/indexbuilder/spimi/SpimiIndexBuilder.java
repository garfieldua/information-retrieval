package com.ukma.davydenko.indexbuilder.spimi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ukma.davydenko.utils.Consts;

public class SpimiIndexBuilder {
	private static int MAX_TERMS_NUMBER = 10000;
	private String pathName;
	private int counter = 0;
	Map<String, TreeSet<Integer>> index = new TreeMap<String, TreeSet<Integer>>();
	
	public SpimiIndexBuilder(String pathName) {
		this.pathName = pathName;
	}

	public void buildSpimiIndex() {
		
		try {
			
			Files.walk(Paths.get(new File(pathName).getAbsolutePath())).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			    	int docID = Integer.parseInt(filePath.getFileName().toString().replaceFirst("[.][^.]+$", ""));
			    	
			    	try {
			    		BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
			    		String currentLine;
			    		
			    		while ((currentLine = br.readLine()) != null) {
							
							String[] words = currentLine.toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement).split(Consts.splitRegex);							
							
							for (String word: words) {
								if (counter > MAX_TERMS_NUMBER) {
									// flush to disk
									System.out.println("flushing...");
									counter = 0;
								}else {
									if (index.containsKey(word)) {
										TreeSet<Integer> list = index.get(word);
										list.add(docID);
										++counter;
									} else {
										TreeSet<Integer> list = new TreeSet<>();
										list.add(docID);
										index.put(word, list);
										++counter;
									}
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
	}
}
