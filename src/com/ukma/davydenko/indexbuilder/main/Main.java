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
import com.ukma.davydenko.indexbuilder.positional.PositionalIndexSearch;
import com.ukma.davydenko.indexbuilder.premuterm.PremutermIndexBuilder;
import com.ukma.davydenko.indexbuilder.premuterm.PremutermIndexPair;
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

public class Main {
	
	private static String folderName = "books";
	
	public static void main(String[] args) {
		
		MyArray<String> permuterm = PremutermIndexBuilder.getPermuterm("books");


		System.out.println((char)Character.getNumericValue('a'));
		// ARRAY TO TXT PROCESSING
//		long startTime = System.currentTimeMillis();
		MyArray<Entry> entries = IndexBuilder.processEntries(folderName);
		MyArray<IndexEntry> index = IndexBuilder.buildIndex(entries);
		
//		CompactSuffixTree tree = new CompactSuffixTree(new SimpleSuffixTree("bananas"));
//		String properties = "rankdir=LR; node[shape=box fillcolor=gray95 style=filled]\n";
//		System.out.println("digraph {\n" + properties + tree.getRoot() + "}");
		
		MyArray<PremutermIndexPair> premutermPairs = PremutermIndexBuilder.getPremutermPairs(index);
		
		MyArray<TrigramIndexPair> trigramPairs = TrigramIndexBuilder.getTrigramPairs(index);
		MyArray<TrigramIndexEntry> trigramIndex = TrigramIndexBuilder.buildIndex(trigramPairs);
		
		// building suffix trie
		TrieVocabulary directTrie = new TrieVocabulary();
		TrieVocabulary reverseTrie = new TrieVocabulary();
		for (int i = 0; i < index.size(); ++i) {
			String word = index.get(i).getTerm();
			if (word.length() >= 1) {
				directTrie.add(word);
				String reverse = new StringBuilder(word).reverse().toString();
				reverseTrie.add(reverse);
			}
		}
		//trie.print(trie.getNode("worship"), "worship");
		//System.out.println(trie.isPrefix("worship"));
		
//		MyArray<String> words = directTrie.getAllSuffixes("worship");
//		for (int i = 0; i < words.size(); ++i) {
//			System.out.println(words.get(i));
//		}
//		
//		words = reverseTrie.getAllSuffixes("lluf");
//		for (int i = 0; i < words.size(); ++i) {
//			System.out.println(words.get(i));
//		}
		
		TrieSearch trieSearch = new TrieSearch(directTrie, reverseTrie, index, folderName);
		trieSearch.startTrieSearch();
		
		TrigramIndexSearch trigramSearch = new TrigramIndexSearch(trigramIndex, index, folderName);
		trigramSearch.startTrigramIndexSearch();
		
		
		
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
