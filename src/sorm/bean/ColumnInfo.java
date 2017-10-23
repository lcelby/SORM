package sorm.bean;

/**
 *封装字段信息的类
 * @author lcelby
 *
 */
public class ColumnInfo {
	/**
	 * 字段名称
	 */
	private String name;
	/**
	 * 字段数据类型
	 */
	private String dataType;
	/**
	 * 字段的键类型
	 */
	private int keyType;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public int getKeyType() {
		return keyType;
	}
	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}
	
	
	public ColumnInfo(String name, String dataType, int keyType) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.keyType = keyType;
	}
	
	public ColumnInfo() {
	}
	
}
