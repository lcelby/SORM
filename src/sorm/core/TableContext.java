package sorm.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sorm.bean.ColumnInfo;
import sorm.bean.TableInfo;
import sorm.utils.JavaFileUtils;
import sorm.utils.StringUtils;

/**
 *负责获取管理数据库所有表结构和类结构的管理，并可以根据表结构生成类结构
 * @author lcelby
 *
 */
public class TableContext {
	/**
	 * 表名为key表信息为value
	 */
	public static Map<String,TableInfo> tables = new HashMap<String,TableInfo>();
	/**
	 * 将po的class和表信息对象关联，便于重用
	 */
	public static Map<Class,TableInfo> poClassTableMap = new HashMap<Class,TableInfo>();
	
	private TableContext(){}
	
	static {
		try {
			//初始化获得表的信息
			Connection con = DBManager.getConn();
			DatabaseMetaData dbmd = con.getMetaData();
			
			ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[]{"TABLE"});
			
			while(tableRet.next()) {
				String tableName = (String) tableRet.getObject("TABLE_NAME");
				TableInfo ti = new TableInfo(tableName, new HashMap<String,ColumnInfo>(),
						new ArrayList<ColumnInfo>());
				tables.put(tableName, ti);
				
				ResultSet set = dbmd.getColumns(null, "%", tableName, "%");//查询表中所有字段
				while(set.next()) {
					ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),
							set.getString("TYPE_NAME"), 0);
					ti.getColumns().put(set.getString("COLUMN_NAME"), ci);
				}
				ResultSet set2 = dbmd.getPrimaryKeys(null, "%", tableName);//查询t_user类中的主键
				while(set2.next()) {
					ColumnInfo ci2 = ti.getColumns().get(set2.getObject("COLUMN_NAME"));
					ci2.setKeyType(1);//设置为主键类型
					ti.getPriKeys().add(ci2);
				}
				
				if(ti.getPriKeys().size()>0) {//取唯一主键，方便使用，如果为联合主键，则为空
					ti.setOnlyPriKey(ti.getPriKeys().get(0));
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//更新类结构
		updateJavaPoFiles();
		//加载po类
		loadPoFile();
	}
	
	/**
	 * 根据表结构更新配置Po包下面的java类，实现表结构到类结构的转换
	 */
	public static void updateJavaPoFiles() {
		Map<String,TableInfo> map = TableContext.tables;
		for(TableInfo ti:map.values()) {
			JavaFileUtils.creatJavaPoFile(ti, new MySqlTypeConvertor());			
		}
	}
	
	/**
	 * 加载po包下面的类
	 */
	public static void loadPoFile() {
		
		for(TableInfo table:tables.values()) {
			//Class.forName(sorm.bean.Query)
			try {
				Class c = Class.forName(DBManager.getConf().getPoPackage()+"."+
						StringUtils.firstChar2UpperCase(table.getTname()));
				poClassTableMap.put(c, table);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
	}

	public static Map<String, TableInfo> getTableInfos() {
		return tables;
	}
	
	
}
