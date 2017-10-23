package sorm.core;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import sorm.bean.Configuration;
import sorm.pool.DBConnPool;


/**
 * 根据配置信息，维持连接对象的管理（增加连接池功能）
 * @author lcelby
 *
 */
public class DBManager {
	/**
	 * 配置信息类
	 */
	private static Configuration conf;
	private static DBConnPool pool;
	
	static {//静态代码块
		Properties pros =new Properties();		
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		conf = new Configuration();
		conf.setDriver(pros.getProperty("driver"));
		conf.setUrl(pros.getProperty("url"));
		conf.setUser(pros.getProperty("user"));
		conf.setPwd(pros.getProperty("pwd"));
		conf.setUsingDB(pros.getProperty("usingDB"));
		conf.setSrcPath(pros.getProperty("srcPath"));
		conf.setPoPackage(pros.getProperty("poPackage"));
		conf.setQueryClass(pros.getProperty("queryClass"));
		conf.setPoolMaxNum(Integer.parseInt(pros.getProperty("poolMaxNum")));
		conf.setPoolMinNum(Integer.parseInt(pros.getProperty("poolMinNum")));
	}
	
	
	/**
	 * 获取配置信息类
	 * @return 
	 */
	public static Configuration getConf() {
		return conf;
	}
	
	/**
	 * 获取Connection连接对象
	 * @return 
	 */
	public static Connection getConn(){
		if(pool==null) {
			pool = new DBConnPool();
		}
		return pool.getConn();
	}
	/**
	 * 创建新的Connection连接对象
	 * @return
	 */
	public static Connection creatConn(){
		try {
			Class.forName(DBManager.getConf().getDriver());
			return DriverManager.getConnection(DBManager.getConf().getUrl(), 
					DBManager.getConf().getUser(), DBManager.getConf().getPwd());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if(stmt!=null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
			pool.close(conn);
		
		
	}
	public static void close( Statement stmt, Connection conn) {
		try {
			if(stmt!=null) {
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.close(conn);
		
	}
	public static void close(Connection conn) {
		pool.close(conn);
		
	}
	
}
