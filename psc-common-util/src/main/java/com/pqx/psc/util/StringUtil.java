package com.pqx.psc.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author quanxing.peng
 * @date 2020年9月25日
 */
public class StringUtil {

	/**
	 * 根据最大存储空间截取字符串
	 * @param str
	 * @param maxByte 须大于0
	 * @param tailString 如果截取了则加上后缀，如...
	 * @return
	 */
	public static String subByMaxByte(String str, int maxByte, String tailString) {
		if (str == null) {
			return str;
		}
		byte[] bytes = str.getBytes();
		if (bytes.length <= maxByte) {
			return str;
		}else {
			tailString = (tailString == null) ? "" : tailString;
			int tailByteLength = tailString.getBytes().length;
			byte[] subBytes = ArrayUtils.subarray(bytes, 0, maxByte - tailByteLength);
			
			return new String(subBytes) + tailString;
		}
	}
	
	public static String subByMaxByte(String str, int maxByte) {
		String tailString = "...";
		return subByMaxByte(str, maxByte, tailString);
	}

	/**
	 *  查找从开始索引第一个sourceChar的配对字符targetChar索引
	 * @param source  源字符串
	 * @param startIndex 从sourceCharIndex开始匹配
	 * @param sourceChar targetChar 两个字符不能是一样的，sourceChar在targetChar前边才算一个配对
	 * @return
	 */
	public static Integer getPairedCharIndex(String source, int startIndex, char sourceChar, char targetChar){
		Integer targetCharIndex = null;

		if (sourceChar == targetChar)
			throw new RuntimeException("sourceChar,targetChar两个字符不能是一样的");
		if (startIndex > source.length() - 1)
			return null;

		char[] charArray =  source.toCharArray();
		startIndex = (startIndex < 0) ? 0 : startIndex;
		int sourceCharCount = 0 ;
		int targetCharCount = 0 ;
		for (int i = startIndex; i < charArray.length; i ++ ){
			if (charArray[i] == sourceChar){
				sourceCharCount ++;
			}else if (charArray[i] == targetChar && sourceCharCount > targetCharCount){  //targetCharCount不可能比sourceCharCount计数多，多了就说明targetChar在最前边，这个不计算
				targetCharCount ++;
				if (sourceCharCount == targetCharCount){
					targetCharIndex = i;
					break;
				}
			}
		}
		return  targetCharIndex;
	}
	
	
	public static boolean assertJson(String jsonStr) {
		try {
			JSONObject.parse(jsonStr);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean assertJsonAndThrow(String jsonStr, String message) {
		try {
			JSONObject.parse(jsonStr);
			return true;
		} catch (Exception e) {
			throw new RuntimeException(message);
		}
	}
	
}
