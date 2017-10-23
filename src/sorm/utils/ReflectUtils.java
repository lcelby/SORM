package sorm.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 封装了反射相关的工具类
 * @author lcelby
 *
 */
public class ReflectUtils {
	
	/**
	 * 通过反射调用对象对应属性fieldName的get方法
	 * @param fieldName 对象的属性名
	 * @param obj 调用的对象
	 * @return 对象属性对应的值
	 */
	public static Object invokeGet(String fieldName, Object obj) {
		try {		
			Method m = obj.getClass().getDeclaredMethod("get"+StringUtils.firstChar2UpperCase(fieldName), null);
			return m.invoke(obj, null);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void invokeSet(Object obj, String columnName, Object columnValue) {
		try {
			if(columnValue!=null) {
				Method m = obj.getClass().getDeclaredMethod("set"+StringUtils.firstChar2UpperCase(columnName),
						columnValue.getClass());
				m.invoke(obj, columnValue);				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
