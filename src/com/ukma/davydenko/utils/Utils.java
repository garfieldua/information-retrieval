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
