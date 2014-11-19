package com.limn.frame.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;










import org.openqa.selenium.NoSuchWindowException;

import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.keyword.KeyWordDriver;
import com.limn.frame.results.RecordResult;
import com.limn.frame.results.UploadServerData;
import com.limn.frame.testcase.TestCase;
import com.limn.frame.testcase.TestCaseExcel;
import com.limn.tool.log.RunLog;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.common.CallBat;
import com.limn.tool.common.FileUtil;
import com.limn.tool.common.Print;
import com.limn.tool.common.Screenshot;
import com.limn.tool.exception.ExcelEditorException;
import com.limn.tool.external.ExcelEditor;
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
	
	private String SR = null;
	
	public Test(HashMap<String, String> map,KeyWordDriver kwd) {
		
		keyWordDriver = kwd;
		
		if (Parameter.RUNMODE!=null && Parameter.RUNMODE.equals("远程")) {
			IP = Parameter.REMOTEIP;
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
		
		// 浏览器类型，URL地址
//		Driver.setDriver(browserType, URL, IP);

		// 是否提交服务器
		if(map.containsKey("UploadResults") && Boolean.valueOf(map.get("UploadResults"))){
			recordResult.addRecordData(new UploadServerData());
		}
		
		// 默认步骤
//		defaultStep();
		

		try {
			FileUtil.copyFile(new File(Parameter.TESTCASEPATH), new File(Parameter.RESULT_FOLDER + "/test.xls"));
			ArrayList<String> Path = RegExp.matcherCharacters(Parameter.TESTCASEPATH, "[^\\\\/]{1,}");
			Parameter.EXCELNAME = RegExp.matcherCharacters(Path.get(Path.size() - 1), ".*(?=.xls)").get(0);
			Parameter.TESTCASEPATH = Parameter.RESULT_FOLDER + "/test.xls";
		} catch (IOException e) {
			e.printStackTrace();
		}

		tc = new TestCaseExcel(Parameter.TESTCASEPATH);

		// 测试结果集
		recordResult.init();

		if (map.containsKey("Specify") && map.get("Specify").equals("指定")) {
			runTimeSheetNum = Integer.parseInt(map.get("SpecifySheet")) - 1;
			runTimeRowNum = Integer.parseInt(map.get("SpecifyRow")) - 1;
			runTimeStepNum = Integer.parseInt(map.get("SpecifyStep")) - 1;
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
			tc.activateSheet(Integer.parseInt(map.get("SheetsNum")) - 1);
			runTimeSheetNum = Integer.parseInt(map.get("SheetsNum")) - 1;
			if (map.get("FrontSteps").equals("需要")) {
				isRelate = true;
				relate = tc.getTestCaseRelateNoByNo();
			}
			RunLog.init(tc.getSheetLastRowNumber());
			executeTestCase();
		}

		tc.saveFile();
	
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
			
			//测试结果集
			recordResult.addModule(tc.getExcelModuleName().get(i));
			
			int m = tc.getExcelModuleStartIndex().get(i);
			
			if(runTimeRowNum>tc.getExcelModuleStartIndex().get(i)){
				m = runTimeRowNum;
			}
			
			count = tc.getExcelModuleEndIndex().get(i);
			for (; m <= count; m++) {			
				
				runTimeRowNum = m;
				tc.setCurrentRow(m);
				result = runSteps(false);
				if (result != 1) {
					tc.setResult("跳过下个模块");
					
					scenarioReduction();
					// 执行下个模块
					break;
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
		
		int result = 1;
		
		if(!isRelated){
			resultPath = runTimeSheetNum + "/" + runTimeRowNum;
		}else{
			resultPath = resultPath + "/relate/" + tc.getCurrentRow();
		}
		
		if (tc.isExecute()) {
			
			
			
			
			if(isRelate){
				Print.log("当前用例编号:" + tc.getTestCaseNo(),0);
				String relateNo = relate.get(tc.getTestCaseNo());
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
			
			
			String[] steps = RegExp.splitWord(tc.getTestStep(), "\n");

			if (!steps[0].isEmpty()) {
				RunLog.setStepsForTextAreaByIndex(tc.getCurrentRow() + 1, steps);
				int stepNum = runTimeStepNum;
				//测试结果集
				recordResult.addCase(tc.getTestCaseNo());
				for (; stepNum < steps.length; stepNum++) {
					runTimeStepNum = stepNum;
					RunLog.highLightCurrentStep(stepNum);
					
					//测试结果集
					recordResult.addStep(steps[stepNum], String.valueOf(result));
					result = runSingleStep(steps[stepNum],resultPath + "/" + runTimeStepNum);
					if(result != 1){
						break;
					}
				}
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
		int results = 1;
		
		try{
			results = keyWordDriver.start(RegExp.splitKeyWord(step));
		} catch (NoSuchWindowException e2){
			Print.log(e2.getMessage(), 2);
			return -2;
		} catch (Exception e1){
			Print.log(e1.getMessage(), 2);
			return -2;
		}
		
		// 截图
		String bitMapPath = Parameter.RESULT_FOLDER_BITMAP + "/" + path + "_"+ step.split(":")[0];
		
		bitMapPath = screenshot.snapShot(bitMapPath);
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
		Print.log("环境出错,搜索还原场景步骤",2);
		runTimeRowNum = 0;
		runTimeStepNum = 0;
		
		try {
			tc.activateSheet("Scenario Reduction");
			SR = "场景还原/";
			Print.log("开始执行还原场景步骤",2);
			executeTestCase();
		} catch (ExcelEditorException e) {
			throw new SeleniumException("场景还原异常:" + e.getMessage());
			
		} finally{
			SR = null;
		}
		
		tc.activateSheet(rtsheetn);
		runTimeRowNum = rtrown;
		runTimeSheetNum = rtsheetn;
		runTimeStepNum = rtstepn;
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
		return tc.getExpected();
	}
	
	/**
	 * 获取excel实际结果内容，第6列
	 * @return
	 */
	public static String getActulResult(){
		return tc.getActual();
	}
	
	/**
	 * 获取excel实际结果内容，第6列
	 * @return
	 */
	public static String getAssociatedProperites(){
		return tc.getAssociatedProperites();
	}
	
	
	
	/**
	 * 实际结果内容写入excel，第6列
	 * @param value
	 */
	public static void setAcutal(String value){
		tc.setAcutal(value);
		recordResult.addActualResults(value.split("\n"));
	}
	/**
	 * 写入sql结果。 第7列
	 * @param value
	 */
	public static void setSqlResults(String value){
		tc.setSQLResults(value);
		
	}
	/**
	 * 对比结果写入excel  true||false，第8列
	 * @param value
	 */
	public static void setAcutalResult(String value){
		tc.setResult(value);
	}
	/**
	 * 写入对比结果，设置字体颜色、
	 * @param value
	 */
	public static void setStyleAcutalResult(String value, String style){
//		tc.setResult(value,style);
		tc.setResult(value);
	}
	/**
	 * 对比结果写入excel  true||false，第8列
	 * @param value
	 */
	public static void setAcutalResult(boolean value){
		tc.setResult(String.valueOf(value));
		recordResult.addExpectedResults(tc.getExpected().split("\n"));
		recordResult.addResult(value);
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
		tc.setHyperLinks(index, path);
	}
	
	
	public static int getCurrentRowCount(){
		return tc.getSheetLastRowNumber();
	}
	
	public static int getSheetIndex(){
		return tc.getExcelSheetIndex();
	}
	
	public static int getCurrentRow(){
		return tc.getCurrentRow();
	}
	
}
