package com.limn.frame.control;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.limn.app.driver.AppDriver;
import com.limn.app.driver.exception.AppiumException;
import com.limn.driver.common.DriverParameter;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.keyword.BaseKeyWordType;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.frame.results.RecordResult;
import com.limn.frame.results.UploadServerData;
import com.limn.frame.testcase.TestCase;
import com.limn.frame.testcase.TestCaseExcel;
import com.limn.tool.log.RunLog;
import com.limn.tool.bean.RunParameter;
import com.limn.tool.bean.StartConfigBean;
import com.limn.tool.common.Common;
import com.limn.tool.common.ConvertCharacter;
import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.common.Screenshot;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.exception.ParameterException;
import com.limn.tool.regexp.RegExp;
import com.thoughtworks.selenium.SeleniumException;

/**
 * 程序驱动
 * 
 * @author tangxy
 * 
 */
public class Test {

	// 测试用例集
	public TestCase tc = null;

	// 前置用例集
	private HashMap<String, String> relate = null;

	// 是否处理相关用例
	private boolean isRelate = false;

	// 测试结果路径(用于是否是相关用例)
	private String resultPath = null;

	// 定义远程电脑IP
	private String IP = null;

	public LinkedHashMap<String, String> TRA_NAME = null;

	/**
	 * 运行时记录用例步骤
	 */
	public  int runTimeStepNum = 0;
	public  int runTimeSheetNum = 0;
	public  int runTimeRowNum = 0;

	// 截图
	private Screenshot screenshot = new Screenshot();

//	private boolean isRestart = false;

	private RecordResult recordResult = new RecordResult();

	private KeyWordDriver keyWordDriver = null;

	private boolean isAPPScreenshot = false;
	
	private StartConfigBean startConfig = null;

	// 不是null 时, 标志执行场景还原
	private String SR = null;

	public Test(StartConfigBean startConfig, KeyWordDriver kwd) throws SeleniumFindException {
		this.startConfig = startConfig;
		keyWordDriver = kwd;
		if (startConfig.getComputer() != null && startConfig.getComputer().equals("远程")) {
			IP = startConfig.getIP();
		}

		if (!startConfig.getRunTestModel().equalsIgnoreCase("浏览器")) {

			try {
				isAPPScreenshot = true;
				AppDriver.init(startConfig.getAppFilePath(), IP);
			} catch (AppiumException e) {
				Print.log(e.getMessage(), 2);
				Print.log("用例停止执行", 2);
				return;
			}

		} else {
			// 浏览器类型，URL地址
			DriverParameter.getDriverPaht().setDriver(startConfig.getBrowserType(), startConfig.getURL(), IP);
			try {
				DriverParameter.getDriverPaht().startBrowser();
			} catch (SeleniumFindException e) {
				Print.log(e.getMessage(), 2);
			}
		}

		// 根据界面上的浏览器类型设置,赋值BrowerType
		// browserType = Parameter.BROWSERTYPE;

		// 配置路径
		// if(Parameter.NOTSERVER){
		// ConfigInfo.setConfigPath(map.get("ConfigPath"));
		// }else{
		// coreReader = new CoreReader(Parameter.YIGOPATH);
		// try {
		// ConfigInfo.setConfigPath(coreReader.getValueByKey("server.config"));
		// } catch (ParameterException e) {
		//
		// }
		// }

		// 测试平台驱动
		// if (Parameter.PLATVERSION.equals("1.4")) {
		// RunKeyWord.setKeyWordDriver(new KeyWordDriver1_4Web());
		// } else if (Parameter.PLATVERSION.equals("1.6")) {
		// RunKeyWord.setKeyWordDriver(new KeyWordDriver1_6Web());
		// }

		// if(map.get("StartPlatform").equals("true")){
		// //需要增加一个默认步骤
		// isRestart = true;
		// }

		// 是否提交服务器
		if (startConfig.getUploadResults()) {
			recordResult.addRecordData(new UploadServerData());
		}

		// 默认步骤
		// defaultStep();

		try {
			FileUtil.copyFile(new File(startConfig.getExcelPath()), new File(RunParameter.getResultPaht().getResultFolder() + "/test.xls"));
//			ArrayList<String> Path = RegExp.matcherCharacters(startConfig.getExcelPath(), "[^\\\\/]{1,}");
//			Parameter.EXCELNAME = RegExp.matcherCharacters(Path.get(Path.size() - 1), ".*(?=.xls)").get(0);
			startConfig.setExcelPath(RunParameter.getResultPaht().getResultFolder() + "/test.xls");
			FileUtil.copySourceToFolder("report", RunParameter.getResultPaht().getResultFolderReport(), this.getClass());
			// FileUtil.copyDirectiory(Parameter.REPORT_PATH,
			// Parameter.RESULT_FOLDER_REPORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		tc = new TestCaseExcel(startConfig.getExcelPath());

		// 测试结果集
		recordResult.init(tc);

		if (startConfig.getSpecify().equals("指定")) {
			runTimeSheetNum = Integer.parseInt(startConfig.getSpecifySheet()) - 1;
			runTimeRowNum = Integer.parseInt(startConfig.getSpecifyRow()) - 1;
			runTimeStepNum = Integer.parseInt(startConfig.getSpecifyStep()) - 1;
			Print.log("指定Sheet:" + (runTimeSheetNum + 1), 0);
			Print.log("指定Row:" + (runTimeRowNum + 1), 0);
			Print.log("指定Step:" + (runTimeStepNum + 1), 0);
			tc.activateSheet(runTimeSheetNum);
			RunLog.init(tc.getSheetLastRowNumber());
			executeTestCase();
		} else if (startConfig.getExecuteMode() != null && startConfig.getExecuteMode().equals("固定模式执行")) {
			// 这里取消掉 旧的模式, 固定模式 就是读sheet0的用例.
			runTimeSheetNum = 0;
			tc.activateSheet(runTimeSheetNum);
			RunLog.init(tc.getSheetLastRowNumber());
			executeTestCase();
			// runTimeSheetNum = 0;
			// tc.activateSheet(runTimeSheetNum);
			//
			// RunLog.init(tc.getSheetLastRowNumber());
			// executeTestCase();
		} else { // 还没有处理是否执行前置用例
			tc.activateSheet(Integer.parseInt(startConfig.getSheetsNum()) - 1);
			runTimeSheetNum = Integer.parseInt(startConfig.getSheetsNum()) - 1;
			if (startConfig.getFrontSteps()) {
				isRelate = true;
				relate = tc.getTestCaseRelateNoByNo();
			}
			RunLog.init(tc.getSheetLastRowNumber());
			executeTestCase();
		}

		tc.saveFile();
		Print.log("用例执行完毕", 4);
		Print.log("***************结束分割线***************\n", 4);
		close();
	}

	private void close() {
		tc = null;
		relate = null;
		isRelate = false;
		resultPath = null;
		IP = null;

		runTimeStepNum = 0;
		runTimeSheetNum = 0;
		runTimeRowNum = 0;
		// 截图
		screenshot = new Screenshot();
//		isRestart = false;
		recordResult = new RecordResult();
		keyWordDriver = null;
		// 不是null 时, 标志执行场景还原
		SR = null;
	}

	/**
	 * 执行测试用例
	 * 
	 * @throws SeleniumFindException
	 */
	private void executeTestCase() throws SeleniumFindException {
		int count;
		int result = 1;

		// 测试结果集
		recordResult.addSheet(runTimeSheetNum);

		// 按模块执行
		for (int i = 0; i < tc.getExcelModuleStartIndex().size(); i++) {

			RunParameter.getResultPaht().setTestCaseMoudle(tc.getExcelModuleName().get(i));
			// 测试结果集
			recordResult.addModule(RunParameter.getResultPaht().getTestCaseMoudle());

			int m = tc.getExcelModuleStartIndex().get(i);

			if (runTimeRowNum > tc.getExcelModuleStartIndex().get(i)) {
				m = runTimeRowNum;
			}

			count = tc.getExcelModuleEndIndex().get(i);
			for (; m <= count; m++) {

				runTimeRowNum = m;
				tc.setCurrentRow(m);
				result = runSteps(false);
				if (result != ExecuteStatus.SUCCESS) {
					Print.log("跳过: " + RunParameter.getResultPaht().getTestCaseMoudle(), 2);

					tc.setResult("跳过下个模块");

					SR = "场景还原/";
					// 执行下个模块
					break;
				} else {
					if (null != SR) {
						Print.log("模块存在关联,跳过: " + RunParameter.getResultPaht().getTestCaseMoudle(), 2);
					}
				}
			}

		}
		runTimeRowNum = 0;
		// 修正用例总数
		recordResult.addTestCaseCount("");
	}

	/**
	 *
	 * 运行测试用例 (用例行数执行 tc 所设定的行列)
	 *
	 * @param isRelated
	 *            改用例是否是相关用例执行
	 * @return
	 * @throws SeleniumFindException
	 */
	private int runSteps(boolean isRelated) throws SeleniumFindException {

		int result = ExecuteStatus.SUCCESS;

		if (!isRelated) {
			resultPath = runTimeSheetNum + "/" + runTimeRowNum;
		} else {
			resultPath = resultPath + "/relate/" + tc.getCurrentRow();
		}

		if (tc.isExecute()) {

			// 加载别名数据
			String context = tc.getAssociatedProperites();
			TRA_NAME = null;
			if (null != context) {
				Print.debugLog("开始加载别名数据", 0);
				TRA_NAME = TransformationMap.transformationByString(context);
				Print.log("加载别名数据完成", 0);
			}

			if (null != SR) {

				String relateCase = tc.getRelatedNo();
				if (null != relateCase && !relateCase.isEmpty()) {
					tc.setResult("跳过下个模块");

					return ExecuteStatus.SUCCESS;
				}
				scenarioReduction();

			}
			RunParameter.getResultPaht().setTestCaseNo(tc.getTestCaseNo());
			if (isRelate) {
				Print.log("当前用例编号:" + RunParameter.getResultPaht().getTestCaseNo(), 0);
				String relateNo = relate.get(RunParameter.getResultPaht().getTestCaseNo());
				if (relateNo != null && !relateNo.equals("")) {
					// 记录原有的行列
					Print.log("相关用例编号:" + relateNo, 0);

					// 记录结果集
					recordResult.addRelatedCase(relateNo);

					int originalSheet = tc.getExcelSheetIndex();
					int originalRow = tc.getCurrentRow();
					String[] location = relate.get(tc.getRelatedNo() + "_Location").split("_");
					// 设置相关用例的行列
					tc.activateSheet(Integer.parseInt(location[0]));
					tc.setCurrentRow(Integer.parseInt(location[1]));
					runSteps(true);
					// 还原相关用例的行列
					tc.activateSheet(originalSheet);
					tc.setCurrentRow(originalRow);
				}
			}
			String[] steps = null;
			if (null != tc.getTestStep()) {
				steps = RegExp.splitWord(tc.getTestStep(), "\n");
			}

			if (null != steps && !steps[0].isEmpty()) {
				RunLog.setStepsForTextAreaByIndex(tc.getCurrentRow() + 1, steps, RunParameter.getResultPaht().getTestCaseNo());
				int stepNum = runTimeStepNum;
				// 测试结果集
				recordResult.addCase(RunParameter.getResultPaht().getTestCaseNo());
				RunParameter.getResultPaht().setCaseStatus(1);
				for (; stepNum < steps.length; stepNum++) {
					runTimeStepNum = stepNum;
					RunLog.highLightCurrentStep(stepNum);

					// 测试结果集
					recordResult.addStep(steps[stepNum], String.valueOf(result));
					result = runSingleStep(steps[stepNum], resultPath + "/" + runTimeStepNum);
					recordResult.addCaseLog(steps[stepNum], result);
					if (result != ExecuteStatus.SUCCESS) {

						// 异常截图
						RunParameter.getResultPaht().setCaseStatus(result);
						String bitMapPath = startConfig + "/" + resultPath + "/" + runTimeStepNum + "_" + steps[stepNum].split(":")[0] + "_error";
						bitMapPath = screenshot(bitMapPath);
						RunParameter.getResultPaht().setVerSnapshot("snapshot/" + runTimeSheetNum + "_" + (resultPath + "/" + runTimeStepNum).replaceAll("/", "_") + "_error");
						screenshot(RunParameter.getResultPaht().getResultFolderReport() + "/" + RunParameter.getResultPaht().getVerSnapshot());
						RunParameter.getResultPaht().setVerSnapshot(RunParameter.getResultPaht().getVerSnapshot() + ".png");
						RunParameter.getResultPaht().setErrorCapture(RunParameter.getResultPaht().getVerSnapshot());
						if (RegExp.findCharacters(steps[stepNum], "^验证:")) {
							result = 1;
							continue;
						}

						break;
					}
				}

				recordResult.addCaseReport();
				// setHyperLink(tc.getCurrentRow(),resultPath + "/" +
				// runTimeStepNum);

				// CollateData.initializationParameter();
				runTimeStepNum = 0;
			}
		}

		tc.saveFile();

		return result;
	}

	/**
	 * 步骤执行
	 * 
	 * @param step
	 *            步骤内容
	 * @param path
	 *            一个存储截图的路径相对路径
	 * @return
	 */
	private int runSingleStep(String step, String path) {
		if (null != SR) {
			path = SR;
		}

		if (RegExp.findCharacters(step, "^验证")) {
			RunParameter.getResultPaht().setVerSnapshot("snapshot/" + runTimeSheetNum + "_" + path.replaceAll("/", "_") + "_result");
			RunParameter.getResultPaht().setCheckPointName(step);
		}

		int results = 1;
		// try{

		String[] st = RegExp.splitKeyWord(step);
		switch (st[0]) {
		case BaseKeyWordType.VERIFICATION:
			CheckItems checkItems = new CheckItems();
			try {
				checkItems.branch(st);
			} catch (ParameterException e1) {
				results = -2;
			}
			
			
			if (!checkItems.isBoolActul()) {
				results = -5;
				RunParameter.getResultPaht().setErrorLog("预期结果与实际结果不一致。请看CHECKPOINT信息。");
			}
			break;
		default:
			//添加别名
			RegExp.splitKeyWord(step + ":" + TransformationMap.transformationByMap(TRA_NAME));
			
			results = keyWordDriver.start(st);
		}

		// } catch (NoSuchWindowException e2){
		// errString = e2.getMessage();
		// Print.log(errString, 2);
		// Parameter.ERRORLOG = errString;
		// return ExecuteStatus.FAILURE;
		// } catch (Exception e1){
		// errString = e1.getMessage();
		// Print.log(e1.getMessage(), 2);
		// Parameter.ERRORLOG = errString;
		// return ExecuteStatus.FAILURE;
		// }

		// 截图
		RunParameter.getResultPaht().setLogSnapshot("snapshot/" + runTimeSheetNum + "_" + path.replaceAll("/", "_") + "_log.png");

		String bitMapPath = RunParameter.getResultPaht().getResultFolderBitMap() + "/" + path + "_" + step.split(":")[0];
		bitMapPath = screenshot(bitMapPath);
		// bitMapPath = screenshot.snapShot(bitMapPath);
		try {
			FileUtil.copyFile(new File(bitMapPath), new File(RunParameter.getResultPaht().getResultFolderReport() + "/" + RunParameter.getResultPaht().getLogSnapshot()));
		} catch (IOException e) {
			results = -2;
			RunParameter.getResultPaht().setErrorLog(e.getMessage());
			Print.log(e.getMessage(), 2);
			e.printStackTrace();
		}
		// 记录结果集
		recordResult.addBitMap(bitMapPath);
		return results;
	}

	/**
	 * 执行场景还原
	 */
	private void scenarioReduction() {

		int rtstepn = runTimeStepNum;
		int rtsheetn = runTimeSheetNum;
		int rtrown = runTimeRowNum;
		int currentRow = tc.getCurrentRow();
		Print.log("环境出错,搜索还原场景步骤", 2);
		runTimeRowNum = 0;
		runTimeStepNum = 0;

		try {
			tc.activateSheet("Scenario Reduction");

			Print.log("开始执行还原场景步骤", 2);

			executeTestCase();

		} catch (Exception e) {

			Print.log("未找到还原场景步骤", 2);

		} finally {
			SR = null;
			tc.activateSheet(rtsheetn);
			runTimeRowNum = rtrown;
			runTimeSheetNum = rtsheetn;
			runTimeStepNum = rtstepn;
			tc.setCurrentRow(currentRow);
		}

	}

	private String screenshot(String bitMapPath) {
		String path = null;
		if (isAPPScreenshot) {
			path = AppDriver.screenshot(bitMapPath);
		} else {
			path = screenshot.snapShot(bitMapPath);
		}

		return path;
	}

	// private void defaultStep(){
	// // 默认步骤 这步是必须的
	// int defaultStep = 0;
	//
	// if(isRestart){
	// CallBat.closeMiddleware();
	// runSingleStep("启动", "默认步骤/");
	// defaultStep = defaultStep + 1;
	// }
	//
	// runSingleStep("运行", "默认步骤/" + defaultStep);
	// File file = new File(Parameter.DFAULT_TEST_PATH + "/DefaultStep.txt");
	//
	//
	//
	// try {
	// if (file.isFile() && file.exists()) { // 判断文件是否存在
	// InputStreamReader read;
	//
	// read = new InputStreamReader(new FileInputStream(file));
	// //考虑到编码格式
	// BufferedReader bufferedReader = new BufferedReader(read);
	// String lineTxt = null;
	// while ((lineTxt = bufferedReader.readLine()) != null) {
	// defaultStep ++;
	// runSingleStep(lineTxt, "默认步骤/" + defaultStep);
	// }
	// read.close();
	// } else {
	// //默认步骤 未设置时的默认步骤
	// runSingleStep("录入:用户代码:administrator", "默认步骤/" + defaultStep);
	// // 增加831时设置帐套。
	// if (Parameter.TESTCASEPATH.indexOf("831") != -1){
	// runSingleStep("录入:账套:NewCorp", "默认步骤/" + defaultStep);
	// }
	// runSingleStep("点击:确定(&O)", "默认步骤/" + defaultStep);
	// }
	// } catch (IOException e) {
	// //默认步骤 未设置时的默认步骤
	// runSingleStep("录入:用户代码:administrator", "默认步骤/" + defaultStep);
	// // 增加831时设置帐套。
	// if (Parameter.TESTCASEPATH.indexOf("831") != -1){
	// runSingleStep("录入:账套:NewCorp", "默认步骤/" + defaultStep);
	// }
	// runSingleStep("点击:确定(&O)", "默认步骤/" + defaultStep);
	// }
	//// ConfigInfo.currentBillKey();
	//// Driver.runScript("Web_ShowDeskUI_JS('')");
	// }

	// /**
	// * 获取下一行用例
	// * @return String[]
	// */
	// public static String[] getNextStep(){
	// TestCase testCase = tc;
	// String[] steps = RegExp.splitWord(testCase.getTestStep(), "\n");
	// int runTimeStepNum = Test.runTimeStepNum;
	// String nextStep;
	// if (steps.length-1 > runTimeStepNum){
	// nextStep = steps[runTimeStepNum +1]; // 下一行用例
	// }else{
	// steps = RegExp.splitWord(testCase.getNextTestStep(), "\n");
	// nextStep = steps[0]; // 下一行用例
	// }
	// String[] nextKeys = RegExp.splitKeyWord(nextStep);
	// return nextKeys;
	// }

	/**
	 * 获取预期结果，第5列
	 * 
	 * @return 字符串
	 * @throws SeleniumFindException
	 */
	public String getExpectedResult() {
		excelExist();
		return tc.getExpected();
	}

	/**
	 * 获取excel实际结果内容，第6列
	 * 
	 * @return
	 * @throws SeleniumFindException
	 */
	public String getActulResult() {
		excelExist();
		return tc.getActual();
	}

	/**
	 * 获取excel关联属性，第6列
	 * 
	 * @return
	 * @throws SeleniumFindException
	 */
	public String getAssociatedProperites() {
		excelExist();
		return tc.getAssociatedProperites();
	}

	/**
	 * 实际结果内容写入excel，第6列
	 * 
	 * @param value
	 * @throws SeleniumFindException
	 */
	public void setAcutal(String value) {
		excelExist();
		tc.setAcutal(value);
		recordResult.addActualResults(value.split("\n"));
	}

	/**
	 * 写入sql结果。 第7列
	 * 
	 * @param value
	 * @throws SeleniumFindException
	 */
	public void setSqlResults(String value) {
		excelExist();
		tc.setSQLResults(value);

	}

	/**
	 * 对比结果写入excel true||false，第8列
	 * 
	 * @param value
	 * @throws SeleniumFindException
	 */
	public void setAcutalResult(String value) {
		excelExist();
		tc.setResult(value);
	}

	/**
	 * 写入对比结果，设置字体颜色、
	 * 
	 * @param value
	 * @throws SeleniumFindException
	 */
	public void setStyleAcutalResult(String value, String style) {
		excelExist();
		// tc.setResult(value,style);
		tc.setResult(value);
	}

	/**
	 * 对比结果写入excel true||false，第8列
	 * 
	 * @param value
	 * @throws SeleniumFindException
	 */
	public void setAcutalResult(boolean value) {
		if (excelExist()) {
			tc.setResult(String.valueOf(value));
			recordResult.addExpectedResults(tc.getExpected().split("\n"));
			recordResult.addResult(value);
		}
	}

	/**
	 * 对比结果写入excel true||false，第8列
	 * 
	 * @param value
	 * @throws SeleniumFindException
	 */
	public void setAcutalResult(String expectedResult, boolean value) {
		excelExist();
		tc.setResult(String.valueOf(value));
		recordResult.addExpectedResults(expectedResult.split("\n"));
		recordResult.addResult(value);
	}

	/**
	 * 获取预期结果，第5列
	 * 
	 * @return 数组
	 * @throws SeleniumFindException
	 */
	public String[] getArrExpectedResult() {
		excelExist();
		String[] steps = new String[0];
		String expectedStr = tc.getExpected();
		if (!expectedStr.isEmpty()) {
			steps = RegExp.splitWord(expectedStr, "\n");
		} else {
			RunLog.printLog("预期结果为空！", 2);
		}
		return steps;
	}

	public boolean excelExist() {
		boolean isExist = true;
		if (tc == null) {
			isExist = false;
		}
		return isExist;
	}

	/**
	 * 获取用例所有要执行的用例个数
	 * 
	 * @return
	 */
	public int getAllCaseSum() {
		return tc.getAllCase();
	}

	/**
	 * 设置超链接
	 * 
	 * @param index
	 *            列
	 * @param path
	 *            超链接地址
	 */
	public void setHyperLink(int index, String path) {
		excelExist();
		tc.setHyperLinks(index, path);
	}

	public int getCurrentRowCount() {
		excelExist();
		return tc.getSheetLastRowNumber();
	}

	public int getSheetIndex() {
		excelExist();
		return tc.getExcelSheetIndex();
	}

	public int getCurrentRow() {
		if (excelExist()) {
			return tc.getCurrentRow();
		}
		return 0;
	}

	public String getCurrentStep() {
		if (excelExist()) {
			return tc.getTestStep();
		}
		return "";
	}

	public String getCurrentStepNo() {
		if (excelExist()) {
			return tc.getTestCaseNo();
		}
		return "";
	}

	public int getCaseCount() {
		return recordResult.getCaseCount();
	}

	public int getExecuteCaseCount() {
		return recordResult.getExecuteCaseCount();
	}

	public int getSucessCaseCount() {
		return recordResult.getSucessCaseCount();
	}

	/**
	 * 校验类
	 * 
	 * @author limengnan
	 *
	 */
	class CheckItems {
		/**
		 * 预期
		 */
		private String[] expectedResults;
		/**
		 * 单个预期对比后的结果（0:内容,1:true || false）
		 */
		private String[] actulResults = new String[2];

		public void setActulResults(String[] actulResults) {
			this.actulResults = actulResults;
		}

		/**
		 * 所有预期的结果true || false
		 */
		private boolean boolActul = true;

		public boolean isBoolActul() {
			return boolActul;
		}

		/**
		 * 所有的结果内容
		 */
		private String acutalAllStep = "";

		/**
		 * 获取到的xpath对象
		 */
		private WebElement webElement = null;
		/**
		 * 每一行预期结果 字符串
		 */
		private String expectedStr = "";
		/**
		 * 每一行预期结果 数组
		 */
		private String[] expectedKeys = {};
		/**
		 * 表格列
		 */
		private int column = -1;

		/**
		 * 获取对象。
		 * 
		 * @return
		 */
		public WebElement getWebElement() {
			return webElement;
		}

		int controlTypeNum = -1;
		/**
		 * 存放表格列名与坐标值
		 */
		private LinkedHashMap<String, LinkedList> tableNameCol = new LinkedHashMap<String, LinkedList>();
		/**
		 * 存放表格中的值
		 */
//		private String[][] tableNameValue = {};

		/**
		 * 重写预期结果（转换表达式）
		 */
		private LinkedList<String> expectedGetExpression = new LinkedList<String>();

		public CheckItems() {

		}

		// public CheckItems(IGetWebElmentByKey iGetWebElmentByKey){
		// ConfigInfo.getBillPath();
		// this.getWebElement = iGetWebElmentByKey;
		// }
		/**
		 * 初始化实例变量
		 */
		public void initializationParameter() {
			this.webElement = null;
			this.column = -1;
		}

		/**
		 * 关键字分支
		 * 
		 * @param keys
		 * @throws ParameterException
		 */
		public void branch(String[] keys) throws ParameterException {
			if (keys.length > 1) {
				if (keys[1].equals("表格")) {
					if (!keys[2].equals("")) {
						setTableNameColNum(keys);
					} else {
						throw new ParameterException("验证表格的xpath为空。");
					}
				}
			}
			// TODO debug的时候需要增加xpath步奏、对比时读取xpath然后取值，
			this.expectedResults = getArrExpectedResult();
			for (int i = 0; i < expectedResults.length; i++) {
				// 把用例中的"\:" "\;" 转义。

				expectedStr = ConvertCharacter.getHtmlAsc(expectedResults[i]);
				expectedKeys = RegExp.splitKeyWord(expectedStr);
				if (keys[0].equals("验证")) {
					check(keys);
					// verification(keys);
					// }else if(keys[0].equals("查看")){
					// check(keys);
					// }else if(keys[0].equals("核对")){
					//// collate(keys);
					// break;
				} else {
					throw new ParameterException("关键字错误：" + keys[0]);
				}
				// if (!keys[0].equals("核对")){
				// 对比结果
				disposeResult();
				// }
				// 初始化实例变量
				initializationParameter();
			}
			// 把结果写入excel。
			writeResult();
			// System.out.println(Test.getActulResult());
			// System.out.println(boolActul);
		}

		/**
		 * 验证
		 * 
		 * @throws ParameterException
		 */
		public void check(String[] keys) throws ParameterException {
			if (keys.length > 1) {
				if (keys[1].equals("表格")) {
					if (keys[2].equals("")) {
						throw new ParameterException("验证表格的xpath为空。");
					}
					expectedKeys = getBillExpectedResult();
					actulResults = checkTableData();
				} else {
					throw new ParameterException("验证关键字有误:" + keys[1]);
				}
			} else {
				// String xpath = expectedKeys[0];
				actulResults = checkControlData();
			}
		}

		/**
		 * 设置table的列名和坐标
		 * 
		 * @param step
		 * @throws ParameterException
		 */
		private void setTableNameColNum(String[] step) throws ParameterException {
			HashMap<String, String> traXPath = null;
			if (step.length >= 4 && RegExp.findCharacters(step[3], "^HASHMAP")) {
				// DEBUG模式
				traXPath = TransformationMap.transformationByString(step[3]);
			} else {
				// START模式
				String context = getAssociatedProperites();
				if (null == context) {
					traXPath = null;
				} else {
					traXPath = TransformationMap.transformationByString(context);
				}
			}
			// if(step.length >= 4 && RegExp.findCharacters(step[3],
			// "^HASHMAP")){
			// traXPath = TransformationMap.transformationByString(step[3]);
			// }else{
			// throw new ParameterException("缺少表格对应的xpath");
			// }
			String tableLocator = null;
			if (null != traXPath) {
				if (traXPath.containsKey(step[2])) {
					tableLocator = traXPath.get(step[2]);
				} else {
					tableLocator = step[2];
				}
			} else {
				tableLocator = step[2];
			}
			if (null == tableLocator) {
				throw new ParameterException("对应的xpath为null。");
			}
			WebElement tableElement = DriverParameter.getDriverPaht().getWebElementBylocator(tableLocator);
			WebElement tableHead = tableElement.findElement(By.xpath(".//thead"));
			List<WebElement> tableHeadTh = tableHead.findElements(By.xpath(".//th"));
			Iterator<WebElement> tableHeadThIte = tableHeadTh.iterator();
			// 确定字段列数
			int tdIndex = 1;
			while (tableHeadThIte.hasNext()) {
				WebElement thIte = tableHeadThIte.next();
				String colString = thIte.getText();
				LinkedList<String> valueList = new LinkedList<String>();
				WebElement tableBody = tableElement.findElement(By.xpath(".//tbody"));
				List<WebElement> tableBodyTrList = tableBody.findElements(By.xpath(".//tr"));
				Iterator<WebElement> tableBodyTrIte = tableBodyTrList.iterator();
//				int tdNum = 1;
				while (tableBodyTrIte.hasNext()) {
					WebElement trIte = tableBodyTrIte.next();
					int tdInd = tdIndex;
					// 获取第一个td的colspan属性
					if (tdInd > 1) {
						List<WebElement> tdFirstElementList = trIte.findElements(By.xpath(".//td[1]"));
						WebElement tdFirstElement = tdFirstElementList.iterator().next();
						String colspan = tdFirstElement.getAttribute("colspan");
						if (null != colspan) { // || !colspan.equals("")
							int cols = Integer.parseInt(colspan);
							if (tdInd <= cols) {
								continue;
							} else {
								tdInd = tdIndex - cols + 1;
							}
						}
					}

					List<WebElement> tdElement = trIte.findElements(By.xpath(".//td[" + tdInd + "]"));
					WebElement tdEle = tdElement.iterator().next();
					String colValue = tdEle.getText();
					// if (colString.equals("客户信息")){
					// System.out.print(colValue);
					//
					// }
					if (RegExp.findCharacters(colValue, "\n")) {
						colValue = colValue.replaceAll("\n", " ");
					}
					valueList.add(colValue);
				}
				tableNameCol.put(colString, valueList);
				tdIndex++;
			}

		}

		private String[] checkTableData() throws ParameterException {
			boolean boolResult = true; // 结果 true||false
			String acutalString = "";
			String[] arr = new String[2];
			LinkedList<?> values = tableNameCol.get(expectedKeys[0]);
			int expectedLen = expectedKeys.length - 1;
			int atuLen = values.size();
			String[] acutalTempArr = expectedKeys.clone();
			for (int i = 0; i < expectedLen; i++) {
				acutalTempArr[i + 1] = Common.getExpressionValue(expectedKeys[i + 1]);
				expectedKeys[i + 1] = acutalTempArr[i + 1];

			}
			if ((expectedKeys.length - 1) != values.size()) {
				acutalString = "预期结果个数与实际结果个数不一样：预期结果 " + expectedLen + "个:" + StringUtils.join(expectedKeys, " ") + "，实际结果 " + atuLen + "个:" + values.toString();
				boolResult = false;
				// acutalString = acutalString + "\n" + values.toString();
				// throw new ParameterException(atuString);
			} else {

				for (int i = 0; i < expectedLen; i++) {
					// acutalTempArr[i+1] =
					// Common.getExpressionValue(expectedKeys[i+1]);
					// expectedKeys[i+1] = acutalTempArr[i+1];

					// expectAllStr = expectTempArr
					// String expectTemp = expectedKeys[i+1];
					String autValue = values.get(i).toString();
					if (!acutalTempArr[i + 1].equals(autValue)) {
						boolResult = false;
						acutalTempArr[i + 1] = autValue;
					}
				}
				acutalString = StringUtils.join(acutalTempArr, " ");
			}
			expectedStr = StringUtils.join(expectedKeys, " ");
			// 重写预期结果（转换表达式）
			expectedGetExpression.add(expectedStr);

			arr[0] = acutalString;
			arr[1] = String.valueOf(boolResult);
			return arr;
		}

		/**
		 * 验证，对比结果
		 * 
		 * @param xpath
		 * @return
		 * @throws ParameterException
		 */
		private String[] checkControlData() throws ParameterException {
			String acutalResult = getControlData(expectedKeys); // 实际界面上获取到的结果。
			boolean boolResult = true; // 结果 true||false
			// boolean isCDbl = doesChangeDouble(); //Decimal Int
			// 与预期结果做对比，
			// if (expectedKeys[0].equals("控件")){
			// // 判断值为空
			// if (expectedKeys[3].equals("空")){
			// if (!acutalResult.isEmpty()){
			// boolResult = false;
			// expectedKeys[3] = acutalResult;
			// }
			// }else{
			// if (isCDbl){
			// if (Double.parseDouble(expectedKeys[3]) !=
			// Double.parseDouble(acutalResult)){
			// boolResult = false;
			// }
			// }else{
			// if
			// (!ConvertCharacter.getHtmlChr(expectedKeys[3]).equals(acutalResult)){
			// boolResult = false;
			// }
			// }
			// expectedKeys[3] = acutalResult;
			// }
			// }else{
			// if (isCDbl){
			// if (Double.parseDouble(expectedKeys[1]) !=
			// Double.parseDouble(acutalResult)){
			// boolResult = false;
			// }
			// }else{
			// 预期结果中只写CODE
			// String[] arrNewExp = expectedKeys[1].split(" ");
			// if (arrNewExp.length == 1){
			// if (!acutalResult.isEmpty()){
			// String[] arrNewAct = acutalResult.split(" ");
			// acutalResult = arrNewAct[0];
			// }
			// }
			String[] acutalTempArr = expectedKeys.clone();
			String expectValue = null;
			// try {
			// try{
			// int inT = Integer.parseInt(expectedKeys[1]);
			// expectValue = null;
			// }catch(Exception e){
			expectValue = Common.getExpressionValue(expectedKeys[1]);
			// }
			expectedKeys[1] = expectValue == null ? expectedKeys[1] : expectValue;
			acutalTempArr[1] = expectedKeys[1];
			// } catch (ParameterException e) {
			// throw new ParameterException("语法解析失败，表达式：" + expectedKeys[1]);
			// }

			if (!ConvertCharacter.getHtmlChr(expectedKeys[1]).equals(acutalResult)) {
				boolResult = false;
				acutalTempArr[1] = acutalResult;
			}
			// }
			expectedKeys[1] = acutalResult;
			expectedStr = StringUtils.join(expectedKeys, " ");
			// 重写预期结果（转换表达式）
			expectedGetExpression.add(expectedStr);
			// }
			String[] arr = { StringUtils.join(acutalTempArr, " "), String.valueOf(boolResult) };
			return arr;
		}

		/**
		 * 处理结果。得到实际结果、结果内容。
		 */
		private void disposeResult() {
			String acutalStep = actulResults[0];
			if (actulResults[1].equalsIgnoreCase("false")) {
				boolActul = false;
			}
			if (acutalAllStep == "") {
				acutalAllStep = acutalStep;
			} else {
				acutalAllStep = acutalAllStep + "\n" + acutalStep;
			}
		}

		/**
		 * 把结果写入excel
		 */
		private void writeResult() {
			String acutalResults = getActulResult();
			if (acutalResults == "" || acutalResults == null) {
				acutalResults = acutalAllStep;
			} else {
				acutalResults = acutalResults + "\n" + acutalAllStep;
			}

			new Screenshot().snapShot(RunParameter.getResultPaht().getResultFolderReport() + "/" + RunParameter.getResultPaht().getVerSnapshot());
			RunParameter.getResultPaht().setVerSnapshot(RunParameter.getResultPaht().getVerSnapshot() + ".png");
			// 重写预期结果（转换表达式）
			String[] expectedTemp = new String[expectedGetExpression.size()];
			expectedTemp = expectedGetExpression.toArray(expectedTemp);
			setAcutalResult(StringUtils.join(expectedTemp, "\n"), boolActul);
			setAcutal(acutalResults);
		}

		/**
		 * 处理 预期结果
		 * 
		 * @return
		 */
		private String[] getBillExpectedResult() {
			// expectedStr = ConvertCharacter.getHtmlChr(expectedStr);
			String str1 = ";";
			String str2 = ":";

			String[] arr = RegExp.splitKeyWord(expectedStr);
			String[] arrExpected = null; // 处理后的预期结果
			if (expectedStr.equals(arr[0] + str1) || expectedStr.equals(arr[0] + str2)) {
				arrExpected = new String[2];
				arrExpected[0] = arr[0];
				arrExpected[1] = "";
			} else {
				if (expectedStr.indexOf(arr[0] + str1) != -1) {
					arrExpected = rebulitExpCase(arr[0], str1);
				} else if (expectedStr.indexOf(arr[0] + str2) != -1) {
					arrExpected = rebulitExpCase(arr[0], str2);
				} else {
					Print.log("预期结果可能存在问题：" + expectedStr, 2);
				}
			}
			return arrExpected;
		}

		private String[] rebulitExpCase(String text, String str) {
			String[] arrNew;
			String strNew = expectedStr.replace(text + str, "");
			String[] arrNewCase = strNew.split("[;|:]", -1);
			arrNew = new String[arrNewCase.length + 1];
			arrNew[0] = text;
			for (int i = 0; i < arrNewCase.length; i++) {
				arrNew[i + 1] = arrNewCase[i];
			}
			return arrNew;

		}

		/*------------------------------------------------------------------------------------*/
		/*--------------------------------下面的方法用于调试时调用-----------------------------*/
		/*-----------------------------------------------------------------------------------*/
		/**
		 * 获取控件的值（调试)
		 * 
		 * @param xpath
		 * @return
		 */
		public String getControlData(String[] step) {
			String acutalResult = "";
			try {
				HashMap<String, String> traXPath = null;
				if (step.length >= 3 && RegExp.findCharacters(step[2], "^HASHMAP")) {
					traXPath = TransformationMap.transformationByString(step[2]);
				} else {
					String context = getAssociatedProperites();
					if (null == context || context.equals("")) {
						traXPath = null;
					} else {
						traXPath = TransformationMap.transformationByString(context);
					}
				}

				String xpath = null;
				if (null != traXPath) {
					if (traXPath.containsKey(step[0])) {
						xpath = traXPath.get(step[0]);
					} else {
						xpath = step[0];
					}
				} else {
					xpath = step[0];
				}

				webElement = DriverParameter.getDriverPaht().getWebElementBylocator(xpath);
				if (webElement != null) {
					acutalResult = webElement.getText();
					// if (isMultiDict){
					// if (acutalResult.indexOf(";") != -1){
					// acutalResult = acutalResult.replaceAll(";", ",");
					// }
					// }
				} else {
					throw new SeleniumException("xpath没有定位到：" + xpath);
				}
				// Driver.setValue(xpath,step[2]);

			} catch (Exception e) {
				new SeleniumFindException("错误:" + e.getMessage());
				// Print.log(e.getMessage(), 2);
			}

			// 单选、多选框
			// if(controlType.equalsIgnoreCase("CHECKBOX")){
			// acutalResult = String.valueOf(webElement.isSelected());
			// }
			return acutalResult;
		}

	}

}
