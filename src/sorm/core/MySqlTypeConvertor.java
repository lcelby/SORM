package sorm.core;
/**
 * MySql数据库的类型转换器
 * @author lcelby
 *
 */
public class MySqlTypeConvertor implements TypeConvertor {

	@Override
	public String dataType2JavaType(String columnType) {
		if("varchar".equalsIgnoreCase(columnType)||"char".equalsIgnoreCase(columnType)) {
			return "String";
		}else if("int".equalsIgnoreCase(columnType)
				||"tinyint".equalsIgnoreCase(columnType)
				||"smallint".equalsIgnoreCase(columnType)
				||"integer".equalsIgnoreCase(columnType)) {
			return "Integer";
		}
		else if("bigint".equalsIgnoreCase(columnType)) {
			return "Long";
		}
		else if("double".equalsIgnoreCase(columnType)||"Float".equalsIgnoreCase(columnType)) {
			return "Double";
		}else if("clob".equalsIgnoreCase(columnType)) {
			return "java.sql.Clob";
		}else if("blob".equalsIgnoreCase(columnType)) {
			return "java.sql.Blob";
		}else if("date".equalsIgnoreCase(columnType)) {
			return "java.sql.Date";
		}else if("time".equalsIgnoreCase(columnType)) {
			return "java.sql.Time";
		}else if("timestamp".equalsIgnoreCase(columnType)) {
			return "java.sql.Timestamp";
		}
		return null;
	}

	@Override
	public String JavaType2dataType(String JavaDataType) {
		// TODO Auto-generated method stub
		return null;
	}

}
