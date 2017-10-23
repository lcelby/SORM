package sorm.bean;

/**
 * 封装配置信息的类
 * @author lcelby
 *
 */
public class Configuration {
	/**
	 *驱动类
	 */
	private String driver;
	/**
	 *JDBC的url
	 */
	private String url;
	/**
	 *数据库用户名
	 */
	private String user;
	/**
	 * 数据库密码
	 */
	private String pwd;
	/**
	 *正在使用的数据库连接
	 */
	private String usingDB;
	/**
	 *项目的源码路径
	 */
	private String srcPath;
	/**
	 * 存储java类的包名（persistent object持久化对象）
	 */
	private String poPackage;
	/**
	 * 要使用的query类
	 */
	private String queryClass;
	/**
	 * 连接池里存放连接的最大值
	 */
	private Integer poolMaxNum;
	/**
	 * 连接池里存放连接的最小值
	 */
	private Integer poolMinNum;
	
	
	public Configuration() {
	}
	
	
	public Configuration(String driver, String url, String user, String pwd, String usingDB, String srcPath,
			String poPackage,String queryClass) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.pwd = pwd;
		this.usingDB = usingDB;
		this.srcPath = srcPath;
		this.poPackage = poPackage;
		this.queryClass = queryClass;
	}
	
	
	
	public String getQueryClass() {
		return queryClass;
	}


	public void setQueryClass(String queryClass) {
		this.queryClass = queryClass;
	}


	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUsingDB() {
		return usingDB;
	}
	public void setUsingDB(String usingDB) {
		this.usingDB = usingDB;
	}
	public String getSrcPath() {
		return srcPath;
	}
	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}
	public String getPoPackage() {
		return poPackage;
	}
	public void setPoPackage(String poPackage) {
		this.poPackage = poPackage;
	}


	public Integer getPoolMaxNum() {
		return poolMaxNum;
	}


	public void setPoolMaxNum(Integer poolMaxNum) {
		this.poolMaxNum = poolMaxNum;
	}


	public Integer getPoolMinNum() {
		return poolMinNum;
	}


	public void setPoolMinNum(Integer poolMinNum) {
		this.poolMinNum = poolMinNum;
	}
	
	
}
