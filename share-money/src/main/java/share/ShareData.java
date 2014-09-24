package share;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import common.MyDate;

public class ShareData {

	
	
	private HSSFWorkbook excelBook = null;
	
	private HSSFSheet excelSheet = null;
	
	private String filePath = null;
	
	private int shareMoneyCol = 5;
	public ShareData(String path){
		this.filePath = path;
		File excelFile = new File(filePath);
		InputStream fileIS =null;
		ByteArrayInputStream byteArrayInputStream = null;
		try {
			fileIS = new FileInputStream(excelFile);
			byte buf[] = org.apache.commons.io.IOUtils.toByteArray(fileIS);
			byteArrayInputStream = new ByteArrayInputStream(buf);
			excelBook = new HSSFWorkbook(byteArrayInputStream);
			fileIS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		excelSheet = excelBook.getSheetAt(0);
	}
	
	public String[][] getData(){
		String[][] data = new String[excelSheet.getLastRowNum()+1][4];
		int rowNumber = 0;
		for (Row row:excelSheet){
			if(row.getCell(1)!=null){
				row.getCell(1).setCellType(1);
				row.getCell(2).setCellType(1);
				row.getCell(3).setCellType(1);
				
				if(row.getCell(4)==null){
					row.createCell(4);
				}
				
				row.getCell(4).setCellType(1);

				data[rowNumber][0] = row.getCell(1).getStringCellValue();
				data[rowNumber][1] = row.getCell(2).getStringCellValue();
				data[rowNumber][2] = row.getCell(3).getStringCellValue();
				
				if(row.getCell(4)==null){
					row.createCell(4);
				}
				
				data[rowNumber][3] = row.getCell(4).getStringCellValue();

			}
			rowNumber ++;
		}

		return data;
	}
	
	public String[] getShareData(){
		String[] data = new String[excelSheet.getLastRowNum()];
		int rowNumber = 0;
		for (Row row:excelSheet){	
			data[rowNumber] = row.getCell(4).getStringCellValue();
			rowNumber ++;
		}

		return data;
	}
	
	
	public void setShareMoney(int row, String value){
		
		if(excelSheet.getRow(row).getCell(shareMoneyCol)==null){
			excelSheet.getRow(row).createCell(shareMoneyCol);
		}
		excelSheet.getRow(row).getCell(shareMoneyCol).setCellValue(value);
		save();
		shareMoneyCol ++;
	}
	
	public void setShareTime(int row,Date date){
		if (excelSheet.getRow(row).getCell(4)==null){
			excelSheet.getRow(row).createCell(4);
		}
		excelSheet.getRow(row).getCell(4).setCellValue(MyDate.getData("yyyy-MM-dd HH:mm:ss", date));
		save();
	}
	
	
	public void setShareStatus(int row,Date date){
		if (excelSheet.getRow(row).getCell(1)==null){
			excelSheet.getRow(row).createCell(1);
		}
		excelSheet.getRow(row).getCell(1).setCellValue(date);
		save();
	}
	
	private void save(){
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			excelBook.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	
}
