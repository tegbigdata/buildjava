package com.teg.dynamicClass.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static boolean findReg (String str, String regex){
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(str);
		if (matcher.find()){
		   return true;
		}
		return false;
	}
	
	public static String subString (String str){
		if(StringUtil.isEmpty(str)){
			return "";
		}
		return str.substring(0, str.length()-1);
	}
	/**
	 * 检查字符串是否为空
	 */
	public static boolean isNotBlank(String str) {
		boolean isBlank = true;
		if (str == null || "".equals(str.trim())) {
			isBlank = false;
		}
		return isBlank;
	}

	public static boolean isEmpty(String str) {
		boolean empty = false;
		if (str == null || str.trim().length() <= 0) {
			empty = true;
		}
		return empty;
	}

	
	
	public static String join (Object array[], String separator, String prefix, String suffix){
		int startIndex = 0;
		int endIndex = array.length;
        /*if(array == null){
        	return null;
        }*/
        if(separator == null)
            separator = "";
        int bufSize = endIndex - startIndex;
        if(bufSize <= 0)
            return "";
        bufSize *= (array[startIndex] != null ? array[startIndex].toString().length() : 16) + separator.length();
        StringBuffer buf = new StringBuffer(bufSize);
        for(int i = startIndex; i < endIndex; i++)
        {
            if(i > startIndex)
                buf.append(separator);
            if(array[i] != null)
                buf.append(prefix).append(array[i]).append(suffix);
        }

        return buf.toString();
    
	}
	
}
