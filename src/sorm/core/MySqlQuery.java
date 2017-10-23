package sorm.core;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import po.Emp;
import sorm.bean.ColumnInfo;
import sorm.bean.TableInfo;
import sorm.utils.JDBCUtils;
import sorm.utils.ReflectUtils;



/**
 * 负责MySql数据库的查询方法
 * @author lcelby
 *
 */
public class MySqlQuery extends Query{
	
	public static void testDML() {
		Emp e =new Emp();
		e.setId(2);
		e.setName("李四");
		e.setAge(24);
		e.setSalary(12000.5);
		//new MySqlQuery().delete(e);
		//new MySqlQuery().insert(e);
		new MySqlQuery().update(e, new String[] {"name","age","salary"});
	}
	
	public static void main(String[] args) {
		
		Query q = QueryFactory.creatQuery();
		Emp e = (Emp) q.queryById(Emp.class, 2);
		System.out.println(e.getName()+"--"+e.getAge());
		
		q.delete(Emp.class, 4);
		
//		List<Emp> list = new MySqlQuery().queryRows("select id,name,age,salary from emp where age>? and salary<?", 
//				Emp.class, new Object[] {14,1800});
//		for(Emp e:list) {
//			System.out.println(e.getName());
//		}
		
//		Number n= new MySqlQuery().queryNumber("select count(*) from emp where age>?", new Object[] {17});
//		System.out.println(n);
	}

	@Override
	public Object queryPagenatw(int pageNum, int size) {
		return null;
	}
		
		


}
