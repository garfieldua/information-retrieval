package com.ukma.davydenko.indexbuilder.suffix;

public abstract class Alphabet {

	public final String alphabet;
	
	public final static Alphabet LOWERCASE = new Alphabet("$abcdefghijklmonpqrstuvwxyz") {
		
		private final int charOffest = Character.getNumericValue('a');

		public byte getIndex(char c) {
			if (c == '$') {
				return 26;
			}
			return (byte) (Character.getNumericValue(c) - charOffest);
		}
	}; 
	
	
	public Alphabet(String alphabet) {
		this.alphabet = alphabet;
	}
	
	public byte getIndex(char c) {
		return (byte) alphabet.indexOf(c);
	}

	public int size() {
		return alphabet.length();
	}
}