package com.ukma.davydenko.indexbuilder.spimi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ukma.davydenko.utils.Consts;

public class SpimiIndexBuilder {
	private static int MAX_TERMS_NUMBER = 100000;
	private String sourcePath;
	private String blockPath;
	
	private int counter = 0;
	private int serial = 0;
	
	Map<String, TreeSet<Integer>> index = new TreeMap<String, TreeSet<Integer>>();
	
	public SpimiIndexBuilder(String sourcePath, String blockPath) {
		this.sourcePath = sourcePath;
		this.blockPath = blockPath;
	}

	public void writeBlockToFile() {
		try {
			String fileName = "" + serial + ".block";
			File dir = new File (blockPath);
			File actualFile = new File (dir, fileName);
			
			BufferedWriter br = new BufferedWriter(new FileWriter(actualFile));
			PrintWriter out = new PrintWriter(br);
			
			for (java.util.Map.Entry<String, TreeSet<Integer>> entry : index.entrySet()) {
				String term = entry.getKey();
				TreeSet<Integer> postingsList = entry.getValue();
				
				out.write('[' + term + ", " + postingsList.size() + ']');
				out.write("\t");
				
				for (Integer docID : postingsList) {
					out.write(" " + docID);
				}
				
				out.write("\r\n");
			}
			
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		++serial;
		index = new TreeMap<String, TreeSet<Integer>>();
	}
	
	public void buildSpimiIndex() {
		
		try {
			
			Files.walk(Paths.get(new File(sourcePath).getAbsolutePath())).forEach(filePath -> {
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
									writeBlockToFile();
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
