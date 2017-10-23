package sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

import sorm.bean.ColumnInfo;
import sorm.bean.TableInfo;
import sorm.utils.JDBCUtils;
import sorm.utils.ReflectUtils;

/**
 * 查询核心类，负责对外提供服务
 * @author lcelby
 *
 */
@SuppressWarnings("all")
public abstract class Query implements Cloneable{
	
	/**
	 * 采用模板方法将JDBC操作封装成模板，便于重用
	 * @param sql 要执行的sql语句
	 * @param clazz 记录要封装到的bean类
	 * @param params sql参数
	 * @param back CallBack的实现类，实现回调
	 * @return 查询结果
	 */
	public Object executeQueryTemplate(String sql,Class clazz, Object[] params,CallBack back) {
		Connection conn = DBManager.getConn();
		PreparedStatement ps =null;
		ResultSet rs = null;		
		try {
			ps = conn.prepareStatement(sql);
			//给sql语句设参
			JDBCUtils.handleParams(ps, params);
			System.out.println(ps);
			rs = ps.executeQuery();
			return back.doExecute(conn, ps, rs);
						
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			DBManager.close(rs, ps, conn);
		}	
		
	}
	
	
	
	/**
     * 直接执行DML语句
     * @param sql sql语句
     * @param params 参数
     * @return ִ执行sql语句所影响的记录的行数
     */
    public int executeDML(String sql, Object[] params) {
    	Connection conn = DBManager.getConn();
		int count =0;
		PreparedStatement ps =null;
		//给sql语句设参
		try {
			ps = conn.prepareStatement(sql);
			JDBCUtils.handleParams(ps, params);
			System.out.println(ps);
			count = ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBManager.close(ps, conn);
		}
		return count;
    }

    /**
     * 往数据库中插入对象
     * @param obj 要插入的对象
     */
    public void insert(Object obj) {
    	Class c= obj.getClass();
		TableInfo table = TableContext.poClassTableMap.get(c);
		
		//insert into 表名 (id,username,age) values(?,?,?)
		StringBuilder sql = new StringBuilder();
		sql.append("insert into "+table.getTname()+" (");
		int notNullField = 0;
		List<Object> params = new ArrayList<Object>();
		Field[] fields = c.getDeclaredFields();
		for(Field f:fields) {
			String fieldName = f.getName();
			Object fieldValue = ReflectUtils.invokeGet(fieldName, obj);
			if(fieldValue!=null) {
				sql.append(fieldName+",");
				notNullField++;
				params.add(fieldValue);
			}
		}
		sql.setCharAt(sql.length()-1, ')');
		sql.append(" values(");
		for(int i=0;i<notNullField;i++) {
			sql.append("?,");
		}
		sql.setCharAt(sql.length()-1, ')');
		executeDML(sql.toString(),params.toArray());
		
    }

    /**
     * 删除Class对象对应的表中记录（通过主键值id寻找记录）
     * @param clazz 跟表对应的类的class对象
     * @param id 主键的值
     */
    public void delete(Class clazz, Object id) { // delete from User where id = 2;
    	//根据class找到TableInfo
		TableInfo table = TableContext.poClassTableMap.get(clazz);	
		//获取主键
		ColumnInfo onlyPriKey = table.getOnlyPriKey();
		//delete from emp where id=?
		String sql = "delete from "+table.getTname()+" where "+onlyPriKey.getName()+"=? " ;
		executeDML(sql,new Object[]{id});
    }

    /**
     * 删除对象对应的表中的记录
     * @param obj 要删除的对象
    */
    public void delete(Object obj) {
    	Class c = obj.getClass();
		TableInfo table = TableContext.poClassTableMap.get(c);
		ColumnInfo onlyPrikey = table.getOnlyPriKey();
		Object onlyPriKeyValue = ReflectUtils.invokeGet(onlyPrikey.getName(), obj);
		delete(c, onlyPriKeyValue);
    }
    
    /**
     * 更新指定对象的指定字段的内容
     * @param obj 要更新的对象
     * @param fieldNames 要更新的属性
     * @return ִ执行sql语句所影响的行数
     */
    public int update(Object obj, String[] fieldNames) {//update user set uname=?,pwd=?;
    	// update 表名 set username=?,age=? where id=?
		Class c = obj.getClass();
		TableInfo table = TableContext.poClassTableMap.get(c);
		ColumnInfo priKey = table.getOnlyPriKey();
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("update "+table.getTname()+" set ");
		
		for(String fName: fieldNames) {
			Object fValue = ReflectUtils.invokeGet(fName, obj);
			sql.append(fName+"=?,");
			params.add(fValue);
		}
		sql.setCharAt(sql.length()-1, ' ');
		sql.append("where "+priKey.getName()+"=? ");
		params.add(ReflectUtils.invokeGet(priKey.getName(), obj));
		
		return executeDML(sql.toString(), params.toArray());
    }
    
    
    /**
     * 查询返回多条记录（多行多列）
     * @param sql 查询的sql语句
     * @param clazz 要查询的表对应的类的class对象
     * @param params sql语句参数
     * @return 查询结果
     */
    public List queryRows(String sql,Class clazz, Object[] params) {
    	
		return (List) executeQueryTemplate(sql, clazz, params, new CallBack() {
			
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				//select id,username,age from emp where age>?
				List list = null;
				try {
					ResultSetMetaData rsMetaData = rs.getMetaData();
					//多行
					while(rs.next()) {
						if(list==null) {
							list = new ArrayList();
						}
						Object rowObj = clazz.newInstance();
						//多列
						for(int i=0; i<rsMetaData.getColumnCount();i++) {
							String columnName = rsMetaData.getColumnLabel(i+1);
							Object columnValue = rs.getObject(i+1);
							ReflectUtils.invokeSet(rowObj, columnName, columnValue);
						}
						list.add(rowObj);
					}
				} catch (Exception e) {
					e.printStackTrace();
			
				}			
				return list;
			}
		});
    }
    
    /**
     * 查询返回一条记录（一行多列）
     * @param sql 查询的sql语句
     * @param clazz 要查询的表对应的类的class对象
     * @param params sql语句参数
     * @return 查询结果
     */
    public Object queryUniqueRow(String sql,Class clazz, Object[] params) {
    	List list = queryRows(sql, clazz, params);
		return (list!=null&&list.size()>0)?list.get(0):null;
    }
    
    /**
     * 根据主键的值和class对象查找对应的对象
     * @param clazz 
     * @param id
     * @return
     */
    public Object queryById(Class clazz, Object id) {
    	//根据class找到TableInfo
		TableInfo table = TableContext.poClassTableMap.get(clazz);	
		//获取主键
		ColumnInfo onlyPriKey = table.getOnlyPriKey();
		//select * from emp where id=?
		String sql = "select * from "+table.getTname()+" where "+onlyPriKey.getName()+"=? " ;
		return queryUniqueRow(sql, clazz, new Object[] {id});
    }
    
    /**
     * 查询返回一个值
     * @param sql 查询的sql语句
     * @param params sql语句参数
     * @return 查询的值
     */
    public Object queryValue(String sql,Object[] params) {
    	
    	return executeQueryTemplate(sql, null, params, new CallBack() {
			
			@Override
			public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
				Object value = null;
				try {
					while(rs.next()) {
						value = rs.getObject(1);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return value;
			}
		});    	
    }
    
    /**
     * 查询返回一个数字
     * @param sql 查询的sql语句
     * @param params sql语句参数
     * @return 查询的值
     */
    public Number queryNumber(String sql,Object[] params) {
    	return (Number)queryValue(sql, params);
    }
    
    /**
     * 分页查询
     * @param pageNum 第几页数据
     * @param size 每页显示多少条记录
     * @return 查询结果
     */
    public abstract Object queryPagenatw(int pageNum, int size);
    	
    @Override
    protected Object clone() throws CloneNotSupportedException {
    	return super.clone();
    }
}
