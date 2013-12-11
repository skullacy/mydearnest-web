package com.osquare.mydearnest.api.helper;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.junglebird.webframe.vo.HttpConnectionResult;



public class HttpConnection {
	
	public static HttpConnectionResult get(String url) {

		HttpConnectionResult result = new HttpConnectionResult();
		
		try {
			URL fbURL = new URL(url);
			HttpURLConnection con = (HttpURLConnection) fbURL.openConnection();
			con.setDoInput(true);
			con.setRequestMethod("GET");
			con.setDoOutput(true);
			con.setReadTimeout(1000);
			con.setUseCaches(false);

			InputStream in;
			if (con.getResponseCode() >= 400) {
				in = con.getErrorStream();
				result.setSuccess(false);
			}
			else {
				in = con.getInputStream();
				result.setSuccess(true);
			}

			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) buffer.append(new String(b, 0, n));

			result.setResult(buffer.toString());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
		
	}

	public static HttpConnectionResult post(String url, String param) {

		HttpConnectionResult result = new HttpConnectionResult();
		
		try {
			URL fbURL = new URL(url);
			HttpURLConnection con = (HttpURLConnection) fbURL.openConnection();
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setReadTimeout(1000);
			con.setUseCaches(false);

			OutputStream out = con.getOutputStream();
			out.write( param.getBytes("UTF-8") );
			out.flush();
		    out.close();

			InputStream in;
			if (con.getResponseCode() >= 400) {
				in = con.getErrorStream();
				result.setSuccess(false);
			}
			else {
				in = con.getInputStream();
				result.setSuccess(true);
			}

			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) buffer.append(new String(b, 0, n));

			result.setResult(buffer.toString());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
		
	}

	public static HttpConnectionResult postWithAuthorize(String url, String param, String header) {

		HttpConnectionResult result = new HttpConnectionResult();
		
		try {
			URL fbURL = new URL(url);
			HttpURLConnection con = (HttpURLConnection) fbURL.openConnection();
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Authorization", header);
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setReadTimeout(1000);
			con.setUseCaches(false);

			OutputStream out = con.getOutputStream();
			out.write( param.getBytes("UTF-8") );
			out.flush();
		    out.close();

			InputStream in;
			if (con.getResponseCode() >= 400) {
				in = con.getErrorStream();
				result.setSuccess(false);
			}
			else {
				in = con.getInputStream();
				result.setSuccess(true);
			}

			StringBuffer buffer = new StringBuffer();
			byte[] b = new byte[4096];
			for (int n; (n = in.read(b)) != -1;) buffer.append(new String(b, 0, n));

			result.setResult(buffer.toString());
		}
		catch(Exception ex) {
			ex.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
		
	}
}
