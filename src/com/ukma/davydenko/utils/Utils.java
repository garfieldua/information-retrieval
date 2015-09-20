package com.ukma.davydenko.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Utils {
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
