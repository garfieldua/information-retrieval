package com.ukma.davydenko.indexbuilder.zonal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.utils.Consts;

import ebook.EBook;
import ebook.parser.InstantParser;
import ebook.parser.Parser;

public class ZonalIndexBuilder {
	
	private static Map<String, Integer> docMapping = new HashMap<String, Integer>();
	private static int DOC_ID = 0;
	
	public Map<String, Integer> getDocMapping() {
		return docMapping;
	}

	public static MyArray<ZonalIndexEntry> buildIndex (MyArray<ZonalEntry> entries) {
		
		MyArray<ZonalIndexEntry> index = new MyArray<>();
		
		ZonalIndexEntry indexEntry = new ZonalIndexEntry();
		ZonalIndexElem indexElem = new ZonalIndexElem();
		
		String currTerm = entries.get(0).getTerm();
		int currDocId = entries.get(0).getDocID();
		
		for (int i = 0 ; i < entries.size(); ++i) {
			if (entries.get(i).getTerm().equals(currTerm)) {
				if (entries.get(i).getDocID() == currDocId) {
					// adding new zone to ZonalIndexElem
					if (!indexElem.getZones().contains(entries.get(i).getZone()))
					indexElem.getZones().add(entries.get(i).getZone());
				} else {
					// saving last processed
					indexElem.setDocID(currDocId);
					indexEntry.getZonalPostingsList().add(indexElem);
				
					// creating new ZonalIndexElem
					indexElem = new ZonalIndexElem();
					currDocId = entries.get(i).getDocID();
					indexElem.getZones().add(entries.get(i).getZone());
				}
			} else {
				indexElem.setDocID(currDocId);
				indexEntry.getZonalPostingsList().add(indexElem);
				
				indexEntry.setTerm(currTerm);
				index.add(indexEntry);

				// creating new ZonalIndexEntry
				indexElem = new ZonalIndexElem();
				indexElem.getZones().add(entries.get(i).getZone());
				
				currTerm = entries.get(i).getTerm();
				currDocId = entries.get(i).getDocID();
				indexEntry = new ZonalIndexEntry();
			}
		}
		
		indexElem.setDocID(currDocId);
		indexEntry.getZonalPostingsList().add(indexElem);
		
		indexEntry.setTerm(currTerm);
		index.add(indexEntry);

		
		return index;
	}
	
	public static MyArray<ZonalEntry> processEntries(String pathName) {
		MyArray<ZonalEntry> entries = new MyArray<>();
		Parser parser = new InstantParser();
		
		try {
			Files.walk(Paths.get(new File(pathName).getAbsolutePath())).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					docMapping.put(filePath.getFileName().toString(), DOC_ID++);
					
					EBook ebook = parser.parse(filePath.toString());
					if (ebook.isOk) {
						ArrayList<ebook.Person> authors = ebook.authors;
						
						for (ebook.Person author: authors) {
							entries.add(new ZonalEntry(author.firstName.toLowerCase(), DOC_ID, ZonalEnum.AUTHOR));
							entries.add(new ZonalEntry(author.lastName.toLowerCase(), DOC_ID, ZonalEnum.AUTHOR));
						}
						
						String[] titleWords = ebook.title.toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement).split("[^\\p{L}\\p{Nd}]+");
						
						
						for (String word: titleWords) {
							entries.add(new ZonalEntry(word, DOC_ID, ZonalEnum.TITLE));
						}
					}
				}
			});
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Arrays.sort(entries.getRawArray(), 0, entries.size());
		
		return entries;
	}
}
