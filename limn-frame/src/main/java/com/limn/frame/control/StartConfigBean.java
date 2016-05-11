package com.limn.frame.control;

public class StartConfigBean {
	
	
	private String BrowserType = null;
	private String AppFilePath = null;
	private String RunTestModel = null;
	private String URL = null;
	private String ExcelPath = null;
	private String ExecuteMode = null;
	private String SheetsNum = null;
	private String FrontSteps = null;
	private String InitDB = null;
	private String SqlData = null;
	private String Computer = null;
	private String IP = null;
	private String Specify = null;
	private String SpecifySheet = null;
	private String SpecifyRow = null;
	private String SpecifyStep = null;
	private boolean UploadResults = false;
	private String StartPlatform = null;
	private boolean NotServer = false;
	private boolean Debug = false;
	
	/**
	 * 是否存在服务端环境
	 * @return
	 */
	public boolean isNotServer() {
		return NotServer;
	}
	
	/**
	 * 设置
	 * @param notServer
	 */
	public void setNotServer(boolean notServer) {
		NotServer = notServer;
	}
	
	public boolean isDebug() {
		return Debug;
	}
	
	public void setDeBug(boolean debug) {
		Debug = debug;
	}

	
	
	/**
	 * 获取浏览器类型
	 * @return "Firefox", "Chrome", "IE"
	 */
	public String getBrowserType() {
		return BrowserType;
	}
	/**
	 * 设置浏览器类型
	 * @param browserType "Firefox", "Chrome", "IE"
	 */
	public void setBrowserType(String browserType) {
		BrowserType = browserType;
	}
	
	/**
	 * 获取APP安装包存放路径
	 * @return
	 */
	public String getAppFilePath() {
		return AppFilePath;
	}

	/**
	 * 设置APP安装包的路径
	 * @param appFilePath
	 */
	public void setAppFilePath(String appFilePath) {
		AppFilePath = appFilePath;
	}

	/**
	 * 测试类型   "浏览器","Android","IOS"
	 * @return
	 */
	public String getRunTestModel() {
		return RunTestModel;
	}
	/**
	 * 设置测试类型
	 * @param runTestModel "浏览器","Android","IOS"
	 */
	public void setRunTestModel(String runTestModel) {
		RunTestModel = runTestModel;
	}

	/**
	 * 获取测试的URL
	 * @return
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * 设置测试的URL地址
	 * @param uRL
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * 获取测试用例的文件地址
	 * @return
	 */
	public String getExcelPath() {
		return ExcelPath;
	}

	/**
	 * 设置测试用例的文件地址
	 * @param excelPath
	 */
	public void setExcelPath(String excelPath) {
		ExcelPath = excelPath;
	}
	
	/**
	 * 运行模式
	 * @return "固定模式执行", "指定页面执行"
	 */
	public String getExecuteMode() {
		return ExecuteMode;
	}

	/**
	 * 设置运行模式
	 * @param executeMode "固定模式执行", "指定页面执行"
	 */
	public void setExecuteMode(String executeMode) {
		ExecuteMode = executeMode;
	}

	/**
	 * 获取指定运行模式下的Sheet页面 index
	 * @return
	 */
	public String getSheetsNum() {
		return SheetsNum;
	}

	/**
	 * 设置指定运行模式下的Sheet页面Index
	 * @param sheetsNum 0,1,2...
	 */
	public void setSheetsNum(String sheetsNum) {
		SheetsNum = sheetsNum;
	}
	
	/**
	 * 是否执行前置步骤
	 * @return "需要", "不需要"
	 */
	public String getFrontSteps() {
		return FrontSteps;
	}
	
	/**
	 * 设置是否需要执行前置步骤
	 * @param frontSteps "需要", "不需要"
	 */
	public void setFrontSteps(String frontSteps) {
		FrontSteps = frontSteps;
	}

	public String getInitDB() {
		return InitDB;
	}

	public void setInitDB(String initDB) {
		InitDB = initDB;
	}

	public String getSqlData() {
		return SqlData;
	}

	public void setSqlData(String sqlData) {
		SqlData = sqlData;
	}
	
	/**
	 * 运行方式
	 * @return "本机", "远程"
	 */
	public String getComputer() {
		return Computer;
	}
	
	/**
	 * 设置运行方式
	 * @param computer "本机", "远程"
	 */
	public void setComputer(String computer) {
		Computer = computer;
	}
	
	/**
	 * 远程运行的IP地址
	 * @return
	 */
	public String getIP() {
		return IP;
	}
	
	/**
	 * 设置远程运行的IP地址
	 * @param iP
	 */
	public void setIP(String iP) {
		IP = iP;
	}
	
	/**
	 * 是否指定步骤执行
	 * @return "不指定", "指定"
	 */
	public String getSpecify() {
		return Specify;
	}
	
	/**
	 * 设置是否制定步骤执行
	 * @param specify "不指定", "指定"
	 */
	public void setSpecify(String specify) {
		Specify = specify;
	}
	
	
	/**
	 * 获取指定的sheet页
	 * @return
	 */
	public String getSpecifySheet() {
		return SpecifySheet;
	}
	
	/**
	 * 设置指定的sheet页
	 * @param specifySheet
	 */
	public void setSpecifySheet(String specifySheet) {
		SpecifySheet = specifySheet;
	}

	/**
	 * 获取指定的行
	 * @return
	 */
	public String getSpecifyRow() {
		return SpecifyRow;
	}

	/**
	 * 设置指定行
	 * @param specifyRow
	 */
	public void setSpecifyRow(String specifyRow) {
		SpecifyRow = specifyRow;
	}

	/**
	 * 获取制定步骤
	 * @return
	 */
	public String getSpecifyStep() {
		return SpecifyStep;
	}

	/**
	 * 设置指定步骤
	 * @param specifyStep
	 */
	public void setSpecifyStep(String specifyStep) {
		SpecifyStep = specifyStep;
	}

	
	public boolean getUploadResults() {
		return UploadResults;
	}

	public void setUploadResults(boolean uploadResults) {
		UploadResults = uploadResults;
	}

	public String getStartPlatform() {
		return StartPlatform;
	}

	public void setStartPlatform(String startPlatform) {
		StartPlatform = startPlatform;
	}

}
