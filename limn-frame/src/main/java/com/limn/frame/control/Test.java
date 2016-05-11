package com.limn.frame.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


import com.limn.app.driver.AppDriver;
import com.limn.app.driver.exception.AppiumException;
import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.frame.results.RecordResult;
import com.limn.frame.results.UploadServerData;
import com.limn.frame.testcase.TestCase;
import com.limn.frame.testcase.TestCaseExcel;
import com.limn.tool.log.RunLog;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.common.Screenshot;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.regexp.RegExp;
import com.thoughtworks.selenium.SeleniumException;

/**
 * 程序驱动
 * 
 * @author tangxy
 * 
 */
public class Test {
	
	// 定义浏览器类型
//	private int browserType = BrowserType.FireFox;
	// 定义URL
//	private String URL = null;

	public static TestCase tc = null;

	// 定义core文件属性集合
//	private CoreReader coreReader = null;

	private HashMap<String,String> relate = null;
	
	//是否处理相关用例
	private boolean isRelate = false;
	
	//测试结果路径(用于是否是相关用例)
	private String resultPath = null;
	
	// 定义远程电脑IP
	private String IP = null;

	public static HashMap<String,String> TRA_NAME = null; 
	
	/**
	 *  运行时记录用例步骤
	 */
	public static int runTimeStepNum = 0;
	public static int runTimeSheetNum = 0;
	public static int runTimeRowNum = 0;

	// 截图
	private static Screenshot screenshot = new Screenshot();
	
	private boolean isRestart = false;

	private static RecordResult recordResult = new RecordResult();
	
	private static KeyWordDriver keyWordDriver = null;
	
	private static boolean isAPPScreenshot = false;
	
	
	// 不是null 时, 标志执行场景还原
	private String SR = null;
	
	public Test(StartConfigBean startConfig,KeyWordDriver kwd) {

		keyWordDriver = kwd;
		if (Parameter.RUNMODE != null && Parameter.RUNMODE.equals("远程")) {
			IP = Parameter.REMOTEIP;
		}

		
		if(!startConfig.getRunTestModel().equalsIgnoreCase("浏览器")){
			
			try {
				isAPPScreenshot = true;
				AppDriver.init(startConfig.getAppFilePath(), IP);
			} catch (AppiumException e) {
				Print.log(e.getMessage(), 2);
				Print.log("用例停止执行", 2);
				return;
			}
			
		}else{
			// 浏览器类型，URL地址
			Driver.setDriver(Parameter.BROWSERTYPE, Parameter.URL, IP);
			try {
				Driver.startBrowser();
			} catch (SeleniumFindException e) {
				Print.log(e.getMessage(), 2);
			}
		}
		
		
		// 根据界面上的浏览器类型设置,赋值BrowerType
//		browserType = Parameter.BROWSERTYPE;
		
		// 配置路径
//		if(Parameter.NOTSERVER){
//			ConfigInfo.setConfigPath(map.get("ConfigPath"));
//		}else{
//			coreReader = new CoreReader(Parameter.YIGOPATH);
//			try {
//				ConfigInfo.setConfigPath(coreReader.getValueByKey("server.config"));
//			} catch (ParameterException e) {
//	
//			}
//		}

		// 测试平台驱动
//		if (Parameter.PLATVERSION.equals("1.4")) {
//			RunKeyWord.setKeyWordDriver(new KeyWordDriver1_4Web());
//		} else if (Parameter.PLATVERSION.equals("1.6")) {
//			RunKeyWord.setKeyWordDriver(new KeyWordDriver1_6Web());
//		}

//		if(map.get("StartPlatform").equals("true")){
//			//需要增加一个默认步骤
//			isRestart = true;
//		}
		
		
		

		// 是否提交服务器
		if(startConfig.getUploadResults()){
			recordResult.addRecordData(new UploadServerData());
		}
		
		// 默认步骤
//		defaultStep();
		

		try {
			FileUtil.copyFile(new File(Parameter.TESTCASEPATH), new File(Parameter.RESULT_FOLDER + "/test.xls"));
			ArrayList<String> Path = RegExp.matcherCharacters(Parameter.TESTCASEPATH, "[^\\\\/]{1,}");
			Parameter.EXCELNAME = RegExp.matcherCharacters(Path.get(Path.size() - 1), ".*(?=.xls)").get(0);
			Parameter.TESTCASEPATH = Parameter.RESULT_FOLDER + "/test.xls";
			// 新建报告目录
			Parameter.RESULT_FOLDER_REPORT = Parameter.RESULT_FOLDER + "/Report";
			FileUtil.copySourceToFolder("report", Parameter.RESULT_FOLDER_REPORT, this.getClass());
//			FileUtil.copyDirectiory(Parameter.REPORT_PATH, Parameter.RESULT_FOLDER_REPORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

		tc = new TestCaseExcel(Parameter.TESTCASEPATH);

		// 测试结果集
		recordResult.init();

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
		} else if (Parameter.EXECUTEMODE != null && Parameter.EXECUTEMODE.equals("固定模式执行")) {
			//这里取消掉 旧的模式, 固定模式 就是读sheet0的用例.
			runTimeSheetNum = 0;
			tc.activateSheet(runTimeSheetNum);
			RunLog.init(tc.getSheetLastRowNumber());
			executeTestCase();
//			runTimeSheetNum = 0;
//			tc.activateSheet(runTimeSheetNum);
//
//			RunLog.init(tc.getSheetLastRowNumber());
//			executeTestCase();
		} else { // 还没有处理是否执行前置用例
			tc.activateSheet(Integer.parseInt(startConfig.getSheetsNum()) - 1);
			runTimeSheetNum = Integer.parseInt(startConfig.getSheetsNum()) - 1;
			if (startConfig.getFrontSteps().equals("需要")) {
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
	
	private void close(){
		tc = null;

		relate = null;
		
		isRelate = false;
		
		resultPath = null;
		
		IP = null;

		/**
		 *  运行时记录用例步骤
		 */
		runTimeStepNum = 0;
		runTimeSheetNum = 0;
		runTimeRowNum = 0;

		// 截图
		screenshot = new Screenshot();
		
		isRestart = false;

		
		recordResult = new RecordResult();
		
		keyWordDriver = null;
		
		// 不是null 时, 标志执行场景还原
		SR = null;
	}

	/**
	 * 执行测试用例
	 */
	private void executeTestCase() {
		int count;
		int result = 1;
		
		//测试结果集
		recordResult.addSheet(runTimeSheetNum);
		
		//按模块执行
		for (int i = 0 ; i < tc.getExcelModuleStartIndex().size(); i++) {
			
			Parameter.TESTCASEMOUDLE = tc.getExcelModuleName().get(i);
			//测试结果集
			recordResult.addModule(Parameter.TESTCASEMOUDLE);
			
			int m = tc.getExcelModuleStartIndex().get(i);
			
			if(runTimeRowNum>tc.getExcelModuleStartIndex().get(i)){
				m = runTimeRowNum;
			}
			
			count = tc.getExcelModuleEndIndex().get(i);
			for (; m <= count; m++) {			
				
				runTimeRowNum = m;
				tc.setCurrentRow(m);
				result = runSteps(false);
				if (result != ExecuteStatus.SUCCESS) {
					Print.log("跳过: " + Parameter.TESTCASEMOUDLE,2);
					
					tc.setResult("跳过下个模块");
					
					SR = "场景还原/";
					// 执行下个模块
					break;
				} else{
					if(null != SR){
						Print.log("模块存在关联,跳过: " + Parameter.TESTCASEMOUDLE,2);
					}
				}
			}

		}
		runTimeRowNum = 0;
		//修正用例总数
		recordResult.addTestCaseCount("");
	}
	
	/**
	 *
	 * 运行测试用例    (用例行数执行     tc 所设定的行列)
	 *
	 * @param isRelated  改用例是否是相关用例执行
	 * @return
	 */
	private int runSteps(boolean isRelated) {
		
		
		
		int result = ExecuteStatus.SUCCESS;
		
		if(!isRelated){
			resultPath = runTimeSheetNum + "/" + runTimeRowNum;
		}else{
			resultPath = resultPath + "/relate/" + tc.getCurrentRow();
		}
		
		if (tc.isExecute()) {
			

			//加载别名数据
			String context = Test.getAssociatedProperites();
			TRA_NAME = null;
			if(null != context){
				Print.debugLog("开始加载别名数据", 0);
				TRA_NAME = TransformationMap.transformationByString(context);
				Print.log("加载别名数据完成", 0);
			}
			
			
			
			if(null != SR){
				
				String relateCase = tc.getRelatedNo();
				if(null != relateCase && !relateCase.isEmpty()){
					tc.setResult("跳过下个模块");
					
					return ExecuteStatus.SUCCESS;
				}
				scenarioReduction();
				
			}
			Parameter.TESTCASENO = tc.getTestCaseNo();
			if(isRelate){
				Print.log("当前用例编号:" + Parameter.TESTCASENO,0);
				String relateNo = relate.get(Parameter.TESTCASENO);
				if (relateNo!=null && !relateNo.equals("")){
					//记录原有的行列
					Print.log("相关用例编号:" + relateNo, 0);
					
					//记录结果集
					recordResult.addRelatedCase(relateNo);
					
					int originalSheet = tc.getExcelSheetIndex();
					int originalRow = tc.getCurrentRow();
					String[] location = relate.get(tc.getRelatedNo() + "_Location").split("_");
					//设置相关用例的行列
					tc.activateSheet(Integer.parseInt(location[0]));
					tc.setCurrentRow(Integer.parseInt(location[1]));
					runSteps(true);
					//还原相关用例的行列
					tc.activateSheet(originalSheet);
					tc.setCurrentRow(originalRow);
				}
			}
			String[] steps = null;
			if(null != tc.getTestStep()){
				steps = RegExp.splitWord(tc.getTestStep(), "\n");
			}
			
			if (null != steps && !steps[0].isEmpty()) {
				RunLog.setStepsForTextAreaByIndex(tc.getCurrentRow() + 1, steps, Parameter.TESTCASENO);
				int stepNum = runTimeStepNum;
				//测试结果集
				recordResult.addCase(Parameter.TESTCASENO);
				Parameter.CASESTATUS = 1;
				for (; stepNum < steps.length; stepNum++) {
					runTimeStepNum = stepNum;
					RunLog.highLightCurrentStep(stepNum);
					
					//测试结果集
					recordResult.addStep(steps[stepNum], String.valueOf(result));
					result = runSingleStep(steps[stepNum],resultPath + "/" + runTimeStepNum);
					recordResult.addCaseLog(steps[stepNum], result);
					if(result != ExecuteStatus.SUCCESS){

						// 异常截图
						Parameter.CASESTATUS = result;
						String bitMapPath = Parameter.RESULT_FOLDER_BITMAP + "/" + resultPath + "/" + runTimeStepNum + "_"+ steps[stepNum].split(":")[0] + "_error";
						bitMapPath = screenshot(bitMapPath);
						Parameter.VERSNAPSHOT = "snapshot/" + runTimeSheetNum + "_" 
								+ (resultPath + "/" + runTimeStepNum).replaceAll("/", "_") + "_error";
						screenshot(Parameter.RESULT_FOLDER_REPORT + "/" + Parameter.VERSNAPSHOT);
						Parameter.VERSNAPSHOT = Parameter.VERSNAPSHOT + ".png";
						Parameter.ERRORCAPTURE = Parameter.VERSNAPSHOT;
						if (RegExp.findCharacters(steps[stepNum], "^验证:")){
							result = 1;
							continue;
						}

						break;
					}
				}
				
				recordResult.addCaseReport();
//				setHyperLink(tc.getCurrentRow(),resultPath + "/" + runTimeStepNum);
				
//				CollateData.initializationParameter();
				runTimeStepNum = 0;
			}
		}

		tc.saveFile();

		return result;
	}

	
	
	/**
	 * 步骤执行
	 * @param step     步骤内容
	 * @param path     一个存储截图的路径相对路径
	 * @return
	 */
	private int runSingleStep(String step, String path) {
		if(null != SR){
			path = SR;
		}
		
		if (RegExp.findCharacters(step, "^验证")){
			Parameter.VERSNAPSHOT = "snapshot/"+ runTimeSheetNum + "_" 
									+ path.replaceAll("/", "_") + "_result";
			Parameter.CHECKPOINTNAME = step;
		}
		
		
		
		
		int results = 1;
//		try{
		results = keyWordDriver.start(RegExp.splitKeyWord(step));
//		} catch (NoSuchWindowException e2){
//			errString = e2.getMessage(); 
//			Print.log(errString, 2);
//			Parameter.ERRORLOG = errString;
//			return ExecuteStatus.FAILURE;
//		} catch (Exception e1){
//			errString = e1.getMessage(); 
//			Print.log(e1.getMessage(), 2);
//			Parameter.ERRORLOG = errString;
//			return ExecuteStatus.FAILURE;
//		}
		
		// 截图		
		Parameter.LOGSNAPSHOT = "snapshot/"+ runTimeSheetNum + "_" 
								+ path.replaceAll("/", "_") + "_log.png";
		
		String bitMapPath = Parameter.RESULT_FOLDER_BITMAP + "/" + path + "_"+ step.split(":")[0];
		bitMapPath = screenshot(bitMapPath);
//		bitMapPath = screenshot.snapShot(bitMapPath);
		try {
			FileUtil.copyFile(new File(bitMapPath), new File(Parameter.RESULT_FOLDER_REPORT+"/"+Parameter.LOGSNAPSHOT));
		} catch (IOException e) {
			results = -2;
			Parameter.ERRORLOG = e.getMessage();
			Print.log(e.getMessage(), 2);
			e.printStackTrace();
		}
		//记录结果集
		recordResult.addBitMap(bitMapPath);
		return results;
	}

	/**
	 * 执行场景还原
	 */
	private void scenarioReduction(){
		
		int rtstepn = runTimeStepNum;
		int rtsheetn = runTimeSheetNum;
		int rtrown = runTimeRowNum;
		int currentRow = tc.getCurrentRow();
		Print.log("环境出错,搜索还原场景步骤",2);
		runTimeRowNum = 0;
		runTimeStepNum = 0;
		
		try {
			tc.activateSheet("Scenario Reduction");
			
			Print.log("开始执行还原场景步骤",2);
			
			executeTestCase();
			
		} catch (Exception e) {
			
			Print.log("未找到还原场景步骤",2);
			
		} finally{
			SR = null;
			tc.activateSheet(rtsheetn);
			runTimeRowNum = rtrown;
			runTimeSheetNum = rtsheetn;
			runTimeStepNum = rtstepn;
			tc.setCurrentRow(currentRow);
		}
		

	}
	
	private static String screenshot(String bitMapPath){
		String path = null;
		if(isAPPScreenshot){
			path = AppDriver.screenshot(bitMapPath);
		}else{
			path = screenshot.snapShot(bitMapPath);
		}
		
		return path;
	}
	
	
	
	
//	private void defaultStep(){
//		// 默认步骤 这步是必须的
//		int defaultStep = 0;
//		
//		if(isRestart){
//			CallBat.closeMiddleware();
//			runSingleStep("启动", "默认步骤/");
//			defaultStep = defaultStep + 1;
//		}
//		
//		runSingleStep("运行", "默认步骤/" + defaultStep);
//		File file = new File(Parameter.DFAULT_TEST_PATH + "/DefaultStep.txt");
//		
//
//
//		try {
//			if (file.isFile() && file.exists()) { // 判断文件是否存在
//				InputStreamReader read;
//
//				read = new InputStreamReader(new FileInputStream(file));
//				//考虑到编码格式
//				BufferedReader bufferedReader = new BufferedReader(read);
//				String lineTxt = null;
//				while ((lineTxt = bufferedReader.readLine()) != null) {
//					defaultStep ++;
//					runSingleStep(lineTxt, "默认步骤/" + defaultStep);
//				}
//				read.close();
//			} else {
//				//默认步骤  未设置时的默认步骤
//				runSingleStep("录入:用户代码:administrator", "默认步骤/" + defaultStep);
//				// 增加831时设置帐套。
//				if (Parameter.TESTCASEPATH.indexOf("831") != -1){
//					runSingleStep("录入:账套:NewCorp", "默认步骤/" + defaultStep);
//				}
//				runSingleStep("点击:确定(&O)", "默认步骤/" + defaultStep);
//			}
//		} catch (IOException e) {
//			//默认步骤  未设置时的默认步骤
//			runSingleStep("录入:用户代码:administrator", "默认步骤/" + defaultStep);
//			//  增加831时设置帐套。
//			if (Parameter.TESTCASEPATH.indexOf("831") != -1){
//				runSingleStep("录入:账套:NewCorp", "默认步骤/" + defaultStep);
//			}
//			runSingleStep("点击:确定(&O)", "默认步骤/" + defaultStep);
//		}
////		ConfigInfo.currentBillKey();
////		Driver.runScript("Web_ShowDeskUI_JS('')");
//	}
	
//	/**
//	 * 获取下一行用例
//	 * @return String[]
//	 */ 
//	public static String[] getNextStep(){
//		TestCase testCase = tc;
//		String[] steps = RegExp.splitWord(testCase.getTestStep(), "\n");
//		int runTimeStepNum = Test.runTimeStepNum;
//		String nextStep;
//		if (steps.length-1 > runTimeStepNum){
//			nextStep = steps[runTimeStepNum +1]; // 下一行用例
//		}else{
//			steps = RegExp.splitWord(testCase.getNextTestStep(), "\n");
//			nextStep = steps[0]; // 下一行用例
//		}
//		String[] nextKeys = RegExp.splitKeyWord(nextStep);
//		return nextKeys;
//	}
	
	
	/**
	 * 获取预期结果，第5列
	 * @return 字符串
	 */
	public static String getExpectedResult(){
		excelExist();
		return tc.getExpected();
	}
	
	/**
	 * 获取excel实际结果内容，第6列
	 * @return
	 */
	public static String getActulResult(){
		excelExist();
		return tc.getActual();
	}
	
	/**
	 * 获取excel关联属性，第6列
	 * @return
	 */
	public static String getAssociatedProperites(){
		excelExist();
		return tc.getAssociatedProperites();
	}
	
	
	
	/**
	 * 实际结果内容写入excel，第6列
	 * @param value
	 */
	public static void setAcutal(String value){
		excelExist();
		tc.setAcutal(value);
		recordResult.addActualResults(value.split("\n"));
	}
	/**
	 * 写入sql结果。 第7列
	 * @param value
	 */
	public static void setSqlResults(String value){
		excelExist();
		tc.setSQLResults(value);
		
	}
	/**
	 * 对比结果写入excel  true||false，第8列
	 * @param value
	 */
	public static void setAcutalResult(String value){
		excelExist();
		tc.setResult(value);
	}
	/**
	 * 写入对比结果，设置字体颜色、
	 * @param value
	 */
	public static void setStyleAcutalResult(String value, String style){
		excelExist();
//		tc.setResult(value,style);
		tc.setResult(value);
	}
	/**
	 * 对比结果写入excel  true||false，第8列
	 * @param value
	 */
	public static void setAcutalResult(boolean value){
		excelExist();
		tc.setResult(String.valueOf(value));
		recordResult.addExpectedResults(tc.getExpected().split("\n"));
		recordResult.addResult(value);
	}
	/**
	 * 对比结果写入excel  true||false，第8列
	 * @param value
	 */
	public static void setAcutalResult(String expectedResult, boolean value){
		excelExist();
		tc.setResult(String.valueOf(value));
		recordResult.addExpectedResults(expectedResult.split("\n"));
		recordResult.addResult(value);
	}
	
	/**
	 * 获取预期结果，第5列
	 * @return 数组
	 */
	public static String[] getArrExpectedResult(){
		excelExist();
		String[] steps = new String[0];
		String expectedStr = tc.getExpected();
		if (!expectedStr.isEmpty()){
			steps = RegExp.splitWord(expectedStr, "\n");
		}else{
			RunLog.printLog("预期结果为空！", 2);
		}
		return steps;
	}
	
	public static void excelExist(){
		if (tc == null){
			throw new SeleniumException("Excel不存在。");
		}
	}
	
	/**
	 * 获取用例所有要执行的用例个数
	 * @return
	 */
	public static int getAllCaseSum(){
		return tc.getAllCase();
	}
	
//	/**
//	 * 获取实际生成的sql， 第10列
//	 * @return
//	 */
//	public static String getActualSql(){
//		return tc.getSQLActual();
//		
//	}
//	/**
//	 * 写入实际生成的sql，第11列
//	 * @return
//	 */
//	public static void setActualSql(String value){
//		tc.setSQLAcutal(value);
//	}
//	/**
//	 * 写入实际生成的sql ，第11列
//	 * @return
//	 */
//	public static void setActualSql(boolean value){
//		tc.setSQLAcutal(String.valueOf(value));
//	}
	
//	/**
//	 * 写入sql 第10列
//	 * @param value
//	 */
//	public static void setSql(String value){
//		tc.setSQL(value);
//	}
	
	/**
	 * 设置超链接
	 * @param index 列
	 * @param path	超链接地址
	 */
	public static void setHyperLink(int index, String path){
		excelExist();
		tc.setHyperLinks(index, path);
	}
	
	
	public static int getCurrentRowCount(){
		excelExist();
		return tc.getSheetLastRowNumber();
	}
	
	public static int getSheetIndex(){
		excelExist();
		return tc.getExcelSheetIndex();
	}
	
	public static int getCurrentRow(){
		excelExist();
		return tc.getCurrentRow();
	}
	
}
