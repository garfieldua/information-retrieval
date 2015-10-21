package com.ukma.davydenko.indexbuilder.compression;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.Entry;
import com.ukma.davydenko.utils.Consts;

public class CompressionIndexBuilder {
	public static MyArray<Entry> processEntries(String pathName) {
		MyArray<Entry> entries = new MyArray<>();
		
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
								entries.add(new Entry(word, docID));
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
		
		Arrays.sort(entries.getRawArray(), 0, entries.size());
		
		return entries;
	}
	
	public static CompressionIndex buildIndex(MyArray<Entry> entries) throws IOException {
		CompressionIndex index = new CompressionIndex();
		StringBuilder vocabularyAccumulator = new StringBuilder();
		
		String lastWord = entries.get(0).getTerm();
		MyArray<Integer> postArr = new MyArray<>();
		
		int curTermPos = 0;
		
		for (int i = 0; i < entries.size(); ++i) {
			String currWord = entries.get(i).getTerm();
			int currDoc = entries.get(i).getDocID();
			
			if (lastWord.equals(currWord)) {
				if (!postArr.contains(currDoc)) {
					postArr.add(currDoc);
				}
			} else {
				// saving last processed word
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				
				int[] tempArr = new int[postArr.size()];
				for (int j = 0; j < postArr.size(); ++j) {
					tempArr[j] = postArr.get(j);
				}
				
				IndexCompression.VBEncode(tempArr, postArr.size(), outputStream);
				
				index.getIndex().add(new CompressionIndexEntry(postArr.size(), curTermPos, outputStream.toByteArray()));
				vocabularyAccumulator.append(lastWord);
				
				// for new word
				curTermPos += lastWord.length();
				lastWord = currWord;
				postArr = new MyArray<>();
				postArr.add(currDoc);
			}
		}
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		int[] tempArr = new int[postArr.size()];
		for (int j = 0; j < postArr.size(); ++j) {
			tempArr[j] = postArr.get(j);
		}
		
		IndexCompression.VBEncode(tempArr, postArr.size(), outputStream);
		
		index.getIndex().add(new CompressionIndexEntry(postArr.size(), curTermPos, outputStream.toByteArray()));
		vocabularyAccumulator.append(lastWord);
		
		index.setVocabulary(vocabularyAccumulator.toString());
		
		return index;
	}
}
