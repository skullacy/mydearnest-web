package com.junglebird.webframe.common;

public class PasswordUtils {
	
	public final static int MD5_PASSWORD = 5510;
	public final static int SHA1_PASSWORD = 5511;
	public final static int SHA128_PASSWORD = 5512;
	public final static int SHA256_PASSWORD = 5513;
	public final static int MYSQL_PASSWORD = 5514;
	public final static int MYSQL_OLD_PASSWORD = 5515;
	
	public static String parse(String password, String mode) {
		return parse(password, getCode(mode));
	}
	
	public static String parse(String password, int mode) {
		
		String result = null;
		switch(mode) {
			case MD5_PASSWORD:
				result = parseMD5(password);
			break;
			case SHA1_PASSWORD:
				result = parseSHA1(password);
			break;
			case MYSQL_PASSWORD:
				result = parseMYSQL(password);
			break;
			case MYSQL_OLD_PASSWORD:
				result = parseOldMYSQL(password);
			break;
		}
		
		return result;
	}
	
	private static int getCode(String mode) {
		return MD5_PASSWORD;
	}
	
	private static String parseMD5(String password) {
		
		return null;
	}

	private static String parseSHA1(String password) {
		
		return null;
	}

	private static String parseMYSQL(String password) {
		
		return null;
	}

	private static String parseOldMYSQL(String password) {
		
		return null;
	}
}
