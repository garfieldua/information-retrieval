package com.ukma.davydenko.indexbuilder.fb2parser;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

import com.ukma.davydenko.indexbuilder.data.MyArray;
import com.ukma.davydenko.utils.Consts;
import com.ukma.davydenko.utils.Utils;

public class Fb2ZonalIndexSearch {
	private MyArray<Fb2ZonalIndexEntry> index;
	
	public Fb2ZonalIndexSearch(MyArray<Fb2ZonalIndexEntry> index) {
		this.index = index;
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

	private MyArray<Fb2ZonalIndexElem> computePostfix(String postfix) throws Exception {

		try {
			Stack<MyArray<Fb2ZonalIndexElem>> stack = new Stack<>();
			StringTokenizer st = new StringTokenizer(postfix);
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (token.equals("&") || token.equals("|") || token.equals("u~")) {
					applyOperator(token, stack);
				} else {	
					int temp = binarySearch(token.toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement));
					System.out.println(temp);
					stack.push(index.get(binarySearch(token.toLowerCase().replaceAll(Consts.punctRegex, Consts.punctReplacement))).getZonalPostingsList());
				}
			}
			MyArray<Fb2ZonalIndexElem> result = stack.pop();
			if (!stack.isEmpty()) {
				throw new Exception();
			}
			return result;
		} catch (EmptyStackException ese) {
			throw new Exception();
		}
	}

	private void applyOperator(String operator, Stack<MyArray<Fb2ZonalIndexElem>> s) {
		MyArray<Fb2ZonalIndexElem> op1 = s.pop();

			MyArray<Fb2ZonalIndexElem> op2 = s.pop();
			MyArray<Fb2ZonalIndexElem> result = new MyArray<>();
			if (operator.equals("&")) {
				result = Utils.fb2ZonalIntersect(op1, op2);
			} else if (operator.equals("|")) {
				result = Utils.fb2ZonalUnion(op1, op2);
			} else {
				throw new IllegalArgumentException();
			}
			s.push(result);
		
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
	
	public MyArray<Fb2ZonalIndexElem> processQuery(String query) throws Exception {
		String postfix = toPostfix(query);
		
		return computePostfix(postfix);
	}
    
    public MyArray<Integer> getPostingsId(Fb2ZonalIndexEntry indexEntry) {
    	MyArray<Integer> result = new MyArray<>();
    	
    	for (int i = 0; i < indexEntry.getZonalPostingsList().size(); ++i) {
    		result.add(indexEntry.getZonalPostingsList().get(i).getDocID());
    	}
    	
    	return result;
    }
    
    class ZonalScore implements Comparable<ZonalScore> {
		int docID;
		double score;
		
		public ZonalScore(int docID, double score) {
			this.docID = docID;
			this.score = score;
		}

		@Override
		public int compareTo(ZonalScore o) {
			return new Integer(this.docID).compareTo(new Integer(o.docID));
		}

		@Override
		public String toString() {
			return "ZonalScore [docID=" + docID + ", score=" + score + "]";
		}
	}
    
    public MyArray<ZonalScore> getScores(MyArray<Fb2ZonalIndexElem> postingList) {
    	MyArray<ZonalScore> scores = new MyArray<>();
    	
    	for (int i = 0; i < postingList.size(); ++i) {
    		double docScore = 0;
    		boolean[] areaHit = new boolean [Fb2ZonalEnum.values().length];
    		for (int j = 0; j < areaHit.length; ++j) {
    			areaHit[j] = false;
    		}
    		
    		MyArray<Fb2ZonalEnum> zonalArray = postingList.get(i).getZones();
    		
    		for (int j = 0; j < zonalArray.size(); ++j) {
    			if (areaHit[zonalArray.get(j).ordinal()] == false) {
					docScore += zonalArray.get(j).getZoneWeight();
					areaHit[zonalArray.get(j).ordinal()] = true;
				}
    		}
    		
    		scores.add(new ZonalScore(postingList.get(i).getDocID(), docScore));
    	}
    	
    	return scores;
    }
    
    public void startZonalIndexSearch() {
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
				
					MyArray<ZonalScore> scores = getScores(computePostfix(postfix));
					System.out.println(scores);
				}
			} catch (Exception e) {
				//e.printStackTrace();
				System.out.println("Invalid expression or no result");
			}
		} while (!quit);
	}
}