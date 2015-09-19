package com.ukma.davydenko.indexbuilder.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.indexbuilder.entities.IndexEntry;

public class QueryProcessor {
	private MyArray<Integer> allDocIDs;
	private MyArray<IndexEntry> index;
	
	public QueryProcessor(MyArray<IndexEntry> index, String folder) {
		this.index = index;
		this.allDocIDs = new MyArray<>();
		
		try {
			Files.walk(Paths.get(new File(folder).getAbsolutePath())).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			    	int docID = Integer.parseInt(filePath.getFileName().toString().replaceFirst("[.][^.]+$", ""));
			    	allDocIDs.add(docID);
			    }
			});
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Arrays.sort(allDocIDs.getRawArray());
	}
	
	public int binarySearch(String word) {
        return binarySearch(word, 0, index.size());
    }

    private int binarySearch(String word, int min, int max) {
        if (min > max) {
            return -1;
        }
        
        int mid = (max + min) / 2;
        
        if (index.get(mid).getTerm().equals(word)) {
            return mid;
        } else if(index.get(mid).getTerm().compareTo(word) > 0) {
            return binarySearch(word, min, mid - 1);
        } else {
            return binarySearch(word, mid + 1, max);
        }
    }
	
    public MyArray<Integer> intersect(MyArray<Integer> list1, MyArray<Integer> list2) {
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
    
    public MyArray<Integer> union(MyArray<Integer> list1, MyArray<Integer> list2) {
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
    
    public MyArray<Integer> getComplementaryDocIDs(MyArray<Integer> list1, MyArray<Integer> list2) {
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
	
	private String toPostfix(String infix) throws Exception {

		try {
			String postfix = "";
			boolean unary = true;
			Stack<String> stack = new Stack<String>();
			StringTokenizer st = new StringTokenizer(infix, "()&|~ ", true);
			while (st.hasMoreTokens()) {
				String token = st.nextToken().trim();
				if (token.equals("")) {
				} else if (token.equals("(")) {
					stack.push(token);
				} else if (token.equals(")")) {
					String op;
					while (!(op = stack.pop()).equals("(")) {
						postfix += " " + op;
					}
				} else if (token.equals("&") || token.equals("|") || token.equals("~")) {
					if (unary) {
						token = "u" + token;
						stack.push(token);
					} else {
						int p = operatorPrecedence(token);
						while (!stack.isEmpty() && !stack.peek().equals("(")
								&& operatorPrecedence(stack.peek()) >= p) {
							String op = stack.pop();
							postfix += " " + op;
						}
						stack.push(token);
					}
					unary = true;
				} else {
					postfix += " " + token;
					unary = false; 
				}
			}
			while (!stack.isEmpty()) {
				String op = stack.pop();
				postfix += " " + op;
			}
			return postfix;
		} catch (EmptyStackException ese) {
			throw new Exception();
		} catch (NumberFormatException nfe) {
			throw new Exception();
		}
	}

	private MyArray<Integer> computePostfix(String postfix) throws Exception {

		try {
			Stack<MyArray<Integer>> stack = new Stack<>();
			StringTokenizer st = new StringTokenizer(postfix);
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.equals("&") || token.equals("|") || token.equals("u~")) {
					applyOperator(token, stack);
				} else {
					stack.push(index.get(binarySearch(token.toLowerCase().replaceAll("\\p{Punct}", ""))).getPostingsList());
				}
			}
			MyArray<Integer> result = stack.pop();
			if (!stack.isEmpty()) {
				throw new Exception();
			}
			return result;
		} catch (EmptyStackException ese) {
			throw new Exception();
		}
	}

	private void applyOperator(String operator, Stack<MyArray<Integer>> s) {
		MyArray<Integer> op1 = s.pop();
		if (operator.equals("u~")) {
			s.push(getComplementaryDocIDs(op1, allDocIDs));
		} else {
			MyArray<Integer> op2 = s.pop();
			MyArray<Integer> result = new MyArray<>();
			if (operator.equals("&")) {
				result = intersect(op1, op2);
			} else if (operator.equals("|")) {
				result = union(op1, op2);
			} else {
				throw new IllegalArgumentException();
			}
			s.push(result);
		}
	}

	private int operatorPrecedence(String operator) throws Exception {
		if (operator.equals("u~")) {
			return 2;
		} else if (operator.equals("|") || operator.equals("&")) {
			return 0;
		} else {
			throw new Exception();
		}
	}
	
	public void startQueryProcessor() {
		System.out.println();
		System.out.println("Type an infix query. Following operations are allowed:");
		System.out.println("\t& - AND operation");
		System.out.println("\t| - OR operation");
		System.out.println("\t~ - NOT operation");
		System.out.println("Parenthesis are allowed as well. To quit, type 'q'");

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		String input;
		boolean quit = false;

		do {
			try {
				System.out.print(">");
				input = in.readLine();
				if (input.equals("q")) {
					quit = true;
				} else {
					String postfix = toPostfix(input);
					//System.out.println("postfix = " + postfix);
					
					// query response
					System.out.println(computePostfix(postfix));
				}
			} catch (Exception e) {
				System.out.println("Invalid expression or no result");
			}
		} while (!quit);
	}
}
