package com.limn.tool.parameter;

import com.limn.tool.common.GetSystemInfo;
import com.limn.tool.regexp.RegExp;

public class Parameter {

	//测试用例路径
//	public static String TESTCASEPATH = null;

//	public static String TESTCASE_FOLDERPATH = null;

	//测试地址
//	public static String URL = "";

	//平台版本
//	public static String PLATVERSION = "";

	//浏览器类型
//	public static int BROWSERTYPE = 1;

	//执行模式
//	public static String EXECUTEMODE = null;

	//运行模式 远程 还是 本地
//	public static String RUNMODE = null;
//	public static String REMOTEIP = null;

	//测试版本
//	public static String VERSION = "";

	//测试模块名称
//	public static String TESTNAME = null;

	//测试结果目录
//	public static String RESULT_FOLDER = null;
//	public static String RESULT_FOLDER_BITMAP = null;
//	public static String RESULT_FOLDER_DATABAK = null;
//	public static String RESULT_FOLDER_LOG = null;
//	public static String RESULT_FOLDER_SQL = null;
//	public static String RESULT_FOLDER_RESULTTXT = null;
//	public static String RESULT_FOLDER_WEB = null;
//	public static String RESULT_FOLDER_REPORT = null;
	/**
	 * 错误信息文件
	 */
//	public static String ERRORFILE = null;
	//数据库相关信息
	/**
	 * core文件数据库连接方式（jdbc、dhcp）
	 */
	public static String CONNTYPE = null;
	/**
	 * core文件数据库类型dbtype
	 */
	public static int DBTYPE = -1;
	/**
	 * core文件里数据库链接字符串
	 */
	public static String DBURL = null;
	/**
	 * core文件数据库链接驱动
	 */
	public static String DBDRIVER = null;
	/**
	 * core文件数据库用户名
	 */
	public static String DBUSER = null;
	/**
	 * core文件数据库密码
	 */
	public static String DBPASS = null;
//
	//中间件路径
//	public static String MIDDLEWARE = null;

	/**
	 * 环境路径
	 */
	public static String DFAULT_TEST_PATH =  System.getProperty("user.dir") ;

	public static String DEFAULT_TESTCASE_PATH = DFAULT_TEST_PATH + "/testcase";

	public static String DEFAULT_CONF_MODULE_PATH = DFAULT_TEST_PATH + "/conf_module";

	public static String DEFAULT_TEMP_PATH = DFAULT_TEST_PATH + "/temp";

	public static String DEFAULT_CONF_PATH = DFAULT_TEST_PATH + "/config";

	public static String DEFAULT_BIN_PATH = DFAULT_TEST_PATH + "/bin";

	public static String DFAULT_RESULTSFOLDER_PATH = DFAULT_TEST_PATH + "/ResultsFolder";

	/**
	 * tmp路径
	 */

	public static String TMP = System.getProperty("java.io.tmpdir") + "/auto-limn";

	/**
	 * 报告模板路径
	 */
	public static String REPORT_PATH = DFAULT_TEST_PATH + "/src/main/resources/report";

	/**
	 * 调试模式
	 */
//	public static boolean DEBUGMODE = false;

	/**
	 * OS = "Windows"
	 * OS = "Linux"
	 * OS = "Mac"
	 */
//	public static String OS = null;

	/**
	 * RemoteDriver 的控制端口
	 */
	public static int REMOTEPORT = 38942;
	/**
	 * Server 的控制端口
	 */
	public static int SERVERPORT = 25041;


//	public static boolean isRemoteRun = false;

	/**
	 * 是否存在服务端环境
	 */
//	public static boolean NOTSERVER = false;

	/**
	 * 如果是查询界面,数据另记录  用于核对
	 */
//	public static boolean ReportWhereBoolean = false;

	/**
	 * 单据的类型
	 */
//	public static String BILLTYPE = "";

	/**
	 * 用例文件名
	 */
//	public static String EXCELNAME = "";

	/**
	 * 是否为报表
	 */
//	public static boolean ISVERIBILL = false;

	/**
	 * 系统位数
	 */
	public static String SYSTEMBIT = GetSystemInfo.getBit();
	/**
	 * 远程机器IP
	 */
//	public static String CLIENTIP = "";
	/**
	 * 远程机器IP端口
	 */
//	public static int CLIENTIPPORT = -1;

	/**
	 * 产品名称
	 */
//	public static String PRODUCTNAME = "";

	/**
	 * 执行开始时间
	 */
//	public static String STARTTIME = "";

	/**
	 * 执行结束时间
	 */
//	public static String ENDTIME = "";

	/**
	 * 测试环境
	 */
//	public static String TESTENVIRONMENT = "";

	/**
	 * 全部用例执行结果
	 */
//	public static boolean OVERALLSTATUS = true;
	/**
	 * 用例编号
	 */
//	public static String TESTCASENO = "";
	/**
	 * 用例模块名
	 */
//	public static String TESTCASEMOUDLE = "";
	/**
	 * 错误信息
	 */
//	public static String ERRORLOG = "";
	/**
	 * 产品提示信息
	 */
//	public static String PRODUCTMESSAGE = "";
	/**
	 * 错误截图路径
	 */
//	public static String ERRORCAPTURE = "";
	/**
	 * 执行结果
	 */
//	public static int CASESTATUS = 0;
	/**
	 * 每一步截图的路径
	 */
//	public static String CASESNAPSHOT = "";
	/**
	 * 验证时的截图路径
	 */
//	public static String VERSNAPSHOT = "";
	/**
	 * 日志详细信息
	 */
//	public static String LOGINFO = "";
	/**
	 * 检查点名
	 */
//	public static String CHECKPOINTNAME = "";
	/**
	 * 每一步的截图路径
	 */
//	public static String LOGSNAPSHOT = "";

	public static String getOS(){
		String os = null;
		if(RegExp.findCharacters(System.getProperty("os.name"),"Mac")){
			os = "Mac";
		}else if(RegExp.findCharacters(System.getProperty("os.name"),"Linux")){
			os = "Linux";
		}else{
			os = "Windows";
		}
		return os;
	}

	public static void setDefaultTestcasePath(String path){
		DFAULT_TEST_PATH = path;
		DEFAULT_TESTCASE_PATH = DFAULT_TEST_PATH + "/testcase";
		DEFAULT_CONF_MODULE_PATH = DFAULT_TEST_PATH + "/conf_module";
		DEFAULT_TEMP_PATH = DFAULT_TEST_PATH + "/temp";
		DEFAULT_CONF_PATH = DFAULT_TEST_PATH + "/config";
		DEFAULT_BIN_PATH = DFAULT_TEST_PATH + "/bin";
		DFAULT_RESULTSFOLDER_PATH = DFAULT_TEST_PATH + "/ResultsFolder";
	}
	
}