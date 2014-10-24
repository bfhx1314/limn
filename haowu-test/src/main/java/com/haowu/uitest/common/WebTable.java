package com.haowu.uitest.common;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;




import com.limn.driver.Driver;
import com.limn.driver.exception.SeleniumFindException;
import com.limn.tool.regexp.RegExp;


public class WebTable {
	
	private WebElement webTable = null;
	private List<WebElement> rows = null;
	private WebElement tBody = null;
	
	public WebTable(WebElement table){
		webTable = table;
		webTable.findElement(By.tagName("thead"));
		tBody = webTable.findElement(By.tagName("tbody"));
		rows = tBody.findElements(By.tagName("tr"));
	}
	
	public void insertValue(int col , int row , String value){
		List<WebElement> cells = rows.get(row).findElements(By.tagName("td"));
		String ID = cells.get(col).getAttribute("id");
//		Driver.setText("id=" + ID, value);
		try {
			Driver.setText(By.id(ID), value);
		} catch (SeleniumFindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public WebElement getWebElment(int col , int row){
		
		List<WebElement> cells = rows.get(row).findElements(By.xpath("//td"));
		return cells.get(col);
	}
	
	public List<WebElement> getRows(){
		return rows;	
	}
	
	
	/**
	 * 获取某列的所有值
	 * @param col index
	 * @return
	 */
	public List<String> getColumnValue(int col){
		List<String> value = new ArrayList<String>();
		for(WebElement tr : rows){
			value.add(tr.findElement(By.xpath("\\td[0]")).getText());
		}
		return value;
	}
	
	/**
	 * 获取某个值所在的行号
	 * @param col 列号
	 * @param findName 所包含的值
	 * @return
	 */
	public int getRowIndexByValue(int col, String findName){
		int index = 0;
		col = col + 1;
		for(WebElement tr : rows){
//		Driver.selectElementByXPath("//tbody/tr[1]").findElements(By.xpath(".//td"))
			if (RegExp.findCharacters(tr.findElement(By.xpath(".//td[" + col + "]")).getText(),findName)) {
				return index;
			}
			index ++;
		}
		return -1;
	}
	

}
