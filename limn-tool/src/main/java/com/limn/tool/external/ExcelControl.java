package com.limn.tool.external;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.util.IOUtils;

/**
 * Excel的读写方法
 * @author limn
 *
 */
public class ExcelControl {
	
	private String filePath = null;
	
	private HSSFSheet excelSheet = null;

	private HSSFWorkbook excelBook = null;
	
	private int sheetCount = 0;
	
	private boolean readOnly = true;
	
	/**
	 * 操作Excel
	 * @param path 文件路径
	 */
	public ExcelControl(String path){
		this.filePath = path;
		File excelFile = new File(filePath);
		InputStream fileIS = null;
		ByteArrayInputStream byteArrayInputStream = null;
		try {
			fileIS = new FileInputStream(excelFile);
			byte buf[] = IOUtils.toByteArray(fileIS);
			byteArrayInputStream = new ByteArrayInputStream(buf);
			excelBook = new HSSFWorkbook(byteArrayInputStream);
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
        }
		sheetCount = excelBook.getNumberOfSheets();
	}
	
	/**
	 * 读取Excel的数据
	 * @param is
	 */
	public ExcelControl(InputStream is){
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
	public String getValue(int sheetIndex, int col, int row){
		return setSheetByIndex(sheetIndex) ? getValue(col, row) : null;
	}
	
	/**
	 * 获取执行sheet页的单元格的值
	 * @param sheetName sheet页的名称
	 * @param col 列号
	 * @param row 行号
	 * @return 全部返回成String类型
	 */
	public String getValue(String sheetName, int col,int row){
		return setSheetByName(sheetName) ? getValue(col, row) : null;
	}
	
	
	/**
	 * 获取Excel单元格上的数据
	 * @param col 列号
	 * @param row 行号
	 * @return 全部返回成String类型
	 */
	private String getValue(int col, int row){
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
	
	private boolean setSheetByIndex(int sheetIndex){
		if(sheetIndex >= sheetCount){
			System.out.println("SheetIndex越界:" + sheetIndex + "，总计：" + sheetCount);
			return false;
		}else{
			excelSheet = excelBook.getSheetAt(sheetIndex);
			return true;
		}
	}
	
	private boolean setSheetByName(String sheetName){
		HSSFSheet excelSheetTmp = excelBook.getSheet(sheetName);
		if(excelSheetTmp == null){
			System.out.println("不存在的Sheet页名称：" + sheetName);
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
		}else if (excelSheet.getRow(row) == null) {
			excelSheet.createRow(row);
		}else if (excelSheet.getRow(row).getCell(col) == null) {
			excelSheet.getRow(row).createCell(col);
		}
		try{
			Cell cell = excelSheet.getRow(row).getCell(col);
			if(value instanceof Double){
				cell.setCellType(Cell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Double) value);
			}else{
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue((String) value);
			}
			return true;
		}catch(Exception e){
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
		}
	}

}
