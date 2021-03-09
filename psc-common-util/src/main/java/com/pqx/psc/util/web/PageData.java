package com.pqx.psc.util.web;

import java.util.List;

/**
 * @author quanxing.peng
 * @date 2020年3月3日
 */
public class PageData<T> {
	private Long total;
	private List<T> rows;
	
	public PageData() {}
	public PageData(Long total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
