package com.limn.parameter;public class Parameter {	//补丁更新地址	public static String UPDATEPATH = "";		//服务器IP	public static String SERVERIP = "";		//运行环境	public static String YIGOPATH = null;		//测试用例路径	public static String TESTCASEPATH = null;		//测试地址	public static String URL = null;		//平台版本	public static String PLATVERSION = null;		//浏览器类型	public static String BROWSERTYPE = null;		//执行模式	public static String EXECUTEMODE = null;		//运行模式 远程 还是 本地	public static String RUNMODE = null;	public static String REMOTEIP = null;		//测试版本	public static String VERSION = "";		//测试模块名称	public static String TESTNAME = null;		//测试结果目录	public static String RESULT_FOLDER = null;	public static String RESULT_FOLDER_BITMAP = null;	public static String RESULT_FOLDER_DATABAK = null;	public static String RESULT_FOLDER_LOG = null;	public static String RESULT_FOLDER_SQL = null;	public static String RESULT_FOLDER_RESULTTXT = null;	public static String RESULT_FOLDER_WEB = null;	/**	 * 错误信息文件	 */	public static String ERRORFILE = null;	//数据库相关信息	/**	 * core文件数据库连接方式（jdbc、dhcp）	 */	public static String CONNTYPE = null;	/**	 * core文件数据库类型dbtype	 */	public static int DBTYPE = -1;	/**	 * core文件里数据库链接字符串	 */	public static String DBURL = null;	/**	 * core文件数据库链接驱动	 */	public static String DBDRIVER = null;	/**	 * core文件数据库用户名	 */	public static String DBUSER = null;	/**	 * core文件数据库密码	 */	public static String DBPASS = null;		//中间件路径	public static String MIDDLEWARE = null;	public static String DEFAULT_TESTCASE_PATH = System.getProperty("user.dir") + "\\testcase";		public static String DEFAULT_CONF_MODULE_PATH = System.getProperty("user.dir") + "\\conf_module";		public static String DEFAULT_TEMP_PATH = System.getProperty("user.dir") + "\\temp";		public static String DEFAULT_CONF_PATH = System.getProperty("user.dir") + "\\conf";		public static String DEFAULT_BIN_PATH = System.getProperty("user.dir") + "\\bin";	/**	 * 环境路径	 */	public static String DFAULT_TEST_PATH = System.getProperty("user.dir");	/**	 * tmp路径	 */	public static String TMP = System.getProperty("java.io.tmpdir") + "\\Selenium_Yigo";			/**	 * 调试模式	 */	public static boolean DEBUGMODE = false;		/**	 * OS = "Windows"	 * OS = "Linux"	 */	public static String OS = null;		/**	 * RemoteDriver 的控制端口	 */	public static int REMOTEPORT = 38942;	/**	 * Server 的控制端口	 */	public static int SERVERPORT = 25041;			public static boolean isRemoteRun = false;		/**	 * 是否存在服务端环境	 */	public static boolean NOTSERVER = false;		/**	 * 如果是查询界面,数据另记录  用于核对	 */	public static boolean ReportWhereBoolean = false;		/**	 * 单据的类型	 */	public static String BILLTYPE = "";		/**	 * 用例文件名	 */	public static String EXCELNAME = "";		/**	 * 是否为报表	 */	public static boolean ISVERIBILL = false;	}