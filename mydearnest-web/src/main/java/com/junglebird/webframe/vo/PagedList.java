package com.junglebird.webframe.vo;

import java.util.List;

@SuppressWarnings("rawtypes")
public class PagedList {

	private long max;
	private long min;
	private List list;
	private long lastpage;
	private long rowSize;

	public long getMax() {
		return max;
	}

	public long getMin() {
		return min;
	}

	public long getLastpage() {
		return lastpage;
	}
	
	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public long getRowSize() {
		return rowSize;
	}

	public void setRowSize(long rowSize) {
		this.rowSize = rowSize;
	}

	public void setPageRange(long page, long pageSize) {

		this.lastpage = pageSize;
		if (pageSize == 0) pageSize = 1;

		// page 표시
		long min;
		long max;
		if (pageSize < 8) {
			min = 1;
			max = pageSize;
		} else if (page - 3 > 0) {
			if (page + 3 < pageSize) {
				min = page - 3;
				max = page + 3;
			} else {
				System.out.println("*");
				min = pageSize - 6;
				max = pageSize;
			}
		} else {// 필요없을 것 같은데...
			long value = 4 - page;

			if (page + value > pageSize) {
				min = 1;
				max = pageSize;
			} else {
				min = 1;
				max = page + 3 + value;
			}
		}
		
		this.min = min;
		this.max = max;
		
	}


}
