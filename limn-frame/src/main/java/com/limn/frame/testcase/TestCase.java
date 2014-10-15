package com.limn.frame.testcase;

import java.io.FileNotFoundException;
import java.util.HashMap;

public interface TestCase {
	

	public int getSheetLastRowNumber();

	public HashMap<Integer, String> getExcelModuleName();

	public HashMap<Integer, Integer> getExcelModuleStartIndex();
	
	public HashMap<Integer, Integer> getExcelModuleEndIndex();

//	public void setTableSheet(int index);

	public void setCurrentRow(int index);
	
	public void activateSheet(int sheetIndex);

	public Boolean isExecute();

	public String getTestCaseNo();
	
	public String getRelatedNo();
	
	public String getTestStep();
	
	public String getNextTestStep();
	
	public String getExpected();
	
	public String getActual();
	
	public String getSQLResults();
	
	public String getResult();
	
	public void setExecuted(String value);
	
	public void setTestCaseNo(String value);
	
	public void setTestRelatedNo(String value);
	
	public void setTestStep(String value);
	
	public void setAcutal(String value);
	
	public void setResult(String value);
	
	/**
	 * 写入第8列
	 * @param value 值
	 * @param result 用于设置字体颜色
	 */
	public void setResult(String value, String style);
	
	public void setSQLResults(String value);

	public int getCurrentRow();

	public int getSheetSize();

	public void setModuleName(int index, String moduleName);

	public void insertRow(int row);

	public void deleteRow(int rowNum);

	public void setResults(String value);

	public void refreshModule();

	public void saveFile();

	public void shiftRows(int start,int end,int move);

//	public void createBook(String path);

	public HashMap<String,String> getTestCaseRelateNoByNo();

	public int getExcelSheetIndex();
	
	/**
	 * 设置超链接
	 * @param index 列
	 * @param path 超链接地址
	 */
	public void setHyperLinks(int index, String path);
}
