package com.ukma.davydenko.indexbuilder.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ukma.davydenko.utils.Consts;
import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.Entry;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;
import com.ukma.davydenko.indexbuilder.entities.MatrixEntry;

public class IndexBuilder {
	public static MyArray<IndexEntry> buildIndex(MyArray<Entry> entries) {
		MyArray<IndexEntry> index = new MyArray<>();
		
		String lastWord = entries.get(0).getTerm();
		MyArray<Integer> postArr = new MyArray<>();
		
		for (int i = 0; i < entries.size(); ++i) {
			String currWord = entries.get(i).getTerm();
			int currDoc = entries.get(i).getDocID();
			
			if (lastWord.equals(currWord)) {
				if (!postArr.contains(currDoc)) {
					postArr.add(currDoc);
				}
			} else {
				// saving last processed word
				index.add(new IndexEntry(lastWord, postArr.size(), postArr));
				
				// for new word
				lastWord = currWord;
				postArr = new MyArray<>();
				postArr.add(currDoc);
			}
		}
		
		index.add(new IndexEntry(lastWord, postArr.size(), postArr));
		
		return index;
	}
	
	public static Map<String, TreeSet<Integer>> buildCollectionIndex(String pathName) {
		Map<String, TreeSet<Integer>> index = new TreeMap<>();
		
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
								if (index.containsKey(word)) {
									TreeSet<Integer> list = index.get(word);
									list.add(docID);
								} else {
									TreeSet<Integer> list = new TreeSet<>();
									list.add(docID);
									index.put(word, list);
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
		
		return index;
	}
	
	public static MyArray<MatrixEntry> buildIncedenceMatrix(MyArray<IndexEntry> index, MyArray<Integer> docIDs) {
		MyArray<MatrixEntry> matrix = new MyArray<>();
		
		for (int i = 0; i < index.size(); ++i) {
			BitSet entries = new BitSet();
			
			int m = 0;
			int n = 0;
			int bitIndex = 0;
			
			while (n < docIDs.size() && m < index.get(i).getPostingsList().size()) {
				if (docIDs.get(n) == index.get(i).getPostingsList().get(m)) {
					entries.set(bitIndex);
					++bitIndex;
					++m;
					++n;
				} else if (docIDs.get(n) < index.get(i).getPostingsList().get(m)) {
					++bitIndex;
					++n;
				}
			}
			
			while (n < docIDs.size()) {
				++bitIndex;
				++n;
			}
			
			matrix.add(new MatrixEntry(index.get(i).getTerm(), entries));
		}
		
		return matrix;
	}
	
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
		
		//System.out.println("Total words processed: " + entries.size());
		
		return entries;
	}
	
	public static void serializeMatrixToFile(MyArray<MatrixEntry> matrix, String fileName) {
		serializer(matrix, fileName);
	}
	
	public static void serializeIndexToFile(MyArray<IndexEntry> index, String fileName) {
		serializer(index, fileName);
	}
	
	public static void serializeIndexToFile(Map<String, TreeSet<Integer>> index, String fileName) {
		serializer(index, fileName);
	}
	
	private static void serializer(Object obj, String fileName) {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			
			out.writeObject(obj);
			
			out.close();
	        fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeIndexToFile(MyArray<IndexEntry> index, String fileName) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
			PrintWriter out = new PrintWriter(br);
			
			for (int i = 0; i < index.size(); ++i) {
				out.write('[' + index.get(i).getTerm() + ", " + index.get(i).getFrequency() + ']');
				out.write("\t");
				for (int j = 0; j < index.get(i).getPostingsList().size(); ++j) {
					out.write(" " + index.get(i).getPostingsList().get(j));
				}
				out.write("\r\n");
			}
			
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	public static void writeIndexToFile(Map<String, TreeSet<Integer>> index, String fileName) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter(fileName));
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
        
	}
	
	@SuppressWarnings("unchecked")
	public static MyArray<IndexEntry> deserializeArr(String fileName) {
		MyArray<IndexEntry> index = null;
		
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(fileName);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        
	        index = (MyArray<IndexEntry>) in.readObject();
	        //System.out.println(index.size());
	        in.close();
	        fileIn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return index;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, TreeSet<Integer>> deserializeColl(String fileName) {
		Map<String, TreeSet<Integer>> index = null;
		
		FileInputStream fileIn;
		try {
			fileIn = new FileInputStream(fileName);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        
	        index = (Map<String, TreeSet<Integer>>) in.readObject();
	        //System.out.println(index.size());
	        in.close();
	        fileIn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return index;
	}
	
	public static MyArray<Integer> getAllDocIDs(String pathName) {
		MyArray<Integer> docIDs = new MyArray<>();
		
		try {
			Files.walk(Paths.get(new File(pathName).getAbsolutePath())).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			    	int docID = Integer.parseInt(filePath.getFileName().toString().replaceFirst("[.][^.]+$", ""));
			    	docIDs.add(docID);
			    }
			});
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Arrays.sort(docIDs.getRawArray());
		
		return docIDs;
	}
}
