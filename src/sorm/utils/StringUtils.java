package sorm.utils;
/**
 * 封装了字符串处理需要的工具类
 * @author lcelby
 *
 */
public class StringUtils {
	/**
	 * 讲一个字符串的首字母大写并返回
	 * @param str 要转换的字符串
	 * @return 首字母大写后的字符串
	 */
	public static String firstChar2UpperCase(String str) {
		//abcd-->ABCD-->Abcd
		return str.toUpperCase().substring(0, 1)+str.substring(1);
		}
}
