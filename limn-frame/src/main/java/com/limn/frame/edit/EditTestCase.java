package com.limn.frame.edit;

import java.io.FileNotFoundException;
import java.util.HashMap;

import com.limn.frame.testcase.TestCase;
import com.limn.frame.testcase.TestCaseExcel;




/**
 * 测试用例编辑的方法  操作excel
 * @author limn
 *
 */
public class EditTestCase{
	
	private TestCase testCase = null;
	private HashMap<Integer,String> moduleName = new HashMap<Integer,String>();
	private HashMap<Integer,Integer> moduleStartByIndex = new HashMap<Integer,Integer>();
	private HashMap<Integer,Integer> moduleEndByIndex = new HashMap<Integer,Integer>();
	
	public EditTestCase(){
		testCase = new TestCaseExcel();
	}
	
	public void openTestCase(String filePath){
		testCase.init(filePath, 0);
		refreshModuleData();
	}
	
	private void refreshModuleData(){
		moduleName = testCase.getExcelModuleName();
		moduleStartByIndex = testCase.getExcelModuleStartIndex();
		moduleEndByIndex = testCase.getExcelModuleEndIndex();
	}
	
	public String getModuleNameByIndex(int index){
		return moduleName.get(index);
	}
	
	public int getModuleIndexByIndex(int index){
		return moduleStartByIndex.get(index);
	}
	
	public int getModuleCount(){
		return moduleStartByIndex.size();
	}
	
	
	public String[][] getModuleTestCaseByIndex(int index){
		int start = moduleStartByIndex.get(index);
		
		int end = moduleEndByIndex.get(index);
		
		int countRow = end - start;

		String[][] testCaseStep = new String[countRow][5];
		
		for (int i = 0; i < countRow; i++){
			testCase.setCurrentRow(start);
			start ++ ;
			testCaseStep[i][0] = testCase.isExecute()?"1":"0";
			testCaseStep[i][1] = testCase.getTestCaseNo();
			testCaseStep[i][2] = testCase.getRelatedNo();
			testCaseStep[i][3] = testCase.getTestStep();
			testCaseStep[i][4] = testCase.getExpected();
		}
		return testCaseStep;
	}
	

	public int getSheetRowCount(){
		return testCase.getSheetCount();
	}
	
	public void setTableSheet(int index){
		testCase.setTableSheet(index);
		refreshModuleData();
	}
	
	
	public void setModuleNameByIndex(int index, String moduleName){
		testCase.setModuleName(index,moduleName);
		refreshModuleData();
	}
	
	public void saveModuleCase(int index,String[][] testCaseTable) throws FileNotFoundException{
		int count = testCaseTable.length;
		
		int start = moduleStartByIndex.get(index);
		int end = moduleEndByIndex.get(index);

		int diff = count - (end - start);
		
		if(diff>0){
			while(diff!=0){
				testCase.insertRow(start);
				diff--;
			}
		}else if(diff<0){
			while(diff!=0){
				testCase.deleteRow(start);
				diff++;
			}
		}
		
		testCase.save();
		
		
		
		
		for(int rowIndex = 0;rowIndex < count; rowIndex++ ){
			testCase.setCurrentRow(start + rowIndex);
			testCase.setExecuted(testCaseTable[rowIndex][0]);
			testCase.setTestCaseNo(testCaseTable[rowIndex][1]);
			testCase.setTestRelatedNo(testCaseTable[rowIndex][2]);
			testCase.setTestStep(testCaseTable[rowIndex][3]);
			testCase.setResults(testCaseTable[rowIndex][4]);
		}
		
		testCase.save();
		refreshModuleData();
	}

	
	
	public void addModuleCase(int index,String moduleName) throws FileNotFoundException{
		int row = 2;
		if(index>1){
			row = moduleEndByIndex.get(index - 1) + 1;
		}
		
		
		testCase.insertRow(row);
		testCase.insertRow(row + 1);
		testCase.insertRow(row + 2);
		testCase.save();
		
		
		testCase.setCurrentRow(row);
		testCase.setTestCaseNo(moduleName);
		testCase.setCurrentRow(row + 1);
		
		testCase.setExecuted("是否执行");
		testCase.setTestCaseNo("用例编号");
		testCase.setTestRelatedNo("相关用例");
		testCase.setTestStep("用例步骤");
		testCase.setResults("预期结果");
		testCase.setAcutal("实际结果");
		testCase.setSQLResults("数据库结果");
		testCase.setResult("是否通过");
		

		
		testCase.save();
		
		refreshModuleData();
		
	}
	
	public void deleteModuleCase(int index) throws FileNotFoundException{
		int rowStart = moduleStartByIndex.get(index) - 2;
		int rowEnd = moduleEndByIndex.get(index);
		
		while(rowEnd-rowStart >= 0){
			testCase.deleteRow(rowStart);
			rowEnd --;
		}
		testCase.save();
		refreshModuleData();
	}
	
	
	public void upModuleCase(int index) throws FileNotFoundException{
		int rowStart = moduleStartByIndex.get(index) - 2;
		int rowEnd = moduleEndByIndex.get(index);
		int upRowStart = moduleStartByIndex.get(index - 1) - 2;
		int upRowEnd = moduleEndByIndex.get(index - 1);
		
		int diffFirstRow = rowStart - upRowStart;
		int diffEndRow = rowEnd - upRowEnd;
		int move = testCase.getTableSheetCount() + 1 - rowStart;
		testCase.shiftRows(rowStart, rowEnd, move);

		testCase.shiftRows(upRowStart, upRowEnd, diffEndRow);

		testCase.shiftRows(rowStart + move, rowEnd + move, -(move + diffFirstRow));
		testCase.save();
		refreshModuleData();
		
	}

	public void createBook(String path) {
		testCase.createBook(path);
		
	}

	public void setSavePath(String path){
		testCase.setSaveFilePath(path);
	}
	
	
	public void Save() throws FileNotFoundException{
		testCase.save();
	}
	
	
}
