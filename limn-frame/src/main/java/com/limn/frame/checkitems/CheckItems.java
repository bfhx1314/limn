package com.limn.frame.checkitems;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.tmatesoft.sqljet.core.internal.lang.SqlParser.savepoint_stmt_return;

import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.frame.control.Test;
import com.limn.tool.common.Common;
import com.limn.tool.common.ConvertCharacter;
import com.limn.tool.common.TransformationMap;
import com.limn.tool.exception.ParameterException;
import com.limn.tool.regexp.RegExp;
import com.thoughtworks.selenium.SeleniumException;

/**
 * 验证界面上的数据
 * @author snow
 *
 */
public class CheckItems {
	
//	private IGetWebElmentByKey getWebElement = null;
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
	/**
	 * 所有的结果内容
	 */
	private String acutalAllStep = ""; 
	/**
	 * 控件信息。
	 */
	private Map<String, String> map = null;
	/**
	 * 控件id
	 */
	private String controlId = "";
	/**
	 * 控件类型
	 */
	private String controlType = "";
	/**
	 * 控件字段类型
	 */
	private String fieldType = "";
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
	 * 表格控件
	 */
	private boolean isGridControl = false;

	/**
	 * 表格列
	 */
	private int column = -1;

	/**
	 * 获取对象。
	 * @return
	 */
	public WebElement getWebElement() {
		return webElement;
	}
	int controlTypeNum = -1;
	
	public CheckItems(){}

//	public CheckItems(IGetWebElmentByKey iGetWebElmentByKey){
//		ConfigInfo.getBillPath();
//		this.getWebElement = iGetWebElmentByKey;
//	}
	/**
	 * 初始化实例变量
	 */
	public void initializationParameter(){
		this.controlId = "";
		this.controlType = "";
		this.fieldType = "";
		this.webElement = null;
		this.isGridControl = false;
		this.map = null;
		this.column = -1;
	}
	
	/**
	 * 关键字分支
	 * @param keys
	 * @throws ParameterException 
	 */
	public void branch(String[] keys) throws ParameterException{
		// TODO debug的时候需要增加xpath步奏、对比时读取xpath然后取值，
		this.expectedResults = Test.getArrExpectedResult();
		for(int i=0;i<expectedResults.length;i++){
			// 把用例中的"\:" "\;" 转义。
			expectedStr = ConvertCharacter.getHtmlAsc(expectedResults[i]);
			expectedKeys = RegExp.splitKeyWord(expectedStr);
			if (keys[0].equals("验证")){
				check();
//				verification(keys);
//			}else if(keys[0].equals("查看")){
//				check(keys);
//			}else if(keys[0].equals("核对")){
////				collate(keys);
//				break;
			}else{
				throw new ParameterException("关键字错误："+keys[0]);
			}
//			if (!keys[0].equals("核对")){
				// 对比结果
				disposeResult();
//			}
			// 初始化实例变量
			initializationParameter();
		}
		// 把结果写入excel。
		writeResult();
		System.out.println(Test.getActulResult());
		System.out.println(boolActul);
	}
	
	/**
	 * 验证
	 * @throws ParameterException 
	 */
	public void check() throws ParameterException{
		// 获取webelement对象
//		webElement = (expectedKeys[1]);
		String xpath = expectedKeys[0];
		actulResults = checkControlData();
	}
	
	/**
	 * 验证，对比结果
	 * @param xpath 
	 * @return
	 * @throws ParameterException 
	 */
	private String[] checkControlData() throws ParameterException {
		String acutalResult = getControlData(expectedKeys); // 实际界面上获取到的结果。
		boolean boolResult = true; // 结果 true||false
//		boolean isCDbl = doesChangeDouble(); //Decimal Int
		//与预期结果做对比，
//		if (expectedKeys[0].equals("控件")){
//			// 判断值为空
//			if (expectedKeys[3].equals("空")){
//				if (!acutalResult.isEmpty()){
//					boolResult = false;
//					expectedKeys[3] = acutalResult;
//				}
//			}else{
//				if (isCDbl){
//					if (Double.parseDouble(expectedKeys[3]) != Double.parseDouble(acutalResult)){
//						boolResult = false;
//					}
//				}else{
//					if (!ConvertCharacter.getHtmlChr(expectedKeys[3]).equals(acutalResult)){
//						boolResult = false;
//					}
//				}
//				expectedKeys[3] = acutalResult;
//			}
//		}else{
//			if (isCDbl){
//				if (Double.parseDouble(expectedKeys[1]) != Double.parseDouble(acutalResult)){
//					boolResult = false;
//				}
//			}else{
				// 预期结果中只写CODE
//				String[] arrNewExp = expectedKeys[1].split(" ");
//				if (arrNewExp.length == 1){
//					if (!acutalResult.isEmpty()){
//						String[] arrNewAct = acutalResult.split(" ");
//						acutalResult = arrNewAct[0];
//					}
//				}
		String expectValue = null;
		try {
			try{
				int inT = Integer.parseInt(expectedKeys[1]);
				expectValue = null;
			}catch(Exception e){
				expectValue = Common.getExpressionValue(expectedKeys[1]);
			}
			expectedKeys[1] = expectValue == null ? expectedKeys[1] : expectValue;
			
		} catch (ParameterException e) {
			
			throw new ParameterException("语法解析失败，表达式：" + expectedKeys[1]);
			
		}
		
				if (!ConvertCharacter.getHtmlChr(expectedKeys[1]).equals(acutalResult)){
					boolResult = false;
//					expectedKeys[1] = acutalResult;
				}
//			}
			expectedKeys[1] = acutalResult;
//		}
		String[] arr = {StringUtils.join(expectedKeys," "), String.valueOf(boolResult)};
		return arr;
	}
	
	/**
	 * 处理结果。得到实际结果、结果内容。
	 */
	private void disposeResult(){
		String acutalStep = actulResults[0];
		if (actulResults[1].equalsIgnoreCase("false")){
			boolActul = false;
		}
		if (acutalAllStep == ""){
			acutalAllStep = acutalStep;
		}else{
			acutalAllStep = acutalAllStep + "\n" + acutalStep;
		}
	}
	
	/**
	 * 把结果写入excel
	 */
	private void writeResult(){
		String acutalResults = Test.getActulResult();
		if (acutalResults == "" || acutalResults == null){
			acutalResults = acutalAllStep;
		}else{
			acutalResults = acutalResults + "\n" + acutalAllStep;
		}
		Test.setAcutal(acutalResults);
		Test.setAcutalResult(boolActul);
	}
	
	/*------------------------------------------------------------------------------------*/	
	/*--------------------------------下面的方法用于调试时调用-----------------------------*/
	/*-----------------------------------------------------------------------------------*/
	/**
	 * 获取控件的值（调试)
	 * @param xpath
	 * @return
	 */
	public String getControlData(String[] step){
		String acutalResult = "";
		try{
			HashMap<String,String> traXPath = null; 
			if(step.length >= 3 && RegExp.findCharacters(step[2], "^HASHMAP")){
				traXPath = TransformationMap.transformationByString(step[2]);
			}else{
				String context = Test.getAssociatedProperites();
				if(null == context || context.equals("")){
					traXPath = null;
				}else{
					traXPath = TransformationMap.transformationByString(context);
				}
			}

			String xpath = null;
			if(null != traXPath){
				if(traXPath.containsKey(step[0])){
					xpath = traXPath.get(step[0]);
				}else{
					xpath = step[0];
				}
			}else{
				xpath = step[0];
			}
			
			webElement = Driver.getWebElementBylocator(xpath);
			if (webElement != null){
				acutalResult = webElement.getAttribute("value");
//				if (isMultiDict){
//					if (acutalResult.indexOf(";") != -1){
//						acutalResult = acutalResult.replaceAll(";", ",");
//					}
//				}
			}else{
				throw new SeleniumException("xpath没有定位到："+xpath);
			}
//			Driver.setValue(xpath,step[2]);
			
		}catch(Exception e){
			new SeleniumFindException("错误:" + e.getMessage());
//			Print.log(e.getMessage(), 2);
		}

			// 单选、多选框
//			if(controlType.equalsIgnoreCase("CHECKBOX")){
//				acutalResult = String.valueOf(webElement.isSelected());
//			}
		return acutalResult;
	}

	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
