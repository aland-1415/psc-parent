/**
 * 
 */
package com.pqx.psc.util.enu;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author quanxing.peng
 * @date 2020年2月28日
 */
public class EnumStatusUtils {

	public static IEnumStatus getStatusByValue(Class<?> enums, Integer value) {
		if(enums.isEnum()) {
			Object[] statuses = enums.getEnumConstants();
			for(int i = 0; i < statuses.length; i++) {
				IEnumStatus status = (IEnumStatus)statuses[i];
				if(status.getValue() == value) {
					return status;
				}
			}
		}
		
		return EnumCommonStatus.UNKOWN;
	}
	
	public static IEnumStatus getStatusByDesc(Class<?> enums, String desc) {
		if(enums.isEnum()) {
			Object[] statuses = enums.getEnumConstants();
			for(int i = 0; i < statuses.length; i++) {
				IEnumStatus status = (IEnumStatus)statuses[i];
				if(status.getDesc().equals(desc)) {
					return status;
				}
			}
		}
		
		return EnumCommonStatus.UNKOWN;
	}
	
	/**
	 * 忽略大小写的
	 */
	public static IEnumStatus getStatusByEnumName(Class<?> enums, String desc) {
		if(enums.isEnum()) {
			Object[] statuses = enums.getEnumConstants();
			for(int i = 0; i < statuses.length; i++) {
				IEnumStatus status = (IEnumStatus)statuses[i];
				if(status.toString().equalsIgnoreCase(desc)) {
					return status;
				}
			}
		}
		
		return EnumCommonStatus.UNKOWN;
	}
	
	/**
	 * 
	 * @param enums  枚举类
	 * @param sta    提示返回值
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Hashtable> getListByEnum(Class<?> enums,String sta){
		List<Hashtable> resList =new ArrayList<Hashtable>();
		Hashtable allTab = new Hashtable();
		allTab.put(sta+"_value", "-1");  
		allTab.put(sta+"_name", "全部");
		resList.add(0,allTab);
		if(enums.isEnum()) {
			Object[] statuses = enums.getEnumConstants();
			for(int i = 0; i < statuses.length; i++) {
				IEnumStatus status = (IEnumStatus)statuses[i];
				Hashtable tab = new Hashtable();
				tab.put(sta+"_value", status.getValue());  
				tab.put(sta+"_name", status.getDesc());
				resList.add((i+1),tab);
			}
		}
		return resList;
	}
	
	/**
	 * 数据列表
	 * @param enums  枚举类
	 * @param sta    提示返回值
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Hashtable> getDataListByEnum(Class<?> enums,String sta){
		List<Hashtable> resList =new ArrayList<Hashtable>();
		if(enums.isEnum()) {
			Object[] statuses = enums.getEnumConstants();
			for(int i = 0; i < statuses.length; i++) {
				IEnumStatus status = (IEnumStatus)statuses[i];
				Hashtable tab = new Hashtable();
				tab.put(sta+"_value", status.getValue());  
				tab.put(sta+"_name", status.getDesc());
				resList.add((i),tab);
			}
		}
		return resList;
	}
	
}
