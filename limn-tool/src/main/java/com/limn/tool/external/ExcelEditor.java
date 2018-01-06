package com.limn.tool.external;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import com.limn.tool.common.BaseToolParameter;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.IOUtils;

import com.limn.tool.common.Print;

/**
 * Excel的读写方法
 * @author limn
 *
 */
public class ExcelEditor {
	
	private String filePath = null;
	
	public HSSFSheet excelSheet = null;

	public HSSFWorkbook excelBook = null;
	
	private int sheetCount = 0;
	
	private boolean readOnly = true;
	
	/**
	 * 操作Excel
	 * @param path 文件路径
	 */
	public ExcelEditor(String path){
		this.filePath = path;
		openExcel();
		sheetCount = excelBook.getNumberOfSheets();
	}
	
//	public static void main(String[] args){
//		ExcelEditor ee = new ExcelEditor("F:\\test.xls");
//	}
	
	/**
	 * 
	 */
	private void openExcel(){
		File excelFile = new File(filePath);
		if (excelFile.exists()) {
			InputStream fileIS = null;
			ByteArrayInputStream byteArrayInputStream = null;
			try {
				fileIS = new FileInputStream(excelFile);
				byte buf[] = IOUtils.toByteArray(fileIS);
				byteArrayInputStream = new ByteArrayInputStream(buf);
				excelBook = new HSSFWorkbook(byteArrayInputStream);
				setSheetByIndex(0);
				readOnly = false;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fileIS != null) {
					try {
						fileIS.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					byteArrayInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else{
			readOnly = false;
			createBook(filePath);
		}
	}
	
	
	/**
	 * 创建文件
	 * @param path
	 */
	private void createBook(String path){
		excelBook = new HSSFWorkbook();
		excelSheet = excelBook.createSheet();
		excelBook.createSheet();
	}
	
	
	/**
	 * 读取Excel的数据
	 * @param is
	 */
	public ExcelEditor(InputStream is){
		byte buf[];
		try {
			buf = IOUtils.toByteArray(is);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
			excelBook = new HSSFWorkbook(byteArrayInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {  
            if (is != null) {  
                try {  
                    is.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }
		sheetCount = excelBook.getNumberOfSheets();

	}
	
	/**
	 * 获取执行sheet页的单元格的值
	 * @param sheetIndex sheet页的Index
	 * @param col 列号
	 * @param row 行号
	 * @return 全部返回成String类型
	 */
	public String getValue(int sheetIndex, int row, int col){
		return setSheetByIndex(sheetIndex) ? getValue(row, col) : null;
	}
	
	/**
	 * 获取执行sheet页的单元格的值
	 * @param sheetName sheet页的名称
	 * @param col 列号
	 * @param row 行号
	 * @return 全部返回成String类型
	 */
	public String getValue(String sheetName, int row, int col){
		return setSheetByName(sheetName) ? getValue(row, col) : null;
	}
	
	
	/**
	 * 获取Excel单元格上的数据
	 * @param col 列号
	 * @param row 行号
	 * @return 全部返回成String类型
	 */
	private String getValue(int row, int col){
		if(excelSheet.getRow(row)==null){
			return null;
		}else if(excelSheet.getRow(row).getCell(col)==null){
			return null;
		}
		
		int cellType = excelSheet.getRow(row).getCell(col).getCellType();
		
		switch(cellType){
		
		case Cell.CELL_TYPE_STRING:
			return excelSheet.getRow(row).getCell(col).getStringCellValue();
			
		case Cell.CELL_TYPE_NUMERIC:
			
			return new DecimalFormat("0").format(excelSheet.getRow(row).getCell(col).getNumericCellValue());
			
		case Cell.CELL_TYPE_BOOLEAN:
			
			return String.valueOf(excelSheet.getRow(row).getCell(col).getBooleanCellValue());
			
		case Cell.CELL_TYPE_FORMULA:
			return null;
		case Cell.CELL_TYPE_BLANK:
			return null;
		case Cell.CELL_TYPE_ERROR:
			return null;
		default :
			return null;
		}

	}
	
	/**
	 * 根据sheetname返回 index
	 * @param sheetName
	 * @return
	 */
	public int getSheetIndexBySheetName(String sheetName){
		HSSFSheet excelSheetTmp = excelBook.getSheet(sheetName);
		if(excelSheetTmp == null){
			BaseToolParameter.getPrintThreadLocal().log("不存在的Sheet页名称：" + sheetName, 2);
			return -1;
		}else{
			return excelBook.getSheetIndex(excelSheetTmp);
		}
	}
	
	private boolean setSheetByIndex(int sheetIndex){
		if(sheetIndex >= sheetCount){
//			System.out.println("SheetIndex越界:" + sheetIndex + "，总计：" + sheetCount);
			return false;
		}else{
			excelSheet = excelBook.getSheetAt(sheetIndex);
			return true;
		}
	}
	
	private boolean setSheetByName(String sheetName){
		HSSFSheet excelSheetTmp = excelBook.getSheet(sheetName);
		if(excelSheetTmp == null){
			BaseToolParameter.getPrintThreadLocal().log("不存在的Sheet页名称：" + sheetName, 2);
			return false;
		}else{
			excelSheet = excelSheetTmp;
			return true;
		}
		
	}
	
	/**
	 * sheet页设置单元格值
	 * @param sheetName sheet页名称
	 * @param row 行
	 * @param col 列
	 * @param value 值
	 * @return 执行是否成功
	 */
	public boolean setValue(String sheetName,int row, int col, Object value) {
		return setSheetByName(sheetName) ? setValue(row, col, value) : false;
	}
	
	/**
	 * sheet页设置单元格值
	 * @param sheetIndex sheet页索引
	 * @param row 行
	 * @param col 列
	 * @param value 值
	 * @return 执行是否成功
	 */
	public boolean setValue(int sheetIndex,int row, int col, Object value) {
		return setSheetByIndex(sheetIndex) ? setValue(row, col, value) : false;
	}
	
	
	
	private boolean setValue(int row, int col, Object value){
		if(readOnly){
			return false;
		}
		if (excelSheet.getRow(row) == null) {
			excelSheet.createRow(row);
		}
		HSSFRow cRow = excelSheet.getRow(row);
		if (cRow.getCell(col) == null) {
			cRow.createCell(col);
		}
		try{
			Cell cell = excelSheet.getRow(row).getCell(col);
			if(value instanceof Double || value instanceof Integer){
				//如果单元格原来是String类型 无法转化 必须删除
				cRow.removeCell(cell);
				cell = cRow.createCell(col);
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				if(value instanceof Integer){
					value = ((Integer) value).doubleValue();
				}
				cell.setCellValue((Double) value);
			}else if(value instanceof HSSFRichTextString){
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue((HSSFRichTextString)value);
			}else{
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue((String) value);
			}
			return true;
		}catch(IllegalStateException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 保存
	 * @return
	 */
	public boolean save(){
		return save(filePath);
	}
	
	/**
	 * 另存为
	 * @return
	 */
	public boolean saveAs(String path){
		return save(path);
	}
	
	
	
	private boolean save(String path){
		if(readOnly){
			return false;
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(path);
			excelBook.write(out);
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally{
			excelBook = null;
			excelSheet = null;
			openExcel();
		}
	}

}
