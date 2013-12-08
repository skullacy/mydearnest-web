package com.junglebird.webframe.common;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {

	public static String getPathHash(String message)
	{
	
		try {
			MessageDigest mda = MessageDigest.getInstance("SHA-512");
			byte [] b = mda.digest(message.getBytes());

			String result = "";
			for (int i=0; i < b.length; i++) result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
			
			return message.length() + "." + result;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
			
		}
		
	}

	public static String getMD5Checksum(InputStream inputStream)  throws Exception
	{
		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = inputStream.read(buffer);
			if (numRead > 0) complete.update(buffer, 0, numRead);
		} 
		while (numRead != -1);

		inputStream.close();
   
		byte[] b = complete.digest();
   
		String result = "";
		for (int i=0; i < b.length; i++) result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		
		return result;
	}


	public static String getSHA1Checksum(InputStream inputStream) throws Exception  {

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("SHA-1");
		int numRead;

		do {
			numRead = inputStream.read(buffer);
			if (numRead > 0) complete.update(buffer, 0, numRead);
		} 
		while (numRead != -1);

		inputStream.close();
   
		byte[] b = complete.digest();
   
		String result = "";
		for (int i=0; i < b.length; i++) result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		
		return result;
	}
	
	public static String parseMD5(String message)
	{
		try {
			MessageDigest mda = MessageDigest.getInstance("MD5");
			byte [] b = mda.digest(message.getBytes());
	
			String result = "";
			for (int i=0; i < b.length; i++) result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
			
			return result;
		}
		catch(Exception ex) {
			return message;
		}
	}

	public static String parseMD5(byte[] message) throws NoSuchAlgorithmException
	{
		MessageDigest mda = MessageDigest.getInstance("MD5");
		byte [] b = mda.digest(message);

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
			return message;
		}
	}

	public static String parseSHA(byte[] message)
	{
		try
		{
			MessageDigest mda = MessageDigest.getInstance("SHA-1");
			byte [] b = mda.digest(message);
	
			String result = "";
			for (int i=0; i < b.length; i++) result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
			
			return result;
		}
		catch(Exception ex) {
			return "-";
		}
	}
}
