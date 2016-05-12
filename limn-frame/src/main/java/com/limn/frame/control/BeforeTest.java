package com.limn.frame.control;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import com.limn.tool.exception.ParameterException;
import com.limn.tool.external.XMLReader;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.tool.log.LogInformation;
import com.limn.tool.log.RunLog;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.common.Common;
import com.limn.tool.common.DateFormat;
import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.regexp.RegExp;

/**
 * 
 * 所有的测试都从这里起步
 * 
 * @author limn
 * 
 */
public class BeforeTest implements Runnable {

	// 测试属性
	// private HashMap<String, String> testParameter = null;
	private StartConfigBean startConfig = null;
	// private boolean update = true;

	private KeyWordDriver keyWordDriver = null;

	private boolean flag = false;

	private Socket socket = null;

	private XMLReader xmlReader = null;

	// private String lastVersion = null;

	private boolean isLoop = false;

	// 本地化的RunLog
	public BeforeTest(HashMap<String, String> map, KeyWordDriver kwd, boolean isLoop) {
		this.isLoop = isLoop;
		// testParameter = map;
		startConfig = getStartConfigByMap(map);
		keyWordDriver = kwd;
		// update = false;
		beforeTest();
	}

	// 连接服务器 RunLog
	public BeforeTest(HashMap<String, String> map, Socket socket, KeyWordDriver kwd, boolean isLoop) {
		this.isLoop = isLoop;
		startConfig = getStartConfigByMap(map);
		keyWordDriver = kwd;
		this.socket = socket;
		beforeTest();
	}

	/**
	 * 转换
	 * 
	 * @param map
	 * @return
	 */
	private StartConfigBean getStartConfigByMap(HashMap<String, String> map) {
		StartConfigBean scb = new StartConfigBean();
		scb.setAppFilePath(map.get("AppFilePath"));
		scb.setBrowserType(map.get("BrowserType"));
		scb.setComputer(map.get("Computer"));
		scb.setDeBug(Boolean.valueOf(map.get("Debug")));
		scb.setExcelPath(map.get("ExcelPath"));
		scb.setExecuteMode(map.get("ExecuteMode"));
		scb.setFrontSteps(map.get("FrontSteps") == "需要" ? true : false);
		scb.setInitDB(map.get("InitDB") == "需要" ? true : false);
		scb.setIP(map.get("IP"));
		scb.setNotServer(Boolean.valueOf(map.get("NotServer")));
		scb.setRunTestModel(map.get("RunTestModel"));
		scb.setSheetsNum(map.get("SheetsNum"));
		scb.setSpecify(map.get("Specify"));
		scb.setSpecifyRow(map.get("SpecifyRow"));
		scb.setSpecifySheet(map.get("SpecifySheet"));
		scb.setSpecifyStep(map.get("SpecifyStep"));
		scb.setSqlData(map.get("SqlData"));
		scb.setStartPlatform(map.get("StartPlatform"));
		scb.setUploadResults(Boolean.valueOf(map.get("UploadResults")));
		scb.setURL(map.get("URL"));

		return scb;
	}

	// 本地化的RunLog
	public BeforeTest(StartConfigBean scb, KeyWordDriver kwd, boolean isLoop) {
		this.isLoop = isLoop;
		startConfig = scb;
		keyWordDriver = kwd;
		// update = false;
		beforeTest();
	}

	// 连接服务器 RunLog
	public BeforeTest(StartConfigBean scb, Socket socket, KeyWordDriver kwd, boolean isLoop) {
		this.isLoop = isLoop;
		startConfig = scb;
		keyWordDriver = kwd;
		this.socket = socket;
		beforeTest();
	}

	private void beforeTest() {
		Print.log("***************开始分割线***************", 4);
		Print.log("用例执行开始", 4);
		initParameter();
		if (Parameter.DEBUGMODE) {
			// TODO 这里设置关键字驱动 和 Panel
			// new RunLog(new DebugEditFrame());
		} else if (!RunLog.isStart()) {
			if (socket != null) {
				new RunLog(socket);
			} else {
				new RunLog();
			}
		}
		try {
			init();
		} catch (ParameterException e) {
			// TODO 初始化失败 需要处理
			e.printStackTrace();
		}
	}

	public BeforeTest(XMLReader xmlReader, Socket socket) {
		this.xmlReader = xmlReader;
		this.socket = socket;
		flag = true;
	}

	/**
	 * 初始化
	 * 
	 * @throws ParameterException
	 */
	private void init() throws ParameterException {

		// if(!Parameter.NOTSERVER){
		// lastVersion = Conf.getLatestVersion();
		// initData();
		// }else{
		// Parameter.VERSION = "unKnow";
		// }
		// TODO 版本信息
		// Parameter.VERSION = ?
		createResultFolder();

	}

	public String checkConfig(StartConfigBean scb){
		
		if(scb.getRunTestModel().equalsIgnoreCase("浏览器")){
			scb.getBrowserType();
			
			
			
		}else if(scb.getRunTestModel().equalsIgnoreCase("IOS") || 
				scb.getRunTestModel().equalsIgnoreCase("Android")){
		
			
		}
		
		return null;

	}
	

	private void initData() throws ParameterException{
		if(startConfig.getInitDB()){
//			dropAllTable();
			String sqlPath = startConfig.getSqlData();
			if(sqlPath != ""){
				//TODO sql文件的执行
//				ExecuteSQL.execute(sqlPath);
			}
		}
	}

	/**
	 * 清空数据库
	 */
	// private void dropAllTable(){
	// String tableName = null;
	// String tableView = null;
	// switch(Parameter.DBTYPE){
	// case 0:
	// tableName = "select name from sysibm.systables where type='T' and
	// creator='"+ Parameter.DBUSER +"'";
	// tableView = "select name from sysibm.systables where type='V' and
	// creator='"+ Parameter.DBUSER +"'";
	// break;
	// case 1:
	// tableName = "select table_name from user_tables";
	// tableView = "select view_name from user_views";
	// break;
	// case 2:
	// tableName = "Select name from sysobjects where xtype='U'";
	// tableView = "Select name from sysobjects where xtype='V'";
	// break;
	// case 3:
	// break;
	// }
	// int results = 0;
	// do{
	// results = 0;
	// String[][] res = DataBase.executeSQL(tableName);
	// for(String[] tables:res){
	// System.out.println(tables[0]);
	// if(DataBase.executeSingleSQL("drop table " + tables[0])==-1){
	// results = -1;
	// }
	// }
	// }while(results==-1);
	//
	// do{
	// results = 0;
	// String[][] resView = DataBase.executeSQL(tableView);
	// for(String[] tables:resView){
	// System.out.println(tables[0]);
	// if(DataBase.executeSingleSQL("drop view " + tables[0])==-1){
	// results = -1;
	// }
	// }
	// }while(results==-1);
	//
	// }

	/**
	 * 设置全局的变量参数
	 */
	private void initParameter() {

		// if(System.getProperties().getProperty("os.name").contains("Windows")){
		// Parameter.OS = "Windows";
		// }else{
		// Parameter.OS = "Linux";
		// }

		// Parameter.YIGOPATH = testParameter.get("Yigo");

		Parameter.TESTCASEPATH = startConfig.getExcelPath();
		if (!Common.isAbsolutePath(Parameter.TESTCASEPATH)) {
			Parameter.TESTCASEPATH = Parameter.DFAULT_TEST_PATH + "/testcase/" + Parameter.TESTCASEPATH;
		}

		// Parameter.TESTNAME = new File(Parameter.TESTCASEPATH).getName();

		// Parameter.TESTNAME = Parameter.TESTNAME.replaceAll("[.][^.]+$", "");
		if (new File(Parameter.TESTCASEPATH).isDirectory()) {
			Parameter.TESTCASEPATH = FileUtil.findFileByType(Parameter.TESTCASEPATH, "xls");
		} else {

		}
		Parameter.TESTCASE_FOLDERPATH = FileUtil.getParent(Parameter.TESTCASEPATH);
		Parameter.PRODUCTNAME = FileUtil.getParent(Parameter.TESTCASE_FOLDERPATH);
		Parameter.PRODUCTNAME = FileUtil.getName(Parameter.PRODUCTNAME);
		if (Parameter.PRODUCTNAME.equals("testcase")) {
			Parameter.PRODUCTNAME = "";
		}
		Parameter.TESTNAME = FileUtil.getName(Parameter.TESTCASEPATH);
		Parameter.TESTNAME = Parameter.TESTNAME.substring(0, Parameter.TESTNAME.lastIndexOf("."));

		// TODO

		Parameter.URL = startConfig.getURL();
		// Parameter.PLATVERSION = testParameter.get("Version");

		if (startConfig.getBrowserType().equalsIgnoreCase("Chrome")) {
			Parameter.BROWSERTYPE = 2;
		} else if (startConfig.getBrowserType().equalsIgnoreCase("IE")) {
			Parameter.BROWSERTYPE = 3;
		} else {
			Parameter.BROWSERTYPE = 1;
		}

		Parameter.EXECUTEMODE = startConfig.getExecuteMode();
		Parameter.RUNMODE = startConfig.getComputer();

		Parameter.REMOTEIP = startConfig.getIP();
		// Parameter.MIDDLEWARE = testParameter.get("Middleware");

		// if(testParameter.containsKey("NotServer")){
		Parameter.NOTSERVER = startConfig.isNotServer();
		// }else{
		// Parameter.NOTSERVER = false;
		// }

		// if(testParameter.containsKey("Debug")){
		Parameter.DEBUGMODE = startConfig.isDebug();
		// }else{
		// Parameter.DEBUGMODE = false;
		// }
		// if (Parameter.PLATVERSION.equals("1.4")){
		// Parameter.UPDATEPATH = Conf.getConfByElement("update_path_1_4");
		// }else if(Parameter.PLATVERSION.equals("1.6")){
		// Parameter.UPDATEPATH = Conf.getConfByElement("update_path_1_6");
		// }

		if (!Parameter.NOTSERVER) {
			// TODO 数据相关
			// new DataBaseParameter(); // 数据库全局变量
		}

	}

	private void createResultFolder() {
		String resultPath = null;
		if (startConfig.getUploadResults()) {
			// TODO 上传服务器相关的
			// resultPath = Variable.resolve("[UploadServer_Result_Path]");
		} else {
			resultPath = System.getProperty("user.dir") + "/ResultsFolder";
		}

		// 生成结果目录
		// File resultFolder = new File(resultPath + "/" + Parameter.VERSION
		// + "/" + Parameter.TESTNAME + "/" +
		// DateFormat.getDate("yyyyMMdd_HHmmss"));
		if (!Parameter.PRODUCTNAME.equals("")) {
			resultPath = resultPath + "/" + Parameter.PRODUCTNAME;
		}
		File resultFolder = new File(
				resultPath + "/" + Parameter.TESTNAME + "/" + DateFormat.getDate("yyyyMMdd_HHmmss"));
		Print.log("测试结果成目录:" + resultFolder.getAbsolutePath(), 4);
		resultFolder.mkdirs();
		Parameter.RESULT_FOLDER = resultFolder.getAbsolutePath();
		Parameter.RESULT_FOLDER_BITMAP = Parameter.RESULT_FOLDER + "/BitMap";
		Parameter.RESULT_FOLDER_DATABAK = Parameter.RESULT_FOLDER + "/DataBak";
		Parameter.RESULT_FOLDER_LOG = Parameter.RESULT_FOLDER + "/log";
		Parameter.RESULT_FOLDER_RESULTTXT = Parameter.RESULT_FOLDER + "/ResultTxt";
		Parameter.RESULT_FOLDER_SQL = Parameter.RESULT_FOLDER + "/SQL";
		Parameter.RESULT_FOLDER_WEB = Parameter.RESULT_FOLDER + "/WEB";
		Parameter.ERRORFILE = Parameter.RESULT_FOLDER + "/ResultTxt";

		new File(Parameter.RESULT_FOLDER_BITMAP).mkdir();
		new File(Parameter.RESULT_FOLDER_DATABAK).mkdir();
		new File(Parameter.RESULT_FOLDER_LOG).mkdir();
		new File(Parameter.RESULT_FOLDER_RESULTTXT).mkdir();
		new File(Parameter.RESULT_FOLDER_SQL).mkdir();
		new File(Parameter.RESULT_FOLDER_WEB).mkdir();
		new File(Parameter.ERRORFILE).mkdir();

		LogInformation.init(Parameter.RESULT_FOLDER_LOG + "/runlog.log");

	}

	// 运行测试
	@Override
	public void run() {
		if (flag) {

			if (keyWordDriver == null) {
				Print.log("关键字驱动没有加载无法运行,请先执行setKeyWordDriver方法", 2);
				return;
			}

			for (int i = 0; i < xmlReader.getTemplateCount(); i++) {
				HashMap<String, String> templateMap = xmlReader.getNodeValueByTemplateIndex(i);
				// testParameter = templateMap;
				startConfig = getStartConfigByMap(templateMap);
				beforeTest();
				new Test(startConfig, keyWordDriver);
			}
		} else {
			new Test(startConfig, keyWordDriver);
		}
		if (isLoop) {
			new Thread(new BeforeTest(startConfig, keyWordDriver, isLoop)).start();
		}

	}

}
