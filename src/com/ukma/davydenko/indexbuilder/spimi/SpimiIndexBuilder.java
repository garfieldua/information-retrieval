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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;
import com.ukma.davydenko.utils.Consts;
import com.ukma.davydenko.utils.Utils;

public class SpimiIndexBuilder {
	private static int MAX_TERMS_NUMBER = 10000000;
	
	private String sourcePath;
	private String blockPath;
	
	private Map<String, TreeSet<Integer>> index = new TreeMap<String, TreeSet<Integer>>();
	private Map<String, Integer> docMapping = new HashMap<String, Integer>();
	
	public Map<String, Integer> getDocMapping() {
		return docMapping;
	}

	public void setDocMapping(Map<String, Integer> docMapping) {
		this.docMapping = docMapping;
	}
	
	public void printDocPrinting() {
		for (java.util.Map.Entry<String, Integer> entry : docMapping.entrySet()) {
			String docName = entry.getKey();
			Integer docId = entry.getValue();
			
			System.out.println(docName + " : " + docId);
		}
	}

	private int counter = 0;
	private int serial = 0;
	private int id = 1;
	
	public SpimiIndexBuilder(String sourcePath, String blockPath) {
		this.sourcePath = sourcePath;
		this.blockPath = blockPath;
	}

	public void writeBlockToFile() {
		try {
			String fileName = Integer.toString(serial + 1000).substring(1) + ".dictionary";
			File dir = new File (blockPath);
			File actualFile = new File (dir, fileName);
			
			BufferedWriter br = new BufferedWriter(new FileWriter(actualFile));
			PrintWriter out = new PrintWriter(br);
			
			for (java.util.Map.Entry<String, TreeSet<Integer>> entry : index.entrySet()) {
				String term = entry.getKey();
				TreeSet<Integer> postingsList = entry.getValue();
				
				// when parsing results, need to split by space for term and postinglist
				// then, by comma to retrieve docID
				out.write(term + " ");

				for (Integer docID : postingsList) {
					out.write(docID + ",");
				}
				
				out.write("\r\n");
			}
			
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		++serial;
		index = new TreeMap<String, TreeSet<Integer>>();
		
		// suggesting Java GC to free memory now
		System.gc();
	}
	
	public void mergeBlocksToFile() throws Exception {
		List<BufferedReader> bufferedReaders = new ArrayList<BufferedReader>();
		
		Files.walk(Paths.get(new File(blockPath).getAbsolutePath())).forEach(filePath -> {	
			
		    if (Files.isRegularFile(filePath)) {
		    	try {
					bufferedReaders.add(new BufferedReader(new FileReader(filePath.toString())));
				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		});
		
		// array of index entries of current processed line of buffer
		MyArray<IndexEntry> tmpEntries = new MyArray<>();
        for (int i = 0; i < bufferedReaders.size(); i++) {
        	tmpEntries.add(new IndexEntry(bufferedReaders.get(i).readLine()));
        }
        
        BufferedWriter br = new BufferedWriter(new FileWriter("result.dictionary"));
		PrintWriter out = new PrintWriter(br);
		
		while (bufferedReaders.size() > 0) {
            MyArray<Integer> smallestTermBlockIDs = new MyArray<>();

            smallestTermBlockIDs.add(0);
            String smallestWord = tmpEntries.get(0).getTerm();

            for (int i = 1; i < tmpEntries.size(); i++) {
                String currWord = tmpEntries.get(i).getTerm();
                if (currWord.compareTo(smallestWord) < 0) {
                    smallestTermBlockIDs = new MyArray<>();
                    smallestTermBlockIDs.add(i);
                    smallestWord = currWord;
                } else if (currWord.compareTo(smallestWord) == 0) {
                    smallestTermBlockIDs.add(i);
                }
            }

            MyArray<Integer> mergedPostingList = new MyArray<>();
            out.write(tmpEntries.get(smallestTermBlockIDs.get(0)).getTerm());

            for (int i = smallestTermBlockIDs.size() - 1; i >= 0; i--) {
                int blockID = smallestTermBlockIDs.get(i);

                // merging postings lists
                mergedPostingList = Utils.union(mergedPostingList, tmpEntries.get(blockID).getPostingsList());

                // preparing next term
                // if no next term (end of file - remove buffer
                String nextIndexEntry = bufferedReaders.get(blockID).readLine();
                if (nextIndexEntry == null) {
                	tmpEntries.remove(blockID);
                	bufferedReaders.get(blockID).close();
                	bufferedReaders.remove(blockID);
                } else {

                	IndexEntry termListPair = new IndexEntry(nextIndexEntry);
                    tmpEntries.set(blockID, termListPair);
                }
            }

            out.write(mergedPostingList.toString() + "\n");
        }
		out.close();
	}
	
	public void buildSpimiIndex() {
		
		try {
			Files.walk(Paths.get(new File(sourcePath).getAbsolutePath())).forEach(filePath -> {
				
				
			    if (Files.isRegularFile(filePath)) {
			    	docMapping.put(filePath.getFileName().toString(), id++);
			    	int docID = id;//Integer.parseInt(filePath.getFileName().toString().replaceFirst("[.][^.]+$", ""));
			    	
			    	try {
			    		BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
			    		String currentLine;
			    		
			    		while ((currentLine = br.readLine()) != null) {
							
							String[] words = currentLine.toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement).split(Consts.splitRegex);							
							
							for (String word: words) {
								if (word.length() >= 1) {
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
							
							
						}
			    		
			    		br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			    }
			});
			
			writeBlockToFile();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
