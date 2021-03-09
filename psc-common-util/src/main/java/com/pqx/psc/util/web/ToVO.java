package com.pqx.psc.util.web;

/**
 * @author quanxing.peng
 * @date 2020年3月6日
 */
public interface ToVO<Po, Vo>{
	
	void operation(Po po, Vo vo);
}
