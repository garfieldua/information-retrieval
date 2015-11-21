package com.ukma.davydenko.utils;

public final class Consts {
	/* Actually, there are several kinds of regex that we could use for splitting.
	 * It really depends on which kind of language we use, how do we want to represent
	 * our words in index (for example, word "they're" or "sugar-free" - with ' and -
	 * or without).
	 * Following regex allows only one or more entry of alphabetic symbols to be
	 * present in index, before splitting by following regex we remove all punctuation
	 * symbols with replaceAll("\\p{Punct}", "")
	 */
	
	public static final String splitRegex = "[^a-zA-Z]+";
	public static final String splitRegexPos = "[^a-zA-Z0-9]+";
	public static final String splitRegexGram = "[^a-zA-Z\\$]+";
	public static final String punctRegex = "\\p{Punct}";
	public static final String punctReplacement = "";
	public static final String universalRegex = "[^\\p{L}\\p{Nd}]+";
}
