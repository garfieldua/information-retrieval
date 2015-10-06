package com.ukma.davydenko.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.ukma.davydenko.indexbuilder.data.MyArray;

public class Utils {
	public static MyArray<String> getTermsIntersection(MyArray<String> list1, MyArray<String> list2) {
		MyArray<String> resultList = new MyArray<>();
		
		int i = 0;
    	int j = 0;
    	
    	while (i < list1.size() && j < list2.size()) {
    		if (list1.get(i).equals(list2.get(j))) {
    			resultList.add(list1.get(i));
    			++i;
    			++j;
    		} else if (list1.get(i).compareTo(list2.get(j)) < 0) {
    			++i;
    		} else {
    			++j;
    		}
    	}
		
		return resultList;
	}
	
	public static MyArray<Integer> union(MyArray<Integer> list1, MyArray<Integer> list2) {
    	MyArray<Integer> resultList = new MyArray<>();
    	
    	int i = 0;
    	int j = 0;
    	
    	while (i < list1.size() && j < list2.size()) {
    		if (list1.get(i) == list2.get(j)) {
    			resultList.add(list1.get(i));
    			++i;
    			++j;
    		} else if (list1.get(i) < list2.get(j)) {
    			resultList.add(list1.get(i));
    			++i;
    		} else {
    			resultList.add(list2.get(j));
    			++j;
    		}
    	}
    	
    	// copy left-overs
    	while (i < list1.size()) {
    		resultList.add(list1.get(i));
    		++i;
    	}
    	
    	while (j < list2.size()) {
    		resultList.add(list2.get(j));
    		++j;
    	}
    	
    	return resultList;
    }
	
    public static MyArray<Integer> intersect(MyArray<Integer> list1, MyArray<Integer> list2) {
    	MyArray<Integer> resultList = new MyArray<>();
    	
    	int i = 0;
    	int j = 0;
    	
    	while (i < list1.size() && j < list2.size()) {
    		if (list1.get(i) == list2.get(j)) {
    			resultList.add(list1.get(i));
    			++i;
    			++j;
    		} else if (list1.get(i) < list2.get(j)) {
    			++i;
    		} else {
    			++j;
    		}
    	}
    	
    	return resultList;
    }
    
    public static MyArray<Integer> getComplementaryDocIDs(MyArray<Integer> list1, MyArray<Integer> list2) {
		MyArray<Integer> resultList = new MyArray<>();
		
    	int i = 0;
    	int j = 0;
    	
    	while (i < list2.size() && j < list1.size()) {
    		if (list2.get(i).equals(list1.get(j))) {
    			++i;
    			++j;
    		} else if (list2.get(i) < list1.get(j)) {
    			resultList.add(list2.get(i));
    			++i;
    		} 
    	}
    	
    	while (i < list2.size()) {
    		resultList.add(list2.get(i));
    		++i;
    	}
    	
    	return resultList;
	}
	
	public static void serializer(Object obj, String fileName) {
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
}
