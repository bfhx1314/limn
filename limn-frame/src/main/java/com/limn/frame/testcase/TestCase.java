package com.limn.frame.testcase;

import java.io.FileNotFoundException;
import java.util.HashMap;

import com.limn.tool.exception.ExcelEditorException;

public interface TestCase {
	

	/**
	 * 当前sheet页的最后一行的编号
	 * @return 行号
	 */
	public int getSheetLastRowNumber();

	/**
	 * 返回测试用例模块的名称集合
	 * @return
	 */
	public HashMap<Integer, String> getExcelModuleName();

	/**
	 * 返回测试用例模块起始点的集合
	 * @return
	 */
	public HashMap<Integer, Integer> getExcelModuleStartIndex();
	
	/**
	 * 返回测试用例模块的结束点集合
	 * @return
	 */
	public HashMap<Integer, Integer> getExcelModuleEndIndex();


	/**
	 * 设置当前行的位置
	 * @param index
	 */
	public void setCurrentRow(int index);
	
	/**
	 * 激活sheet页
	 * @param sheetIndex
	 */
	public void activateSheet(int sheetIndex) ;

	/**
	 * 激活sheet页
	 * @param sheetName
	 */
	public void activateSheet(String sheetName) throws ExcelEditorException;
	
	/**
	 * 是否执行
	 * @return
	 */
	public Boolean isExecute();

	/**
	 * 测试用例编号
	 * @return
	 */
	public String getTestCaseNo();
	
	/**
	 * 测试用例的前置用例
	 * @return
	 */
	public String getRelatedNo();
	
	/**
	 * 测试用例步骤
	 * @return
	 */
	public String getTestStep();
	
//	/**
//	 * 
//	 * @return
//	 */
//	public String getNextTestStep();
	
	/**
	 * 测试用例的预期结果
	 * @return
	 */
	public String getExpected();
	
	/**
	 * 测试用例的实际结果
	 * @return
	 */
	public String getActual();
	
	/**
	 * 测试用例的数据库结果
	 * @return
	 */
	public String getSQLResults();
	
	/**
	 * 测试用例的执行结果
	 * @return
	 */
	public String getResult();
	
	/**
	 * 获取关联属性
	 * @return
	 */
	public String getAssociatedProperites();
	
	/**
	 * 设置是否执行
	 * @param value 1 or 0
	 */
	public void setExecuted(String value);
	
	/**
	 * 设置测试用例编号
	 * @param value
	 */
	public void setTestCaseNo(String value);
	
	/**
	 * 设置测试用例的前置用例编号
	 * @param value
	 */
	public void setTestRelatedNo(String value);
	
	/**
	 * 设置测试用例步骤
	 * @param value
	 */
	public void setTestStep(String value);
	
	/**
	 * 设置测试用例的实际结果
	 * @param value
	 */
	public void setAcutal(String value);
	
	/**
	 * 设置测试用例的执行结果
	 * @param value
	 */
	public void setResult(String value);
	
	/**
	 * 设置关联属性
	 * @param value
	 */
	public void setAssociatedProperites(String value);
	
//	/**
//	 * 写入第8列
//	 * @param value 值
//	 * @param result 用于设置字体颜色
//	 */
//	public void setResult(String value, String style);
	
	/**
	 * 设置测试用例的数据库结果
	 * @param value
	 */
	public void setSQLResults(String value);

	/**
	 * 获取当前的行号
	 * @return
	 */
	public int getCurrentRow();

	/**
	 * 获取Excel的sheet数量
	 * @return
	 */
	public int getSheetSize();

	/**
	 * 设置模块名称
	 * @param index 模块index
	 * @param moduleName 名称
	 */
	public void setModuleName(int index, String moduleName);

	public void insertRow(int row);

	public boolean deleteRow(int rowNum);

	/**
	 * 设置测试用例的预期结果
	 * @param value
	 */
	public void setResults(String value);

	/**
	 * 刷新测试用例模块
	 */
	public void refreshModule();

	/**
	 * 保存
	 */
	public boolean saveFile();

	
	public void shiftRows(int start,int end,int move);


	public HashMap<String,String> getTestCaseRelateNoByNo();

	/**
	 * 获取当前sheet页index
	 * @return
	 */
	public int getExcelSheetIndex();
	
	/**
	 * 设置超链接
	 * @param index 列
	 * @param path 超链接地址
	 */
	public void setHyperLinks(int index, String path);

}
