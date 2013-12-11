package com.osquare.mydearnest.post.vo;

import java.io.InputStream;

public class ImageSourceFile {

	private InputStream inputStream;
	private Long fileLength;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Long getFileLength() {
		return fileLength;
	}

	public void setFileLength(Long fileLength) {
		this.fileLength = fileLength;
	}

}
