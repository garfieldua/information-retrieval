package com.ukma.davydenko.indexbuilder.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

import com.ukma.davydenko.indexbuilder.cluster.ClusterBuilder;
import com.ukma.davydenko.indexbuilder.compression.CompressionIndex;
import com.ukma.davydenko.indexbuilder.compression.CompressionIndexBuilder;
import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.Entry;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;
import com.ukma.davydenko.indexbuilder.entities.MatrixEntry;
import com.ukma.davydenko.indexbuilder.fb2parser.Fb2IndexBuilder;
import com.ukma.davydenko.indexbuilder.fb2parser.Fb2ZonalEntry;
import com.ukma.davydenko.indexbuilder.fb2parser.Fb2ZonalIndexEntry;
import com.ukma.davydenko.indexbuilder.fb2parser.Fb2ZonalIndexSearch;
import com.ukma.davydenko.indexbuilder.logic.IndexBuilder;
import com.ukma.davydenko.indexbuilder.logic.QueryProcessor;
import com.ukma.davydenko.indexbuilder.permuterm.PermutermIndexBuilder;
import com.ukma.davydenko.indexbuilder.permuterm.PermutermIndexPair;
import com.ukma.davydenko.indexbuilder.permuterm.PermutermIndexSearch;
import com.ukma.davydenko.indexbuilder.positional.PositionalEntry;
import com.ukma.davydenko.indexbuilder.positional.PositionalIndexBuilder;
import com.ukma.davydenko.indexbuilder.positional.PositionalIndexEntry;
import com.ukma.davydenko.indexbuilder.positional.PositionalIndexSearch;
import com.ukma.davydenko.indexbuilder.spimi.SpimiIndexBuilder;
import com.ukma.davydenko.indexbuilder.suffix.TrieSearch;
import com.ukma.davydenko.indexbuilder.suffix.TrieVocabulary;
import com.ukma.davydenko.indexbuilder.trigram.TrigramIndexBuilder;
import com.ukma.davydenko.indexbuilder.trigram.TrigramIndexEntry;
import com.ukma.davydenko.indexbuilder.trigram.TrigramIndexPair;
import com.ukma.davydenko.indexbuilder.trigram.TrigramIndexSearch;
import com.ukma.davydenko.indexbuilder.twoword.BiwordIndexEntry;
import com.ukma.davydenko.indexbuilder.twoword.BiwordEntry;
import com.ukma.davydenko.indexbuilder.twoword.BiwordIndexBuilder;
import com.ukma.davydenko.indexbuilder.twoword.BiwordIndexSearch;
import com.ukma.davydenko.indexbuilder.zonal.ZonalEntry;
import com.ukma.davydenko.indexbuilder.zonal.ZonalIndexBuilder;
import com.ukma.davydenko.indexbuilder.zonal.ZonalIndexEntry;
import com.ukma.davydenko.indexbuilder.zonal.ZonalIndexSearch;

public class Main {
	
	private static String folderName = "books";
	private static String epubFolderName = "books_epub";
	private static String fb2FolderName = "books_fb2";
	
	public static void main(String[] args) {
		// FB2 PARSING
		MyArray<Fb2ZonalEntry> fb2ZonalEntries = Fb2IndexBuilder.processEntries(fb2FolderName);
		MyArray<Fb2ZonalIndexEntry> fb2ZonalIndex = Fb2IndexBuilder.buildIndex(fb2ZonalEntries);
		System.out.println(Fb2IndexBuilder.getDocMapping());
		
		Fb2ZonalIndexSearch fb2ZonalSearch = new Fb2ZonalIndexSearch(fb2ZonalIndex);
		fb2ZonalSearch.startZonalIndexSearch();
		
		// CLUSTERING
//		MyArray<Entry> entries = IndexBuilder.processEntries(folderName);
//		MyArray<IndexEntry> index = IndexBuilder.buildIndex(entries);
//		System.out.println(IndexBuilder.getDocMapping());
//		
//		ClusterBuilder clusterBuilder = new ClusterBuilder(index, entries, IndexBuilder.getMaxDocId());
//		
//		HashMap<Integer, List<Integer>> clusters = clusterBuilder.clusterize();
//		for (java.util.Map.Entry<Integer, List<Integer>> entry : clusters.entrySet()) {
//			Integer leadDocID = entry.getKey();
//			List<Integer> followersList = entry.getValue();
//			
//			System.out.println(leadDocID + " : " + followersList);
//		}
		
//		for (int i = 0; i < index.size(); ++i) {
//			System.out.println(index.get(i).getTerm() + ':' + index.get(i).getFrequency());
//		}
		
		// ZONAL INDEX
//		MyArray<ZonalEntry> zonalEntries = ZonalIndexBuilder.processEntries(epubFolderName);
//		MyArray<ZonalIndexEntry> zonalIndex = ZonalIndexBuilder.buildIndex(zonalEntries);
//		System.out.println(ZonalIndexBuilder.getDocMapping());
//		
//		ZonalIndexSearch zonalSearch = new ZonalIndexSearch(zonalIndex);
//		zonalSearch.startZonalIndexSearch();
		
		// COMPRESSION
//		MyArray<Entry> entries = CompressionIndexBuilder.processEntries(folderName);
//		CompressionIndex index = null;
//		
//		try {
//			index = CompressionIndexBuilder.buildIndex(entries);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		
//		// SPIMI
//		long startTime = System.currentTimeMillis();
//
//		SpimiIndexBuilder spIndex = new SpimiIndexBuilder(folderName, "index_blocks");
//		spIndex.buildSpimiIndex();
//		//spIndex.printDocMapping();
//		try {
//			spIndex.mergeBlocksToFile();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		long stopTime = System.currentTimeMillis();
//	    long elapsedTime = stopTime - startTime;
//	    System.out.println("SPIMI index building took " + elapsedTime + " ms");
		
		// ARRAY TO TXT PROCESSING
//		MyArray<Entry> entries = IndexBuilder.processEntries(folderName);
//		MyArray<IndexEntry> index = IndexBuilder.buildIndex(entries);
	    
		// PERMUTERMS
//		MyArray<PermutermIndexPair> premutermPairs = PermutermIndexBuilder.getPremutermPairs(index);
//		
//		// building premuterm trie
//		TrieVocabulary premutermTrie = new TrieVocabulary();
//		for (int i = 0; i < index.size(); ++i) {
//			MyArray<String> permuterm = PermutermIndexBuilder.getPermuterm(index.get(i).getTerm());
//			
//			for (int j = 0; j < permuterm.size(); ++j) {
//				premutermTrie.add(permuterm.get(j));
//			}
//		}
//		
//		PermutermIndexSearch permutermSearch = new PermutermIndexSearch(premutermTrie, premutermPairs, index, folderName);
//		permutermSearch.startPermutermSearch();
//		
//		// SUFFIX TRIES
//		// building suffix trie
//		TrieVocabulary directTrie = new TrieVocabulary();
//		TrieVocabulary reverseTrie = new TrieVocabulary();
//		for (int i = 0; i < index.size(); ++i) {
//			String word = index.get(i).getTerm();
//			if (word.length() >= 1) {
//				directTrie.add(word);
//				String reverse = new StringBuilder(word).reverse().toString();
//				reverseTrie.add(reverse);
//			}
//		}
//
//		TrieSearch trieSearch = new TrieSearch(directTrie, reverseTrie, index, folderName);
//		trieSearch.startTrieSearch();
//		
//		// TRIGRAMS
//		MyArray<TrigramIndexPair> trigramPairs = TrigramIndexBuilder.getTrigramPairs(index);
//		MyArray<TrigramIndexEntry> trigramIndex = TrigramIndexBuilder.buildIndex(trigramPairs);
//		
//		TrigramIndexSearch trigramSearch = new TrigramIndexSearch(trigramIndex, index, folderName);
//		trigramSearch.startTrigramIndexSearch();
		
		
//		// OUTPUT DICTIONARY TO TXT FILE
//		IndexBuilder.writeIndexToFile(index, "index_arr.txt");
//		long stopTime = System.currentTimeMillis();
//	    long elapsedTime = stopTime - startTime;
//	    System.out.println("Array to txt processing " + elapsedTime + " ms");
//		
//		// ARRAY TO SERIALIZATION
//	    startTime = System.currentTimeMillis();
//	    entries = IndexBuilder.processEntries(folderName);
//	    index = IndexBuilder.buildIndex(entries);
//
//		IndexBuilder.serializeIndexToFile(index, "index_arr.ser");
//	    stopTime = System.currentTimeMillis();
//		elapsedTime = stopTime - startTime;
//		System.out.println("Array to serialization processing " + elapsedTime + " ms");
//		//IndexBuilder.deserializeArr("index_arr.ser");
//
//		// COLLECTION TO TXT PROCESSING
//	    startTime = System.currentTimeMillis();
//		Map<String, TreeSet<Integer>> indexCol = IndexBuilder.buildCollectionIndex(folderName);
//		IndexBuilder.writeIndexToFile(indexCol, "index_coll.txt");
//		stopTime = System.currentTimeMillis();
//		elapsedTime = stopTime - startTime;
//		System.out.println("Collection to txt processing " + elapsedTime + " ms");
//		
//		// COLLECTION TO SERIALIZATION
//		startTime = System.currentTimeMillis();
//		indexCol = IndexBuilder.buildCollectionIndex(folderName);
//		IndexBuilder.serializeIndexToFile(indexCol, "index_coll.ser");
//		stopTime = System.currentTimeMillis();
//		elapsedTime = stopTime - startTime;
//		System.out.println("Collection to serialization processing " + elapsedTime + " ms");
//		//IndexBuilder.deserializeColl("index_coll.ser");
//		
//		// TERM-DOCUMENT INCEDENCE MATRIX
//		MyArray<Integer> docids = IndexBuilder.getAllDocIDs(folderName);
//		MyArray<MatrixEntry> matrix = IndexBuilder.buildIncedenceMatrix(index, docids);
//		IndexBuilder.serializeMatrixToFile(matrix, "matrix_arr.ser");
		
		/////////////////////////////////////////////////////////////
//		QueryProcessor qp = new QueryProcessor(index, folderName);
//		qp.startQueryProcessor();
		
		////////////////////// TWO WORD INDEX ///////////////////////
//		List<BiwordEntry> twEntries = BiwordIndexBuilder.processEntries(folderName);
//		List<BiwordIndexEntry> twIndex = BiwordIndexBuilder.buildIndex(twEntries);
//
//		BiwordIndexSearch twSearch = new BiwordIndexSearch(twIndex);
//		twSearch.startTwoIndexSearch();
		
		///////////////////// POSITIONAL INDEX //////////////////////
//		List<PositionalEntry> posEntries = PositionalIndexBuilder.processEntries(folderName);
//		List<PositionalIndexEntry> posIndex = PositionalIndexBuilder.buildIndex(posEntries);
		//PositionalIndexBuilder.serialize(posIndex, "posindex.ser");
		
//		PositionalIndexSearch posSearch = new PositionalIndexSearch(posIndex);
//		posSearch.startPosIndexSearch();
	}
}
