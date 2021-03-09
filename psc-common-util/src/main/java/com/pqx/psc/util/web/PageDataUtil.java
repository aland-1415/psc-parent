package com.pqx.psc.util.web;

import java.util.ArrayList;
import java.util.List;

/**
 * @author quanxing.peng
 * @date 2020年3月6日
 */
public class PageDataUtil<PO, VO>{

	public PageData<VO> pageVO(PageData<PO> pagePO, Class<VO> clsVo, ToVO<PO, VO> toVo){
		PageData<VO> pageVO = new PageData<VO>();
		List<VO> vos = new ArrayList<VO>();
		
		pageVO.setTotal(pagePO.getTotal());
		List<PO> pos = pagePO.getRows();
		for (PO po : pos) {
			try {
				VO vo = clsVo.newInstance();
				toVo.operation(po, vo);
				vos.add(vo);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				throw new RuntimeException(clsVo.getName() + "对象转换失败，VO:" + e.getMessage());
			}
		}
		pageVO.setRows(vos);
		return pageVO;
	}

//	public static <T> PageData<T> convertIpage(IPage<T> iPage){
//		PageData<T> pageData = new PageData<>();
//		pageData.setTotal(iPage.getTotal());
//		pageData.setRows(iPage.getRecords());
//
//		return pageData;
//	}
}
