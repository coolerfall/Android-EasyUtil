package com.droiddev.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtil
{
	/**
	 * @param source what you want to split
	 * @param separator 
	 * @return the 1st String
	 */
	public static String cutString(String source, String separator){
		int pos = source.indexOf(separator);
		return source.substring(0, pos);
	}
	
	public static String getEnd(String source, String separator){
		String[] tmp = source.split(separator);
		return tmp[tmp.length-1];
	}
	
	/**
	 * Filter the input string.
	 * 
	 * @param  str the string to filter
	 * @return the result
	 * @throws PatternSyntaxException
	 */
	public static String stringFilter(String str) throws PatternSyntaxException {

		String regEx = "[/\\:,.，。、！!：；‘’“”()~&@^#;*?<>|\'\"\n\t]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		return m.replaceAll("");
	}
	
	/**
	 * Filter input string according to the regex.
	 * 
	 * @param  str   the string to filter
	 * @param  regEx the reg expression
	 * @return       the filtered string
	 * @throws PatternSyntaxException
	 */
	public static String stringFilter(String str, String regEx) throws PatternSyntaxException {
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);

		return m.replaceAll("");
	}
}