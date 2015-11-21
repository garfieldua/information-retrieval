package com.ukma.davydenko.indexbuilder.fb2parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.zonal.ZonalEntry;
import com.ukma.davydenko.indexbuilder.zonal.ZonalEnum;
import com.ukma.davydenko.indexbuilder.zonal.ZonalIndexElem;
import com.ukma.davydenko.indexbuilder.zonal.ZonalIndexEntry;
import com.ukma.davydenko.utils.Consts;
import com.ukma.davydenko.utils.Utils;

public class Fb2IndexBuilder {
	private static Map<Integer, String> docMapping = new HashMap<Integer, String>();
	private static int DOC_ID = 0;
	
	public static Map<Integer, String> getDocMapping() {
		return docMapping;
	}

	public static MyArray<Fb2ZonalIndexEntry> buildIndex (MyArray<Fb2ZonalEntry> entries) {
		
		MyArray<Fb2ZonalIndexEntry> index = new MyArray<>();
		
		Fb2ZonalIndexEntry indexEntry = new Fb2ZonalIndexEntry();
		Fb2ZonalIndexElem indexElem = new Fb2ZonalIndexElem();
		
		String currTerm = entries.get(0).getTerm();
		int currDocId = entries.get(0).getDocID();
		
		for (int i = 0 ; i < entries.size(); ++i) {
			if (entries.get(i).getTerm().equals(currTerm)) {
				if (entries.get(i).getDocID() == currDocId) {
					// adding new zone to ZonalIndexElem
					if (!indexElem.getZones().contains(entries.get(i).getZone()))
					indexElem.getZones().add(entries.get(i).getZone());
				} else {
					// saving last processed
					indexElem.setDocID(currDocId);
					indexEntry.getZonalPostingsList().add(indexElem);
				
					// creating new Fb2ZonalIndexElem();
					indexElem = new Fb2ZonalIndexElem();
					currDocId = entries.get(i).getDocID();
					indexElem.getZones().add(entries.get(i).getZone());
				}
			} else {
				indexElem.setDocID(currDocId);
				indexEntry.getZonalPostingsList().add(indexElem);
				
				indexEntry.setTerm(currTerm);
				index.add(indexEntry);

				// creating new ZonalIndexEntry
				indexElem = new Fb2ZonalIndexElem();
				indexElem.getZones().add(entries.get(i).getZone());
				
				currTerm = entries.get(i).getTerm();
				currDocId = entries.get(i).getDocID();
				indexEntry = new Fb2ZonalIndexEntry();
			}
		}
		
		indexElem.setDocID(currDocId);
		indexEntry.getZonalPostingsList().add(indexElem);
		
		indexEntry.setTerm(currTerm);
		index.add(indexEntry);

		
		return index;
	}
	
	public static MyArray<Fb2ZonalEntry> processEntries(String pathName) {
		MyArray<Fb2ZonalEntry> entries = new MyArray<>();
		//Parser parser = new InstantParser();
		
		try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        dBuilder = dbFactory.newDocumentBuilder();
        XPath xPath = XPathFactory.newInstance().newXPath();
        
        String paragraphExp = "//p";
		String titleExp = "//book-title";
		String firstNameExp = "//first-name";
		String secondNameExp = "//last-name";
        
			Files.walk(Paths.get(new File(pathName).getAbsolutePath())).forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					docMapping.put(++DOC_ID, filePath.getFileName().toString());
					
					try {
						Document doc = dBuilder.parse(filePath.toFile());
				        doc.getDocumentElement().normalize();
						
				        // fetching all paragraphs text
				        NodeList nodeList = (NodeList) xPath.compile(paragraphExp).evaluate(doc, XPathConstants.NODESET);
				         for (int i = 0; i < nodeList.getLength(); i++) {
				            Node nNode = nodeList.item(i);
				            
				            String[] words = nNode.getTextContent().toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement).split(Consts.universalRegex);
				            
				            for (int j = 0; j < words.length; ++j) {
				            	//System.out.println(words[j]);
					            entries.add(new Fb2ZonalEntry(words[j], DOC_ID, Fb2ZonalEnum.BODY));
				            }
				         }
				         
			     		// fetching first name
				        NodeList fnList = (NodeList) xPath.compile(firstNameExp).evaluate(doc, XPathConstants.NODESET);
				         for (int i = 0; i < fnList.getLength(); i++) {
				            Node nNode = fnList.item(i);
				            
				            String[] words = nNode.getTextContent().toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement).split(Consts.universalRegex);
				            
				            for (int j = 0; j < words.length; ++j) {
					            entries.add(new Fb2ZonalEntry(words[j], DOC_ID, Fb2ZonalEnum.AUTHOR));
				            }
				         }
				         
				        // fetching second name
				        NodeList snList = (NodeList) xPath.compile(secondNameExp).evaluate(doc, XPathConstants.NODESET);
				         for (int i = 0; i < snList.getLength(); i++) {
				            Node nNode = snList.item(i);
				            
				            String[] words = nNode.getTextContent().toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement).split(Consts.universalRegex);
				            
				            for (int j = 0; j < words.length; ++j) {

				            	System.out.println(DOC_ID + " " + words[j]);
					            entries.add(new Fb2ZonalEntry(words[j], DOC_ID, Fb2ZonalEnum.AUTHOR));
				            }
				         }
				         
				        // fetching title
				        NodeList titleList = (NodeList) xPath.compile(titleExp).evaluate(doc, XPathConstants.NODESET);
				         for (int i = 0; i < titleList.getLength(); i++) {
				            Node nNode = titleList.item(i);
				            
				            String[] words = nNode.getTextContent().toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement).split(Consts.universalRegex);
				            
				            for (int j = 0; j < words.length; ++j) {
				            	System.out.println(DOC_ID + " " + words[j]);
					            entries.add(new Fb2ZonalEntry(words[j], DOC_ID, Fb2ZonalEnum.TITLE));
				            }
				         }
			         
					  } catch (SAXException e) {
				         e.printStackTrace();
				      } catch (IOException e) {
				         e.printStackTrace();
				      } catch (XPathExpressionException e) {
				         e.printStackTrace();
				      }
				}
			});
			
		} catch (ParserConfigurationException e) {
	         e.printStackTrace();
	      }  catch (IOException e) {
	         e.printStackTrace();
	      }
		
		Arrays.sort(entries.getRawArray(), 0, entries.size());
		
		return entries;
	}
}
