package com.pqx.psc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author quanxing.peng
 * @date 2020年2月29日
 */
public class ReflectUtil {

	public static Class<?> getClassByFullMethodPath(String methodFullPath) {
		try {
			String className = methodFullPath.substring(0, methodFullPath.lastIndexOf("."));
			return Class.forName(className);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ClassNotFound: 未找到" + methodFullPath + "方法的类，请检查名称是否正确，以及相关的maven依赖是否正确添加！");
		}
	}
	
	
	public static Method getMethod(String methodFullPath) {
		Class<?> clazz = getClassByFullMethodPath(methodFullPath);
		return getMothd(clazz, methodFullPath);
	}
	
	public static Method getMothd(Class<?> clazz, String methodFullPath) {
		String methodName = methodFullPath.substring(methodFullPath.lastIndexOf(".") + 1);
		for (Method m : clazz.getMethods()) {
            if (methodName.equals(m.getName())) {
                return m;
            }
        }
		throw new RuntimeException("MethodNotFound: 未找到" + methodFullPath + "方法，请检查方法名称是否正确,或相关的maven依赖版本号是否正确！");
	}
	
	
	public static String[] getParamTypeNames(Method method) {
		List<String> list = new ArrayList<String>();
        for (Class<?> c : method.getParameterTypes()) {
            list.add(c.getName());
        }
        
        return list.toArray(new String[list.size()]);
	}
	
	/**
	 * 将方法的json入参解析成对应的object数组，主要用于调用dubbo接口使用
	 * @return 
	 */
	public static Object[] parseParam2Objects(Method method, String[] paramNames, JSONObject paramsJson) {
		Class<?>[] paramTypes = method.getParameterTypes();
		List<Object> params = new ArrayList<Object>();
		
		if (paramTypes.length != paramNames.length) {
			throw new RuntimeException(method.getName() + "接口方法的参数与给定参数名个数不一致，请检查接口方法参数名的配置是否正确！");
		}
		for (int i = 0; i < paramTypes.length; i++) {
			Object paramObject = parseObject(paramTypes[i], paramNames[i], paramsJson);
			params.add(paramObject);
		}
		
		return params.toArray();
	}
	
	public static Object parseObject(Class<?> clazz, String name, JSONObject jsonObject) {
		Object value = jsonObject.get(name);
		if (value == null || "null".equals(value)) {
            return null;
        }
		
		//基本数据类型
		if (clazz.getClassLoader() == null) {
			Object parseRes = null;
			//日期最好只传许传入yyyy-MM-dd HH:mm:ss格式的
			if (clazz.getName().equals(Date.class.getName())) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse((String) value);
				} catch (ParseException e) {
					return JSON.parseObject(value.toString(), Date.class);
				}
			}
			//数组内只能是基本数据类型
			try {
				//String类型的使用JSON.parseObject解析报错，故单独处理，直接返回
				if (String.class.getName().equals(clazz.getName())) {
					parseRes = value;
				}else {
					parseRes = JSON.parseObject(value.toString(), clazz);
				}
			} catch (Exception e) {
				throw new RuntimeException(name + "字段格式错误，正确数据类型为:" + clazz.getName());
			}
			return parseRes;
		}else {
		//自定义类
			Map<String, Object> paraMap = new HashMap<String, Object>();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				paraMap.put(field.getName(), parseObject(field.getType(), field.getName(), (JSONObject) JSONObject.toJSON(value)));
			}
			return paraMap;
		}
		
	}

	/**
	 * 从jar包获取所有类
	 * @param filePath
	 * @return
	 */
	public static List<Class> getClassFromJar(String filePath){
		if (!filePath.endsWith(".jar")){
			throw new RuntimeException("文件不是jar包！");
		}

		File packageFile = new File(filePath);
		List<Class> classList = new ArrayList<>();
		try {
			JarFile jarFile = new JarFile(filePath);
			Enumeration<JarEntry> enumeration = jarFile.entries();
			while (enumeration.hasMoreElements()){
				JarEntry jarEntry = enumeration.nextElement();
				String fileName = jarEntry.getName();
				//非class文件则不处理
				if (!fileName.endsWith(".class")){
					continue;
				}
				String className = fileName.replace("/", ".")
						.substring(0, fileName.length()-6)
						.replaceAll("\\$\\d*$", "");
				URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{packageFile.toURI().toURL()}, Thread.currentThread().getContextClassLoader());
				Class clazz = null;
				clazz = urlClassLoader.loadClass(className);
				classList.add(clazz);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return classList;
	}

}



