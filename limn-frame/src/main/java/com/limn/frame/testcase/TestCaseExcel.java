package com.limn.frame.testcase;


import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import com.limn.tool.regexp.RegExp;
import com.limn.tool.common.Print;
import com.limn.tool.external.ExcelEditor;


public class TestCaseExcel extends ExcelEditor implements TestCase {


	
	private HashMap <Integer,Integer> excelModuleStartIndex;
	
	private HashMap <Integer,Integer> excelModuleEndIndex;
	
	private HashMap <Integer,String> excelModuleName;
	
	private int excelSheetIndex;
	
	private int currentRow;
	
	
	public TestCaseExcel(String path) {
		super(path);
	}
	
	
	
	@Override
	public int getCurrentRow(){
		return currentRow;
	}
	
	
	@Override
	public void activateSheet(int sheetIndex) {
		excelSheetIndex = sheetIndex;
		excelSheet = excelBook.getSheetAt(excelSheetIndex);
		getExcelModule();
	}
	
	
	@Override
	public void setCurrentRow(int rowIndex){
		currentRow = rowIndex;
	}
	
	@Override
	public int getSheetLastRowNumber(){
		excelBook.setPrintArea(excelSheetIndex,"A4");
		return excelSheet.getLastRowNum();
	}
	
	public int getSheetSize(){
		return excelBook.getNumberOfSheets();
	}
	
	
	
	/**
	 * excelModuleIndex  对应用例开始的行
	 * excelModuleName	对应用例开始的行 - 2  才是模块名称 (-1是用例编号,相关用例 一栏)
	 */
	private void getExcelModule(){
		excelModuleStartIndex = new HashMap<Integer,Integer>();
		excelModuleEndIndex = new HashMap<Integer,Integer>();
		excelModuleName = new HashMap<Integer,String>();
		
		int index = 1;
		for (Row row:excelSheet){	
			setCurrentRow(row.getRowNum());
			if (getTestCaseNo() != null && getTestCaseNo().equals("用例编号")){
				excelModuleStartIndex.put(index, row.getRowNum());
				excelModuleName.put(index, getValue(excelSheetIndex,row.getRowNum() - 1,1));
				if(index > 1){
					excelModuleEndIndex.put(index-1, row.getRowNum() - 2);
				}
				index++;
			}
		}
		
		//最后一个模块.结束为文件的最后
		index--;
		if(index > 0){
			removeInvalidRow(excelModuleStartIndex.get(index));
			excelModuleEndIndex.put(index, excelSheet.getLastRowNum() + 1);
		}else{
			
		}
	}
	
	
	
	@Override
	public HashMap <Integer,String> getExcelModuleName(){
		return excelModuleName;
	}
	
	@Override
	public HashMap <Integer,Integer> getExcelModuleStartIndex(){
		return excelModuleStartIndex;
	}
	
	@Override
	public HashMap <Integer,Integer> getExcelModuleEndIndex(){
		return excelModuleEndIndex;
	}
	
	
	private String getCurrentCell(int columnIndex){
		return getValue(excelSheetIndex, currentRow, columnIndex);
	}
	
	
	
	@Override
	public Boolean isExecute(){
		String isExe = getValue(excelSheetIndex, currentRow, 0);
		return isExe==null?false:isExe.equals("1");
	}
	
	
	@Override
	public String getTestCaseNo(){
		return getCurrentCell(1);
	}
	
	@Override
	public String getRelatedNo(){
		return getCurrentCell(2);
	}

	@Override
	public String getTestStep() {
		
		return getCurrentCell(3);
	}
	
	@Override
	public String getExpected() {

		return getCurrentCell(4);
	}

	@Override
	public String getActual() {

		return getCurrentCell(5);
	}

	@Override
	public String getSQLResults() {

		return getCurrentCell(6);
	}
	
	@Override
	public String getResult() {

		return getCurrentCell(7);
	}
	
	
	@Override
	public void setExecuted(String value) {
		setCurrentCell(0,value);
	}


	@Override
	public void setTestCaseNo(String value) {
		setCurrentCell(1,value);	
	}


	@Override
	public void setTestRelatedNo(String value) {
		setCurrentCell(2,value);
	}

	@Override
	public void setTestStep(String value) {
		setCurrentCell(3,value);
	}
	
	@Override
	public void setResults(String value) {
		setCurrentCell(4,value);
	}
	

	@Override
	public void setAcutal(String value) {
		setCurrentCell(5,value);
	}

	@Override
	public void setSQLResults(String value) {
		setCurrentCell(6,value);	
	}
	
	@Override
	public void setResult(String value) {
		setCurrentCell(7,value);
	}
	
	@Override
	public void setResult(String value, String style) {
		setCurrentCell(7,value,style);
	}
	
	/**
	 * 数据录入
	 * @param index
	 * @param value
	 */
	private void setCurrentCell(int index,String value){
		
		setValue(excelSheetIndex, currentRow, index, new HSSFRichTextString(value));

		String[] rows = RegExp.splitWord(value, "\n");

		int lineCount = rows.length;
		if(excelSheet.getRow(currentRow).getHeightInPoints()<lineCount*13){
			excelSheet.getRow(currentRow).setHeightInPoints(lineCount*13);
		}
		
	}
	
	
	private void setCurrentCell(int index,String value, String style){
		
		setValue(excelSheetIndex, currentRow, index, new HSSFRichTextString(value));
		
//		//创建字体
//		CellStyle titleStyle = excelBook.createCellStyle();
//		HSSFFont font = excelBook.createFont();
//		if (!style.isEmpty()){
////			font.setColor(HSSFColor.BLUE.index);
//		}else if(index == 8){
//			if (style.equalsIgnoreCase("true")){
//				//设置链接的字体为蓝色
//				font.setColor(HSSFColor.BLUE.index);
//			}else if(style.equalsIgnoreCase("false")){
//				//设置链接的字体为红色
//				font.setColor(HSSFColor.RED.index);
//			}
//		}
//		titleStyle.setFont(font);
//		if(excelSheet.getRow(currentRow).getCell(index - 1)==null){
//			excelSheet.getRow(currentRow).createCell(index - 1);
//		}
//		excelSheet.getRow(currentRow).getCell(index - 1).setCellValue(value);
//		excelSheet.getRow(currentRow).getCell(index - 1).setCellStyle(titleStyle);
	}
	
	@Override
	public void insertRow(int rowNum){
		if(rowNum >= excelSheet.getLastRowNum()){
			insertLastRow();
		}else{
			excelSheet.shiftRows(rowNum + 1, excelSheet.getLastRowNum(),1);
		}
	}
	
	private void insertLastRow(){
		excelSheet.createRow((short)(excelSheet.getLastRowNum()+1)); 
	}
	
	
	@Override
	public void deleteRow(int rowNum){
		excelSheet.shiftRows(rowNum + 1,excelSheet.getLastRowNum(),-1);
	}

	
	public void setModuleName(int index, String moduleName){
		setCurrentRow(excelModuleStartIndex.get(index) - 2);
		setTestCaseNo(moduleName);
		getExcelModule();
	}
	
	public void saveAsFile(String path){
		saveAs(path);
	}
	
	@Override
	public void saveFile(){
		setStyle();
		save();
		
//		FileOutputStream out = null;
//
//		out = new FileOutputStream(excelFilePath);
//
//
//		try {
//			excelBook.write(out);
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		excelBook = null;
//		excelSheet = null;
//		init(excelFilePath,excelSheetIndex);
	}
	
	@Override
	public void refreshModule(){
		getExcelModule();
	}
	
	@Override
	public String getNextTestStep() {
		if(excelSheet.getRow(currentRow+1).getCell(4-1)==null){
			return "";
		}else{
			return excelSheet.getRow(currentRow+1).getCell(4-1).toString();			
		}
	}
	
	public int getExcelSheetIndex(){
		return excelSheetIndex;
	}
	
	
	private HSSFCellStyle setContentBorder() {
		HSSFCellStyle style = excelBook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN); // 下边框
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);// 左边框
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);// 上边框
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);// 右边框
		style.setWrapText(true);
		return style;
	}
	
	/**
	 * 样式
	 */
	private void setStyle(){
		//模块标题
		CellStyle titleStyle = excelBook.createCellStyle();
		//列表标题(用例编号,相关用例等)
		CellStyle listStyle =  setContentBorder();
		//内容
		CellStyle contentStyle = setContentBorder();
		
		HSSFFont font2 = excelBook.createFont();
		font2.setFontName("楷体");
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		font2.setFontHeightInPoints((short) 16);
		
		HSSFFont font1 = excelBook.createFont();
		font1.setFontName("楷体");
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
		font1.setFontHeightInPoints((short) 12);
		

		titleStyle.setFont(font2);
		listStyle.setFont(font1);
		//设置背景颜色
		listStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
		listStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		for(int index:excelModuleStartIndex.keySet()){
			HSSFRow row = null;
			row = excelSheet.getRow(excelModuleStartIndex.get(index)-2);
			if(row!=null){
				row.createCell(1);
				row.getCell(1).setCellStyle(titleStyle);
			}
			
			row = excelSheet.getRow(excelModuleStartIndex.get(index)-1);
			
			setCellStyle(row, listStyle);
			
			for(int i = excelModuleStartIndex.get(index);i<=excelModuleEndIndex.get(index);i++){
				
				row = excelSheet.getRow(i);
				if(row==null){
					row = excelSheet.createRow(i);
					row.createCell(0);
					row.createCell(1);
					row.createCell(2);
					row.createCell(3);
					row.createCell(4);
					row.createCell(5);
					row.createCell(6);
					row.createCell(7);
				}else{
					setCellStyle(row, contentStyle);
				}
				row.setHeightInPoints(13);
				for(int cellIndex = 0;cellIndex<=7;cellIndex++){
					HSSFCell cell = row.getCell(cellIndex);
					int lineCount = 1;
					if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
						String value = cell.getStringCellValue();
						String[] rows = RegExp.splitWord(value, "\n");
						lineCount = rows.length;
					}
					
					if(row.getHeightInPoints()<lineCount*13){
						row.setHeightInPoints(lineCount*13);
					}
					
				}
			}
		}
	
		excelSheet.autoSizeColumn((short)0);
		excelSheet.autoSizeColumn((short)1);
		excelSheet.autoSizeColumn((short)2);
		excelSheet.autoSizeColumn((short)3);
		excelSheet.autoSizeColumn((short)4);
		excelSheet.autoSizeColumn((short)5);
		excelSheet.autoSizeColumn((short)6);
		excelSheet.autoSizeColumn((short)7);
	}
	
	
	private void setCellStyle(HSSFRow row, CellStyle contentStyle){
		if(row.getCell(0)==null){
			row.createCell(0);
		}
		if(row.getCell(1)==null){
			row.createCell(1);
		}
		if(row.getCell(2)==null){
			row.createCell(2);
		}
		if(row.getCell(3)==null){
			row.createCell(3);
		}
		if(row.getCell(4)==null){
			row.createCell(4);
		}
		if(row.getCell(5)==null){
			row.createCell(5);
		}
		if(row.getCell(6)==null){
			row.createCell(6);
		}
		if(row.getCell(7)==null){
			row.createCell(7);
		}
		row.getCell(0).setCellStyle(contentStyle);
		row.getCell(1).setCellStyle(contentStyle);
		row.getCell(2).setCellStyle(contentStyle);
		row.getCell(3).setCellStyle(contentStyle);
		row.getCell(4).setCellStyle(contentStyle);
		row.getCell(5).setCellStyle(contentStyle);
		row.getCell(6).setCellStyle(contentStyle);
		row.getCell(7).setCellStyle(contentStyle);
		
	}
	
	
	private void removeInvalidRow(int index){
		CellReference cellReference = new CellReference("A"+index);
		boolean flag = false;
		for (int i = cellReference.getRow(); i < excelSheet.getLastRowNum();) {
			Row r = excelSheet.getRow(i);
			if (r == null) {
				// 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
				deleteRow(i);
				continue;
			}
			flag = false;
			for (Cell c : r) {
				if (c.getCellType() != Cell.CELL_TYPE_BLANK) {
					flag = true;
					break;
				}
			}
			if (flag) {
				i++;
				continue;
			} else {// 如果是空白行（即可能没有数据，但是有一定格式）
				if (i == excelSheet.getLastRowNum()){
					
				}else{
					// 如果还没到最后一行，则数据往上移一行
					deleteRow(i);
				}
			}
		}
	}
	
	public void shiftRows(int start,int end,int move){
		excelSheet.shiftRows(start, end, move);
	}

//	public void createBook(){
//		excelBook = new HSSFWorkbook();
//		excelSheet = excelBook.createSheet();
//		excelBook.createSheet();
//	}


	@Override
	public HashMap<String, String> getTestCaseRelateNoByNo() {
		HashMap<String, String> relate = new HashMap<String, String>();
		int sheetCount = getSheetSize();
		int currentSheetIndex = getExcelSheetIndex();
		for(int i=0;i<sheetCount;i++){
			activateSheet(i);
			setCurrentRow(0);
			if(getCurrentCell(1).toString().equals("1.0") || getCurrentCell(1).toString().equals("1")){
				getExcelModule();
				for(int index:excelModuleStartIndex.keySet()){
					int row = excelModuleStartIndex.get(index);
					int rowEnd = excelModuleEndIndex.get(index);
					for(;row<=rowEnd;row++){
						setCurrentRow(row);
						if(!relate.containsKey(getTestCaseNo()) && !getTestCaseNo().isEmpty()){
							relate.put(getTestCaseNo(), getRelatedNo());
							relate.put(getTestCaseNo() + "_Location", excelSheetIndex + "_" + row);
						}else if(!getTestCaseNo().isEmpty()){
							Print.log("用例编号存在重复:" + getTestCaseNo() + " " + excelSheetIndex
									+ "_" + row , 2);
						}
					}
				}
			}
		}
		activateSheet(currentSheetIndex);
		setCurrentRow(0);
		return relate;
	}

	private void setExcelHyperLinks(int index, String path){
		HSSFHyperlink link = null;
		//创建带URL链接的单元格
		link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
		//设置单元格链接的地址
		link.setAddress(path);
		//在当前的单元格上生效
		excelSheet.getRow(currentRow).getCell(index - 1).setHyperlink(link);
	}
	
	@Override
	public void setHyperLinks(int index, String path) {
		setExcelHyperLinks(index,path);
	}
	
}
