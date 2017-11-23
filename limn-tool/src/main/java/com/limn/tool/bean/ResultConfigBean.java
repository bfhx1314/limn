package com.limn.tool.bean;

public class ResultConfigBean {
	
	private String testCaseFolderPath = null;
	private String productName = "";
	private String testName = "";
	private String resultFolder = null;
	private String resultFolderBitMap = null;
	private String resultFolderDataBak = null;
	private String resultFolderLog = null;
	private String resultFolderSQL = null;
	private String resultFolderResultTxt = null;
	private String resultFolderWeb = null;
	private String resultFolderReport = null;
	private String startTime = "";
	private String endTime = "";
	private String testEnvironment = "";
	private String testCaseNo = "";
	private String testCaseMoudle = "";
	private String errorMessage  = "";
	private String errorLog  = "";
	private String errorCapture = "";
	private String verSnapshot = "";
	private String logSnapshot = "";
	private int caseStatus = 0;
	private String checkPointName = "";
	

	public String getTestCaseFolderPath() {
		return testCaseFolderPath;
	}
	
	/**
	 * 测试文件目录
	 * @param testCaseFolderPath
	 */
	public void setTestCaseFolderPath(String testCaseFolderPath) {
		this.testCaseFolderPath = testCaseFolderPath;
	}

	public String getProductName() {
		return productName;
	}
	
	
	/**
	 * 产品名称
	 * @param productName
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getResultFolderBitMap() {
		return resultFolderBitMap;
	}

	public void setResultFolderBitMap(String rresultFolderBitMap) {
		this.resultFolderBitMap = rresultFolderBitMap;
	}

	public String getResultFolderDataBak() {
		return resultFolderDataBak;
	}

	public void setResultFolderDataBak(String resultFolderDataBak) {
		this.resultFolderDataBak = resultFolderDataBak;
	}

	public String getResultFolderLog() {
		return resultFolderLog;
	}

	public void setResultFolderLog(String resultFolderLog) {
		this.resultFolderLog = resultFolderLog;
	}

	public String getResultFolderSQL() {
		return resultFolderSQL;
	}

	public void setResultFolderSQL(String resultFolderSQL) {
		this.resultFolderSQL = resultFolderSQL;
	}

	public String getResultFolderResultTxt() {
		return resultFolderResultTxt;
	}

	public void setResultFolderResultTxt(String resultFolderResultTxt) {
		this.resultFolderResultTxt = resultFolderResultTxt;
	}

	public String getResultFolderWeb() {
		return resultFolderWeb;
	}

	public void setResultFolderWeb(String resultFolderWeb) {
		this.resultFolderWeb = resultFolderWeb;
	}

	public String getResultFolderReport() {
		return resultFolderReport;
	}

	public void setResultFolderReport(String resultFolderReport) {
		this.resultFolderReport = resultFolderReport;
	}

	public String getResultFolder() {
		return resultFolder;
	}

	public void setResultFolder(String resultFolder) {
		this.resultFolder = resultFolder;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTestEnvironment() {
		return testEnvironment;
	}

	public void setTestEnvironment(String testEnvironment) {
		this.testEnvironment = testEnvironment;
	}

	public String getTestCaseNo() {
		return testCaseNo;
	}

	public void setTestCaseNo(String testCaseNo) {
		this.testCaseNo = testCaseNo;
	}

	public String getTestCaseMoudle() {
		return testCaseMoudle;
	}

	public void setTestCaseMoudle(String testCaseMoudle) {
		this.testCaseMoudle = testCaseMoudle;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public String getErrorCapture() {
		return errorCapture;
	}

	public void setErrorCapture(String errorCapture) {
		this.errorCapture = errorCapture;
	}

	public int getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(int caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getVerSnapshot() {
		return verSnapshot;
	}

	public void setVerSnapshot(String verSnapshot) {
		this.verSnapshot = verSnapshot;
	}

	public String getCheckPointName() {
		return checkPointName;
	}

	public void setCheckPointName(String checkPointName) {
		this.checkPointName = checkPointName;
	}

	public String getLogSnapshot() {
		return logSnapshot;
	}

	public void setLogSnapshot(String logSnapshot) {
		this.logSnapshot = logSnapshot;
	}


}
