package sorm.utils;

import java.sql.PreparedStatement;
import java.sql.SQLException;



/**
 * 封装JDBC相关的工具类
 * @author lcelby
 *
 */
public class JDBCUtils {
	
	/**
	 * 给sql语句设参
	 * @param ps 预编译的sql语句对象
	 * @param params 参数
	 */
	public static void handleParams(PreparedStatement ps,Object[] params) {		
		if(params!=null) {
			for(int i=0;i<params.length;i++) {
				try {
					ps.setObject(i+1, params[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
