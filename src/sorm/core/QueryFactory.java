package sorm.core;
/**
 * Query类的工厂类
 * @author lcelby
 *
 */
public class QueryFactory {
	/**
	 * 原型对象
	 */
	private static Query prototypeObj;
	
	private QueryFactory() {		
	}
	
	static {
		try {
			Class c = Class.forName(DBManager.getConf().getQueryClass());
			prototypeObj = (Query) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建Query对象
	 * @return 返回创建的Query对象
	 */
	public static Query creatQuery() {
		try {
			return (Query) prototypeObj.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
