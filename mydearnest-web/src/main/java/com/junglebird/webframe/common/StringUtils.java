package com.junglebird.webframe.common;

public class StringUtils {

	public static boolean isEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static String replaceCrLf(String str) {
		return str.replaceAll("\\n", "<br />");
	}
}
