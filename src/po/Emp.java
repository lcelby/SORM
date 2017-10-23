package po;

import java.sql.*;
import java.util.*;


public class Emp{
	private String name;
	private Integer id;
	private String dept;
	private Double salary;
	private Integer age;
	public String getName(){
		return name;
	}
	public Integer getId(){
		return id;
	}
	public String getDept(){
		return dept;
	}
	public Double getSalary(){
		return salary;
	}
	public Integer getAge(){
		return age;
	}
	public void setName(String name){
		this.name=name;
	}
	public void setId(Integer id){
		this.id=id;
	}
	public void setDept(String dept){
		this.dept=dept;
	}
	public void setSalary(Double salary){
		this.salary=salary;
	}
	public void setAge(Integer age){
		this.age=age;
	}
}
