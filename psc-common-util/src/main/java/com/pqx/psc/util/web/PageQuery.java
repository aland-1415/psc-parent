package com.pqx.psc.util.web;

/**
 * @author quanxing.peng
 * @date 2020年3月3日
 */
public class PageQuery<T> {

	private Long page;  //当前页
	private Long pageSize;
	private Object filter;
	
	public PageQuery() { }
	
	public PageQuery(Long page, Long pageSize) {
		this.page = page;
		this.pageSize = pageSize;
		this.filter = null;
	}
	
	public PageQuery(Long page, Long pageSize, T filter) {
		this.page = page;
		this.pageSize = pageSize;
		this.filter = filter;
	}
	
	public Object getFilter() {
		return filter;
	}
	public void setFilter(Object filter) {
		this.filter = filter;
	}
	public Long getPage() {
		return page;
	}
	public void setPage(Long page) {
		this.page = page;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	 
	//查询开始
	public Long getStart(){
		return (this.page - 1) * pageSize;
	}
}

