package com.ukma.davydenko.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.fb2parser.Fb2ZonalEnum;
import com.ukma.davydenko.indexbuilder.fb2parser.Fb2ZonalIndexElem;
import com.ukma.davydenko.indexbuilder.zonal.ZonalEnum;
import com.ukma.davydenko.indexbuilder.zonal.ZonalIndexElem;

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
	
	public static MyArray<ZonalEnum> unionZones(MyArray<ZonalEnum> zones1, MyArray<ZonalEnum> zones2) {
		MyArray<ZonalEnum> resultZone = new MyArray<>();
		
		for (int i = 0; i < zones1.size(); ++i) {
			resultZone.add(zones1.get(i));
		}
		
		for (int i = 0; i < zones2.size(); ++i) {
			resultZone.add(zones2.get(i));
		}
		
		return resultZone;
	}
	
	// for zonal posting lists
	public static MyArray<ZonalIndexElem> zonalUnion(MyArray<ZonalIndexElem> list1, MyArray<ZonalIndexElem> list2) {
    	MyArray<ZonalIndexElem> resultList = new MyArray<>();
    	
    	int i = 0;
    	int j = 0;
    	
    	while (i < list1.size() && j < list2.size()) {
    		if (list1.get(i).getDocID() == list2.get(j).getDocID()) {
    			MyArray<ZonalEnum> unionZones = unionZones(list1.get(i).getZones(), list2.get(i).getZones());
    			resultList.add(new ZonalIndexElem(list1.get(i).getDocID(), unionZones));
    			++i;
    			++j;
    		} else if (list1.get(i).getDocID() < list2.get(j).getDocID()) {
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
	
    public static MyArray<Fb2ZonalIndexElem> fb2ZonalIntersect(MyArray<Fb2ZonalIndexElem> list1, MyArray<Fb2ZonalIndexElem> list2) {
    	MyArray<Fb2ZonalIndexElem> resultList = new MyArray<>();
    	
    	int i = 0;
    	int j = 0;
    	
    	while (i < list1.size() && j < list2.size()) {
    		if (list1.get(i).getDocID() == list2.get(j).getDocID()) {
    			MyArray<Fb2ZonalEnum> unionZones = fb2UnionZones(list1.get(i).getZones(), list2.get(i).getZones());
    			resultList.add(new Fb2ZonalIndexElem(list1.get(i).getDocID(), unionZones));
    			++i;
    			++j;
    		} else if (list1.get(i).getDocID() < list2.get(j).getDocID()) {
    			++i;
    		} else {
    			++j;
    		}
    	}
    	
    	return resultList;
    }
	
	public static MyArray<Fb2ZonalEnum> fb2UnionZones(MyArray<Fb2ZonalEnum> zones1, MyArray<Fb2ZonalEnum> zones2) {
		MyArray<Fb2ZonalEnum> resultZone = new MyArray<>();
		
		for (int i = 0; i < zones1.size(); ++i) {
			resultZone.add(zones1.get(i));
		}
		
		for (int i = 0; i < zones2.size(); ++i) {
			resultZone.add(zones2.get(i));
		}
		
		return resultZone;
	}
	
	// for zonal posting lists
	public static MyArray<Fb2ZonalIndexElem> fb2ZonalUnion(MyArray<Fb2ZonalIndexElem> list1, MyArray<Fb2ZonalIndexElem> list2) {
    	MyArray<Fb2ZonalIndexElem> resultList = new MyArray<>();
    	
    	int i = 0;
    	int j = 0;
    	
    	while (i < list1.size() && j < list2.size()) {
    		if (list1.get(i).getDocID() == list2.get(j).getDocID()) {
    			MyArray<Fb2ZonalEnum> unionZones = fb2UnionZones(list1.get(i).getZones(), list2.get(i).getZones());
    			resultList.add(new Fb2ZonalIndexElem(list1.get(i).getDocID(), unionZones));
    			++i;
    			++j;
    		} else if (list1.get(i).getDocID() < list2.get(j).getDocID()) {
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
	
    public static MyArray<ZonalIndexElem> zonalIntersect(MyArray<ZonalIndexElem> list1, MyArray<ZonalIndexElem> list2) {
    	MyArray<ZonalIndexElem> resultList = new MyArray<>();
    	
    	int i = 0;
    	int j = 0;
    	
    	while (i < list1.size() && j < list2.size()) {
    		if (list1.get(i).getDocID() == list2.get(j).getDocID()) {
    			MyArray<ZonalEnum> unionZones = unionZones(list1.get(i).getZones(), list2.get(i).getZones());
    			resultList.add(new ZonalIndexElem(list1.get(i).getDocID(), unionZones));
    			++i;
    			++j;
    		} else if (list1.get(i).getDocID() < list2.get(j).getDocID()) {
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
