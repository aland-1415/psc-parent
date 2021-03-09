/**
 * 
 */
package com.pqx.psc.util.enu;

/**
 * @author quanxing.peng
 * @date 2020年2月28日
 */
public enum EnumCommonStatus implements IEnumStatus {

	YES(1, "是"),
	NO(0, "否"),
	UNKOWN(-10000, "未知状态");

	private int value;
	private String desc;

	private EnumCommonStatus(int value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	
	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public IEnumStatus getEnumStatus(int value) {
		return EnumStatusUtils.getStatusByValue(EnumCommonStatus.class, value);
	}
}
