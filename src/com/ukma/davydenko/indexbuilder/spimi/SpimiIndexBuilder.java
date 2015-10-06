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
		
		MyArray<IndexEntry> tmpEntries = new MyArray<>();
        for (int i = 0; i < bufferedReaders.size(); i++) {
        	tmpEntries.add(new IndexEntry(bufferedReaders.get(i).readLine()));
        }
        
        BufferedWriter br = new BufferedWriter(new FileWriter("result.dictionary"));
		PrintWriter out = new PrintWriter(br);
		
		while (bufferedReaders.size() > 0) {
            /*
             *  To hold index or indices of block(s) where the next lexicographically smallest term is found
             */
            ArrayList<Integer> indicesOfBlocksWithSmallestNextTerm = new ArrayList<Integer>(bufferedReaders.size());

            indicesOfBlocksWithSmallestNextTerm.add(0);
            String smallestWord = tmpEntries.get(0).getTerm();
            /*
             *  Find the next smallest term and the associated posting lists
             */
            for (int i = 1; i < tmpEntries.size(); i++) {
                String currentWord = tmpEntries.get(i).getTerm();
                if (currentWord.compareTo(smallestWord) < 0) {
                    indicesOfBlocksWithSmallestNextTerm.clear();
                    indicesOfBlocksWithSmallestNextTerm.add(i);
                    smallestWord = currentWord;
                } else if (currentWord.compareTo(smallestWord) == 0) {
                    indicesOfBlocksWithSmallestNextTerm.add(i);
                }
            }

            /*
             *  Merge the posting lists from the different blocks for the current term and read the next line from the blocks
             *  that were used to retrieve the current term
             */
            MyArray<Integer> mergedPostingList = new MyArray<>();
            out.write(tmpEntries.get(indicesOfBlocksWithSmallestNextTerm.get(0)).getTerm());

            // dealing with posting list
            for (int i = indicesOfBlocksWithSmallestNextTerm.size() - 1; i >= 0; i--) {
                int indexOfBlockToProcess = indicesOfBlocksWithSmallestNextTerm.get(i);

                /*
                 * 'entry-safe' method merge is used instead of binary union.
                 * See PostingsList.java for more details.
                 */
                mergedPostingList = Utils.union(mergedPostingList, tmpEntries.get(indexOfBlockToProcess).getPostingsList());

                String szTermListPair = bufferedReaders.get(indexOfBlockToProcess).readLine();
                if (szTermListPair == null) {
                	tmpEntries.remove(indexOfBlockToProcess);
                	bufferedReaders.get(indexOfBlockToProcess).close();
                	bufferedReaders.remove(indexOfBlockToProcess);
                } else {

                	IndexEntry termListPair = new IndexEntry(szTermListPair);
                    tmpEntries.set(indexOfBlockToProcess, termListPair);
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
