package com.limn.frame.control;

import java.io.File;
import java.net.Socket;
import java.util.HashMap;

import com.limn.tool.exception.ParameterException;
import com.limn.tool.external.XMLReader;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.frame.results.RecordResult;
import com.limn.tool.log.LogInformation;
import com.limn.tool.log.RunLog;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;
import com.limn.tool.bean.ResultConfigBean;
import com.limn.tool.bean.StartConfigBean;
import com.limn.tool.common.Common;
import com.limn.tool.common.DateFormat;
import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;

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

	private boolean isLoop = false;

	private Test test = null;

	private boolean isStartLog = true;
	
	private ResultConfigBean rcb = new ResultConfigBean();
	
	// 本地化的RunLog
	public BeforeTest(HashMap<String, String> map, KeyWordDriver kwd, boolean isLoop, boolean isStartLog) {
		setParameter(getStartConfigByMap(map),null,kwd,isLoop,isStartLog);
//		beforeTest();
	}

	// 连接服务器 RunLog
	public BeforeTest(HashMap<String, String> map, Socket socket, KeyWordDriver kwd, boolean isLoop, boolean isStartLog) {
		setParameter(getStartConfigByMap(map),socket,kwd,isLoop,isStartLog);
//		beforeTest();
	}

	// 本地化的RunLog
	public BeforeTest(StartConfigBean scb, KeyWordDriver kwd, boolean isLoop, boolean isStartLog) {
		setParameter(scb,null,kwd,isLoop,isStartLog);
//		beforeTest();
	}

	// 连接服务器 RunLog
	public BeforeTest(StartConfigBean scb, Socket socket, KeyWordDriver kwd, boolean isLoop, boolean isStartLog) {
		setParameter(scb,socket,kwd,isLoop,isStartLog);
//		beforeTest();
	}
	
	private void setParameter(StartConfigBean scb, Socket socket, KeyWordDriver kwd, boolean isLoop, boolean isStartLog){
		this.isLoop = isLoop;
		startConfig = scb;
		keyWordDriver = kwd;
		this.socket = socket;
		this.isStartLog = isStartLog;
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

	public String checkStartConfig() {
		if (startConfig.getRunTestModel().equalsIgnoreCase("Web")) {

		} else if (startConfig.getRunTestModel().equalsIgnoreCase("Android") || startConfig.getRunTestModel().equalsIgnoreCase("IOS")) {
			if (!FileUtil.exists(startConfig.getAppFilePath())) {
				return "未找到APP存放路径:" + startConfig.getAppFilePath();
			}
		} else {
			return "无法识别的测试类型:" + startConfig.getRunTestModel();
		}

		if (!FileUtil.exists(startConfig.getExcelPath())) {
			return "未找到测试用例存放路径:" + startConfig.getExcelPath();
		}
		return null;
	}

	private void beforeTest() {
//		RunParameter.setStartPaht(startConfig);
		//RunParameter.setResultPaht();
		Print.log("***************开始分割线***************", 4);
		Print.log("用例执行开始", 4);
		initParameter();
		if (startConfig.isDebug()) {
			// TODO 这里设置关键字驱动 和 Panel
			// new RunLog(new DebugEditFrame());
		} else if (!RunLog.isStart() && isStartLog) {
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

	public String checkConfig(StartConfigBean scb) {
		if (scb.getRunTestModel().equalsIgnoreCase("浏览器")) {
			scb.getBrowserType();
		} else if (scb.getRunTestModel().equalsIgnoreCase("IOS") || scb.getRunTestModel().equalsIgnoreCase("Android")) {

		}
		return null;

	}

	/**
	 * 初始化数据
	 * 
	 * @throws ParameterException
	 */
	public void initData() throws ParameterException {
		if (startConfig.getInitDB()) {
			// dropAllTable();
			String sqlPath = startConfig.getSqlData();
			if (sqlPath != "") {
				// TODO sql文件的执行
				// ExecuteSQL.execute(sqlPath);
			}
		}
	}

	/**
	 * 设置全局的变量参数
	 */
	private void initParameter() {

		if (!Common.isAbsolutePath(startConfig.getExcelPath())) {
			startConfig.setExcelPath(Parameter.DFAULT_TEST_PATH + "/testcase/" + startConfig.getExcelPath());
		}

		if (new File(startConfig.getExcelPath()).isDirectory()) {
			startConfig.setExcelPath(FileUtil.findFileByType(startConfig.getExcelPath(), "xls"));
		} else {

		}
		
		rcb.setTestCaseFolderPath(FileUtil.getParent(startConfig.getExcelPath()));
		String productName = FileUtil.getParent(rcb.getTestCaseFolderPath());
		productName = FileUtil.getName(productName);
		if (productName.equals("testcase")) {
			productName = "";
		}

		rcb.setTestName(FileUtil.getName(startConfig.getExcelPath()));
		rcb.setTestName(rcb.getTestName().substring(0, rcb.getTestName().lastIndexOf(".")));

		// TODO

		// Parameter.PLATVERSION = testParameter.get("Version");

		// if (startConfig.getRunTestModel().equalsIgnoreCase("Web")) {
		// if (startConfig.getBrowserType().equalsIgnoreCase("Chrome")) {
		// Parameter.BROWSERTYPE = 2;
		// } else if (startConfig.getBrowserType().equalsIgnoreCase("IE")) {
		// Parameter.BROWSERTYPE = 3;
		// } else {
		// Parameter.BROWSERTYPE = 1;
		// }
		// }

		// Parameter.EXECUTEMODE = startConfig.getExecuteMode();
		// Parameter.RUNMODE = startConfig.getComputer();

		// Parameter.REMOTEIP = startConfig.getIP();
		// Parameter.MIDDLEWARE = testParameter.get("Middleware");

		// if(testParameter.containsKey("NotServer")){
		// Parameter.NOTSERVER = startConfig.isNotServer();
		// }else{
		// Parameter.NOTSERVER = false;
		// }

		// if(testParameter.containsKey("Debug")){
		// Parameter.DEBUGMODE = startConfig.isDebug();
		// }else{
		// Parameter.DEBUGMODE = false;
		// }
		// if (Parameter.PLATVERSION.equals("1.4")){
		// Parameter.UPDATEPATH = Conf.getConfByElement("update_path_1_4");
		// }else if(Parameter.PLATVERSION.equals("1.6")){
		// Parameter.UPDATEPATH = Conf.getConfByElement("update_path_1_6");
		// }

		if (!startConfig.isNotServer()) {
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
			resultPath = Parameter.DFAULT_RESULTSFOLDER_PATH;
		}
		rcb.setStartTime(DateFormat.getDateToString());

		// 生成结果目录
		// File resultFolder = new File(resultPath + "/" + Parameter.VERSION
		// + "/" + Parameter.TESTNAME + "/" +
		// DateFormat.getDate("yyyyMMdd_HHmmss"));
		if (rcb.getProductName() == null || rcb.getProductName().isEmpty()) {

		} else {
			resultPath = resultPath + "/" + rcb.getProductName();
		}
		File resultFolder = new File(resultPath + "/" + rcb.getTestName() + "/" + DateFormat.getDate("yyyyMMdd_HHmmss"));
		Print.log("测试结果成目录:" + resultFolder.getAbsolutePath(), 4);
		resultFolder.mkdirs();

		rcb.setResultFolder(resultFolder.getAbsolutePath());
		rcb.setResultFolderBitMap(rcb.getResultFolder() + "/BitMap");
		rcb.setResultFolderDataBak(rcb.getResultFolder() + "/DataBak");
		rcb.setResultFolderLog(rcb.getResultFolder() + "/log");
		rcb.setResultFolderResultTxt(rcb.getResultFolder() + "/ResultTxt");
		rcb.setResultFolderSQL(rcb.getResultFolder() + "/SQL");
		rcb.setResultFolderWeb(rcb.getResultFolder() + "/WEB");
		rcb.setResultFolderReport(rcb.getResultFolder() + "/Report");

		// Parameter.RESULT_FOLDER = resultFolder.getAbsolutePath();
		// Parameter.RESULT_FOLDER_BITMAP = Parameter.RESULT_FOLDER + "/BitMap";
		// Parameter.RESULT_FOLDER_DATABAK = Parameter.RESULT_FOLDER +
		// "/DataBak";
		// Parameter.RESULT_FOLDER_LOG = Parameter.RESULT_FOLDER + "/log";
		// Parameter.RESULT_FOLDER_RESULTTXT = Parameter.RESULT_FOLDER +
		// "/ResultTxt";
		// Parameter.RESULT_FOLDER_SQL = Parameter.RESULT_FOLDER + "/SQL";
		// Parameter.RESULT_FOLDER_WEB = Parameter.RESULT_FOLDER + "/WEB";
		// Parameter.ERRORFILE = startConfig.getResultFolder() + "/ResultTxt";

		new File(rcb.getResultFolderBitMap()).mkdir();
		new File(rcb.getResultFolderDataBak()).mkdir();
		new File(rcb.getResultFolderLog()).mkdir();
		new File(rcb.getResultFolderResultTxt()).mkdir();
		new File(rcb.getResultFolderSQL()).mkdir();
		new File(rcb.getResultFolderWeb()).mkdir();
		new File(rcb.getResultFolderReport()).mkdir();
		// new File(Parameter.ERRORFILE).mkdir();

		LogInformation.init(rcb.getResultFolderLog() + "/runlog.log");

	}

	/**
	 * 返回测试结果集
	 * 
	 * @return
	 */
	public RecordResult getRecordResult() {
		return test.getRecordResult();
	}

	public Test getTest() {
		return test;
	}

	// 运行测试
	@Override
	public void run() {
		
		beforeTest();
		
		if (flag) {
			// 远程调用
			if (keyWordDriver == null) {
				Print.log("关键字驱动没有加载无法运行,请先执行setKeyWordDriver方法", 2);
				return;
			}

			for (int i = 0; i < xmlReader.getTemplateCount(); i++) {
				HashMap<String, String> templateMap = xmlReader.getNodeValueByTemplateIndex(i);
				// testParameter = templateMap;
				startConfig = getStartConfigByMap(templateMap);
				beforeTest();
				test = new Test(startConfig, keyWordDriver, rcb);
			}
		} else {
			test = new Test(startConfig, keyWordDriver, rcb);
		}

		if (isLoop) {
			Print.log("暂停使用循环执行功能", 2);
			return;
			// new Thread(new BeforeTest(startConfig, keyWordDriver,
			// isLoop)).start();
		}

	}

}
