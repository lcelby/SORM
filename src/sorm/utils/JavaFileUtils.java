package sorm.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sorm.bean.ColumnInfo;
import sorm.bean.JavaFieldGetSet;
import sorm.bean.TableInfo;
import sorm.core.DBManager;
import sorm.core.MySqlTypeConvertor;
import sorm.core.TableContext;
import sorm.core.TypeConvertor;

/**
 * 封装了java类源代码操作所需要的工具类
 * @author lcelby
 *
 */
public class JavaFileUtils {
	/**
	 * ͨ通过数据库字段信息生成属性及相应的方法varchar username--->private String username和相应的setget方法
	 * @param column 字段信息类
	 * @param convertor 类型转换器
	 * @return java类属性及setget方法的源代码
	 */
	public static JavaFieldGetSet creatJavaFieldGetSet(ColumnInfo column, TypeConvertor convertor) {
		JavaFieldGetSet f = new JavaFieldGetSet();
		
		//private String username;
		String javaFieldType = convertor.dataType2JavaType(column.getDataType());
		f.setFieldInfo("\tprivate "+javaFieldType+" "+column.getName()+";\n");
		
		//public String getUsername(){return username;}
		StringBuilder getSrc = new StringBuilder();
		getSrc.append("\tpublic "+javaFieldType+" get"+StringUtils.firstChar2UpperCase(column.getName()+"(){\n"));
		getSrc.append("\t\treturn "+column.getName()+";\n");
		getSrc.append("\t}\n");
		f.setGetInfo(getSrc.toString());
		
		//public void setUsername(String username){this.username=username;}
		StringBuilder setSrc = new StringBuilder();
		setSrc.append("\tpublic void set"+StringUtils.firstChar2UpperCase(column.getName()+"("
				+javaFieldType+" "+column.getName()+"){\n"));
		setSrc.append("\t\tthis."+column.getName()+"="+column.getName()+";\n");
		setSrc.append("\t}\n");
		f.setSetInfo(setSrc.toString());
		
		return f;
	}
	
	/**
	 * 通过表信息生成相应的java源代码
	 * @param table 表信息
	 * @param convertor 类型转换器
	 * @return 返回的java类源代码
	 */
	public static String creatJavaSrc(TableInfo table, TypeConvertor convertor) {
		
		
		Map<String,ColumnInfo> columns = table.getColumns();
		List<JavaFieldGetSet> javaField= new ArrayList<JavaFieldGetSet>();
		for(ColumnInfo c:columns.values()) {
			javaField.add(JavaFileUtils.creatJavaFieldGetSet(c, convertor));
		}
		
		StringBuilder src = new StringBuilder();
		//生成package语句
		src.append("package "+DBManager.getConf().getPoPackage()+";\n\n");
		//生成import语句
		src.append("import java.sql.*;\n");
		src.append("import java.util.*;\n");
		src.append("\n\n");
		//生成类声明语句
		src.append("public class "+StringUtils.firstChar2UpperCase(table.getTname())+"{\n");
		//生成属性声明语句
		for(JavaFieldGetSet f:javaField) {
			src.append(f.getFieldInfo());
		}
		//生成get语句
		for(JavaFieldGetSet f:javaField) {
			src.append(f.getGetInfo());
		}
		//生成set语句
		for(JavaFieldGetSet f:javaField) {
			src.append(f.getSetInfo());
		}
		//生成类结束符
		src.append("}\n");		
		return src.toString();
	}
	
	public static void creatJavaPoFile(TableInfo table, TypeConvertor convertor) {
		String srcPath = DBManager.getConf().getSrcPath();
		String poPackage = DBManager.getConf().getPoPackage().replaceAll("\\.", "/");
		String filePath = srcPath+"\\"+poPackage;

		File f = new File(filePath);
		if(!f.exists()) {
			f.mkdirs();
		}		
		
		String src = creatJavaSrc(table, convertor);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(f.getAbsolutePath()+"/"+
					StringUtils.firstChar2UpperCase(table.getTname())+".java")));
			bw.write(src);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
/*	public static void main(String[] args) {
		
		Map<String,TableInfo> tables = TableContext.tables;
		TableInfo t = tables.get("emp");
		JavaFileUtils.creatJavaPoFile(t, new MySqlTypeConvertor());
		
	}*/
}

