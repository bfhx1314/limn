package com.limn.tool.bean;

public class StartConfigBean {
	
	
	private String browserType = null;
	private String appFilePath = null;
	private String runTestModel = null;
	private String URL = "";
	private String excelPath = null;
	private String executeMode = null;
	private String sheetsNum = null;
	private boolean frontSteps = false;
	private boolean initDB = false;
	private String SQLData = null;
	private String computer = null;
	private String IP = null;
	private String specify = null;
	private String specifySheet = null;
	private String specifyRow = null;
	private String specifyStep = null;
	private boolean uploadResults = false;
	private String startPlatform = null;
	private boolean notServer = false;
	private boolean debug = false;
	
	
	
	/**
	 * 是否存在服务端环境
	 * @return
	 */
	public boolean isNotServer() {
		return notServer;
	}
	
	/**
	 * 设置
	 * @param notServer
	 */
	public void setNotServer(boolean notServer) {
		this.notServer = notServer;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDeBug(boolean debug) {
		this.debug = debug;
	}

	
	
	/**
	 * 获取浏览器类型
	 * @return "Firefox", "Chrome", "IE"
	 */
	public String getBrowserType() {
		return browserType;
	}
	/**
	 * 设置浏览器类型
	 * @param browserType "Firefox", "Chrome", "IE"
	 */
	public void setBrowserType(String browserType) {
		this.browserType = browserType;
	}
	
	/**
	 * 获取APP安装包存放路径
	 * @return
	 */
	public String getAppFilePath() {
		return appFilePath;
	}

	/**
	 * 设置APP安装包的路径
	 * @param appFilePath
	 */
	public void setAppFilePath(String appFilePath) {
		this.appFilePath = appFilePath;
	}

	/**
	 * 测试类型   "浏览器","Android","IOS"
	 * @return
	 */
	public String getRunTestModel() {
		return runTestModel;
	}
	/**
	 * 设置测试类型
	 * @param runTestModel "浏览器","Android","IOS"
	 */
	public void setRunTestModel(String runTestModel) {
		this.runTestModel = runTestModel;
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
	public void setURL(String URL) {
		this.URL = URL;
	}

	/**
	 * 获取测试用例的文件地址
	 * @return
	 */
	public String getExcelPath() {
		return excelPath;
	}

	/**
	 * 设置测试用例的文件地址
	 * @param excelPath
	 */
	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}
	
	/**
	 * 运行模式
	 * @return "固定模式执行", "指定页面执行"
	 */
	public String getExecuteMode() {
		return executeMode;
	}

	/**
	 * 设置运行模式
	 * @param executeMode "固定模式执行", "指定页面执行"
	 */
	public void setExecuteMode(String executeMode) {
		this.executeMode = executeMode;
	}

	/**
	 * 获取指定运行模式下的Sheet页面 index
	 * @return
	 */
	public String getSheetsNum() {
		return sheetsNum;
	}

	/**
	 * 设置指定运行模式下的Sheet页面Index
	 * @param sheetsNum 0,1,2...
	 */
	public void setSheetsNum(String sheetsNum) {
		this.sheetsNum = sheetsNum;
	}
	
	/**
	 * 是否执行前置步骤
	 * @return "需要", "不需要"
	 */
	public boolean getFrontSteps() {
		return frontSteps;
	}
	
	/**
	 * 设置是否需要执行前置步骤
	 * @param frontSteps "需要", "不需要"
	 */
	public void setFrontSteps(boolean frontSteps) {
		this.frontSteps = frontSteps;
	}

	public boolean getInitDB() {
		return initDB;
	}

	public void setInitDB(boolean initDB) {
		this.initDB = initDB;
	}

	public String getSqlData() {
		return SQLData;
	}

	public void setSqlData(String sqlData) {
		this.SQLData = sqlData;
	}
	
	/**
	 * 运行方式
	 * @return "本机", "远程"
	 */
	public String getComputer() {
		return computer;
	}
	
	/**
	 * 设置运行方式
	 * @param computer "本机", "远程"
	 */
	public void setComputer(String computer) {
		this.computer = computer;
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
		return specify;
	}
	
	/**
	 * 设置是否制定步骤执行
	 * @param specify "不指定", "指定"
	 */
	public void setSpecify(String specify) {
		this.specify = specify;
	}
	
	
	/**
	 * 获取指定的sheet页
	 * @return
	 */
	public String getSpecifySheet() {
		return specifySheet;
	}
	
	/**
	 * 设置指定的sheet页
	 * @param specifySheet
	 */
	public void setSpecifySheet(String specifySheet) {
		this.specifySheet = specifySheet;
	}

	/**
	 * 获取指定的行
	 * @return
	 */
	public String getSpecifyRow() {
		return specifyRow;
	}

	/**
	 * 设置指定行
	 * @param specifyRow
	 */
	public void setSpecifyRow(String specifyRow) {
		this.specifyRow = specifyRow;
	}

	/**
	 * 获取制定步骤
	 * @return
	 */
	public String getSpecifyStep() {
		return specifyStep;
	}

	/**
	 * 设置指定步骤
	 * @param specifyStep
	 */
	public void setSpecifyStep(String specifyStep) {
		this.specifyStep = specifyStep;
	}

	
	public boolean getUploadResults() {
		return uploadResults;
	}

	public void setUploadResults(boolean uploadResults) {
		this.uploadResults = uploadResults;
	}

	public String getStartPlatform() {
		return startPlatform;
	}

	public void setStartPlatform(String startPlatform) {
		this.startPlatform = startPlatform;
	}
}
