package sorm.core;

/**
 * 负责将java数据类型和数据库数据类型相互转换的类
 * @author lcelby
 *
 */
public interface TypeConvertor {
	/**
	 * 将数据库数据类型转换为java数据类型
	 * @param columnType 数据库数据类型
	 * @return java数据类型
	 */
	public String dataType2JavaType(String columnType);
	
	/**
	 * 将java数据类型转换为数据库数据类型
	 * @param JavaDataType java数据类型
	 * @return 数据库数据类型
	 */
	public String JavaType2dataType(String JavaDataType);

}
