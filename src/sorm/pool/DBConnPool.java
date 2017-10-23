package sorm.pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import sorm.core.DBManager;

/**
 * 连接池相关的类
 * @author lcelby
 *
 */
public class DBConnPool {
	/**
	 * 连接池
	 */
	private List<Connection> pool;
	/**
	 * 连接池里存放连接的最大值
	 */
	private static int poolMaxNum;
	/**
	 * 连接池里存放连接的最小值
	 */
	private static int poolMinNum;
	/**
	 * 初始化连接池
	 */
	private void initPool() {
		poolMaxNum = DBManager.getConf().getPoolMaxNum();
		poolMinNum = DBManager.getConf().getPoolMinNum();
		
		if(pool==null) {
			pool = new ArrayList<Connection>();
		}
		while(pool.size()<poolMinNum) {
			pool.add(DBManager.creatConn());
		}
	}
	
	public DBConnPool() {
		initPool();
	}
	
	
	/**
	 * 从连接池中获取连接对象
	 * @return 获取到的Connection对象
	 */
	public synchronized Connection getConn() {
		Connection c = null;
		if(pool.size()>0) {
			int connIndex = pool.size()-1;
			c = pool.get(connIndex);
			pool.remove(connIndex);
			return c;			
		}else {
			c = DBManager.creatConn();			
			return c;
		}
	}
	/**
	 * 将使用完的连接放入连接池
	 * @param conn 要关闭的连接
	 */
	public synchronized  void close(Connection conn) {
		if(pool.size()==poolMaxNum) {
			try {
				if(conn!=null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			pool.add(conn);			
		}
	}
	

}
