package com.osquare.mydearnest.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

	
	public static String parseMD5(String message) throws NoSuchAlgorithmException
	{
		MessageDigest mda = MessageDigest.getInstance("MD5");
		byte [] b = mda.digest(message.getBytes());

		String result = "";
		for (int i=0; i < b.length; i++) result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		
		return result;
	}

	public static String parseSHA(String message)
	{
		try
		{
			MessageDigest mda = MessageDigest.getInstance("SHA-1");
			byte [] b = mda.digest(message.getBytes());
	
			String result = "";
			for (int i=0; i < b.length; i++) result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
			
			return result;
		}
		catch(Exception ex) {
			return "-";
		}
	}

}
