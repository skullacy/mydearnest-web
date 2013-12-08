package com.junglebird.webframe.common;

import java.io.FileInputStream;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertiesManager {

	private String path;
	private JSONObject data;
	
	@Autowired private SessionFactory sessionFactory;

	public void setPath(String _path) {
		this.path = _path;
		
		data = new JSONObject();
		try {
			String propertyPath = "";
			
			JSONObject propertyPathes = new JSONObject();
			Properties p = new Properties();
			p.load(getClass().getResourceAsStream("/" + this.path));
			for(Object key : p.keySet()) 
				propertyPathes.put(key.toString(), p.getProperty(key.toString()));
			p.clear();

			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				propertyPath = propertyPathes.has("conf.win.path") ? propertyPathes.getString("conf.win.path") : "";
			}
			else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
				propertyPath = propertyPathes.has("conf.linux.path") ? propertyPathes.getString("conf.linux.path") : "";
			}
			else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
				propertyPath = propertyPathes.has("conf.osx.path") ? propertyPathes.getString("conf.osx.path") : "";
			}

			if (propertyPath == null || propertyPath.isEmpty())
				propertyPath = propertyPathes.getString("conf.path");
			
			p = new Properties();
			p.load(new FileInputStream(propertyPath));
			for(Object key : p.keySet()) 
				data.put(key.toString(), p.getProperty(key.toString()));
			
			p.clear();
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
		
	public String get(String key) {
		try {
			return data.getString(key);
		}
		catch (Exception ex){
			return null;
		}
	}
	
	public Integer getInteger(String key) {
		try {
			return Integer.parseInt(data.getString(key));
		}
		catch (Exception ex){
			return null;
		}
	}

}
