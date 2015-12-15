package com.baidumap.util;

public class Utility {
	
	//判断是不是中文
	public static boolean isChinese(char c) {
		boolean flag = false;
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
	            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	        flag = true;
	    }
		
		return flag;
	}
	
	public static String getPrice(String s) {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < s.length(); i++) {
			if (!isChinese(s.charAt(i))) {
				sb.append(s.charAt(i));
			}
		}
		return sb.toString();
	}

}
