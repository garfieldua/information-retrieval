package com.ukma.davydenko.indexbuilder.main;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.Entry;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;
import com.ukma.davydenko.indexbuilder.entities.MatrixEntry;
import com.ukma.davydenko.indexbuilder.logic.IndexBuilder;
import com.ukma.davydenko.indexbuilder.logic.QueryProcessor;
import com.ukma.davydenko.indexbuilder.positional.PositionalEntry;
import com.ukma.davydenko.indexbuilder.positional.PositionalIndexBuilder;
import com.ukma.davydenko.indexbuilder.positional.PositionalIndexEntry;
import com.ukma.davydenko.indexbuilder.twoword.TwoWordIndexEntry;
import com.ukma.davydenko.indexbuilder.twoword.TwoWordEntry;
import com.ukma.davydenko.indexbuilder.twoword.TwoWordIndexBuilder;
import com.ukma.davydenko.indexbuilder.twoword.TwoWordIndexSearch;

public class Main {
	
	private static String folderName = "books";
	
	public static void main(String[] args) {
		
		// ARRAY TO TXT PROCESSING
//		long startTime = System.currentTimeMillis();
//		MyArray<Entry> entries = IndexBuilder.processEntries(folderName);
//		MyArray<IndexEntry> index = IndexBuilder.buildIndex(entries);
//		
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
		
		
		////////////////////// TWO WORD INDEX ///////////////////////
		List<TwoWordEntry> twEntries = TwoWordIndexBuilder.processEntries(folderName);
		//System.out.println(twEntries);
		
		List<TwoWordIndexEntry> twIndex = TwoWordIndexBuilder.buildIndex(twEntries);
//		for (TwoWordIndexEntry twIndexEntry : twIndex) {
//			System.out.println(twIndexEntry);
//		}
		
		TwoWordIndexSearch twSearch = new TwoWordIndexSearch(twIndex, folderName);
		twSearch.startTwoIndexSearch();
		// works great!
		//System.out.println();
		//System.out.println(twSearch.binarySearch("you", "you"));
		
		///////////////////// POSITIONAL INDEX //////////////////////
		List<PositionalEntry> posEntries = PositionalIndexBuilder.processEntries(folderName);
//		for (PositionalEntry posEntry : posEntries) {
//			System.out.println(posEntry);
//		}
		
		List<PositionalIndexEntry> posIndex = PositionalIndexBuilder.buildIndex(posEntries);
//		for (PositionalIndexEntry posIndexEntry : posIndex) {
//			System.out.println(posIndexEntry);
//		}
		
		/////////////////////////////////////////////////////////////
//		QueryProcessor qp = new QueryProcessor(index, folderName);
//		qp.startQueryProcessor();
		
		// testing github from java
	}
}
