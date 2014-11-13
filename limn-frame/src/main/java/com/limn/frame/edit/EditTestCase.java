package com.limn.frame.edit;

import java.io.FileNotFoundException;
import java.util.HashMap;

import com.limn.frame.testcase.TestCase;
import com.limn.frame.testcase.TestCaseExcel;
import com.limn.tool.common.Print;




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
		
	}
	
	public void openTestCase(String filePath){
		testCase = new TestCaseExcel(filePath);
		testCase.activateSheet(0);
		refreshModuleData();
	}
	
	/**
	 * 刷新模块
	 */
	private void refreshModuleData(){
		testCase.refreshModule();
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
	
	/**
	 * 获取模块列表信息
	 * @param index
	 * @return
	 */
	public String[][] getModuleTestCaseByIndex(int index){
		
		int start = moduleStartByIndex.get(index);
		
		int end = moduleEndByIndex.get(index);
		
		int countRow = end - start + 1;

		String[][] testCaseStep = new String[countRow][6];
		
		for (int i = 0; i < countRow; i++){
			testCase.setCurrentRow(start);
			start ++ ;
			testCaseStep[i][0] = testCase.isExecute()?"1":"0";
			testCaseStep[i][1] = testCase.getTestCaseNo();
			testCaseStep[i][2] = testCase.getRelatedNo();
			testCaseStep[i][3] = testCase.getTestStep();
			testCaseStep[i][4] = testCase.getExpected();
			testCaseStep[i][5] = testCase.getExpected();
		}
		return testCaseStep;
	}
	

	public int getSheetRowCount(){
		return testCase.getSheetSize();
	}
	
	public void setTableSheet(int index){
		testCase.activateSheet(index);
		refreshModuleData();
	}
	
	
	public void setModuleNameByIndex(int index, String moduleName){
		testCase.setModuleName(index,moduleName);
		refreshModuleData();
	}
	
	public void saveModuleCase(int index,String[][] testCaseTable){
		int count = testCaseTable.length;
		refreshModuleData();
		
		int start = moduleStartByIndex.get(index);
		int end = moduleEndByIndex.get(index);
		//0行和1行 都是1行
		int orgCount = count;
		if(count>0){
			count--;
		}
		int diff = count - (end - start);

		Print.log("Start:" + start + " End:" + end + " diff:" + diff,2);
		
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
		testCase.saveFile();
		
		//这里行数需要正常
		if(orgCount > 0){
			count++;
		}
		for(int rowIndex = 0;rowIndex < count; rowIndex++ ){
			testCase.setCurrentRow(start + rowIndex);
			testCase.setExecuted(testCaseTable[rowIndex][0]);
			testCase.setTestCaseNo(testCaseTable[rowIndex][1]);
			testCase.setTestRelatedNo(testCaseTable[rowIndex][2]);
			testCase.setTestStep(testCaseTable[rowIndex][3]);
			testCase.setResults(testCaseTable[rowIndex][4]);
			testCase.setAssociatedProperites(testCaseTable[rowIndex][5]);
			refreshModuleData();
			testCase.saveFile();
			
		}
	}

	
	
	public void addModuleCase(int index,String moduleName){
		int row = 0;
		if(index > 0){
			//上一个模块的最后一行+1
			row = moduleEndByIndex.get(index - 1) + 1;
		}
		
		
		
		testCase.insertRow(row);
		testCase.insertRow(row + 1);
		testCase.insertRow(row + 2);
		testCase.saveFile();
		
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
		testCase.setAssociatedProperites("关联属性");
		
		
		testCase.saveFile();
		refreshModuleData();
		
	}
	
	public boolean deleteModuleCase(int index){
		int rowStart = moduleStartByIndex.get(index) - 2;
		int rowEnd = moduleEndByIndex.get(index);
		
		while(rowEnd-rowStart >= 0){
			testCase.deleteRow(rowStart);
			rowEnd --;
		}
		refreshModuleData();
		return testCase.saveFile();
			 
		
	}
	
	
	public void upModuleCase(int index) throws FileNotFoundException{
		if(index==0){
			Print.log("已经是第一行",3);
			return;
		}
		int rowStart = moduleStartByIndex.get(index) - 2;
		int rowEnd = moduleEndByIndex.get(index);
		int upRowStart = moduleStartByIndex.get(index-1) - 2;
		int upRowEnd = moduleEndByIndex.get(index-1);
		
		int diffFirstRow = rowStart - upRowStart;
		int diffEndRow = rowEnd - upRowEnd;
		int move = testCase.getSheetLastRowNumber() - rowStart;
		
		//数据移动到最后
		testCase.shiftRows(rowStart, rowEnd, move);

		//后一模块上移
		testCase.shiftRows(upRowStart, upRowEnd, diffEndRow);

		//之前的数据移动回来
		testCase.shiftRows(rowStart + move, rowEnd + move, -(move + diffFirstRow));
		
		testCase.saveFile();
		refreshModuleData();
		
	}

	public void createBook(String path) {
		testCase = new TestCaseExcel(path);
		
	}

	public void setSavePath(String path){
		((TestCaseExcel) testCase).saveAsFile(path);
	}
	
	
	public void save(){
		testCase.saveFile();
	}
	
	
}
