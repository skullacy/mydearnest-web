package com.osquare.mydearnest.profile.vo;

import java.util.Collection;

import com.osquare.mydearnest.entity.Folder;
import com.osquare.mydearnest.entity.ImageSource;


public class FolderItem {

	private Folder folder;
	private Collection<ImageSource> imageSources;

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public Collection<ImageSource> getImageSources() {
		return imageSources;
	}

	public void setImageSources(Collection<ImageSource> collection) {
		this.imageSources = collection;
	}

}
