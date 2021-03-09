/**
 * 
 */
package com.pqx.psc.util.enu;

/**
 * @author quanxing.peng
 * @date 2020年2月28日
 */
public interface IEnumStatus {

	public int getValue();
	public String getDesc();
	
	default public IEnumStatus getEnumStatus(int value) {
		throw new RuntimeException("枚举方法未实现-not implement this method.");
	}
	
	default public IEnumStatus getEnumStatus(Class<?> typClass, Integer value) {
		try {
			return EnumStatusUtils.getStatusByValue(typClass, value);
		} catch(Exception exp) {
			return EnumCommonStatus.UNKOWN;
		}
	}
}
