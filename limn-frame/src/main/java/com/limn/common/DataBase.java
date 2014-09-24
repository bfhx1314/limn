package com.limn.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.limn.log.RunLog;
import com.limn.parameter.Parameter;


public class DataBase {
	
	private static Connection conn = null;
	
	/**
	 * 建立数据库连接
	 */
	private static void DBInit(){

		
		
		try {
			Class.forName(Parameter.DBDRIVER);
			conn = DriverManager.getConnection(Parameter.DBURL, Parameter.DBUSER, Parameter.DBPASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			RunLog.printLog(e.getErrorCode()+":"+e.getMessage(), 2);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 执行:sql 用于初始化数据库
	 * @param sql sql内容
	 * @throws ParameterException 
	 */
	public static void executeMultilineSQL(String sql){
		DBInit();
		// 判断数据库为sqlserver
		if (Parameter.DBTYPE == 2){
			sql = sql.replaceAll(";\nGO", "\nGO");
			sql = sql.replaceAll("\nGO", "\n;");
			sql = sql.replaceAll("\n;", ";\n");
		}
		String[] arr = sql.split(";\n"); 
		try {
			Statement sta = conn.createStatement();
			for(int i=0;i<arr.length;i++){
				sta.executeUpdate(arr[i]);
			}
			sta.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
//			throw new ParameterException(ParameterException.SQL_Exception, e.getErrorCode()+":"+e.getMessage());
		}
	}
	
	
	public static int executeSingleSQL(String sql){
		DBInit();
		try {
			Statement sta = conn.createStatement();
			sta.executeUpdate(sql);
			RunLog.printLog(sql, 0);
			sta.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			RunLog.printLog(sql + " 未执行成功:" + e.getMessage(), 2);
			return -1;
		}
		return 0;
	}
	
	
	public static String[][] executeSQL(String sql){
		DBInit();
		Print.debugLog(sql, 0);
		Statement sta = null;
		String[][] res = null;
		try {
			sta = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			ResultSet result = sta.executeQuery(sql);
			int columnCount = result.getMetaData().getColumnCount();
			result.last();
			int rowCount = result.getRow();
			result.first();
			if(rowCount==0 || columnCount==0){
				return new String[0][0];
			}
			res = new String[rowCount][columnCount];
			int row = 0;
			do{ 
				for(int i=0;i<columnCount;i++){
					res[row][i] = result.getString(i+1);
				}
				row ++;
			} while(result.next());

			result.close();
			sta.close();
			conn.close();

		} catch (SQLException e) {
			RunLog.printLog(e.getMessage(), 0);
		}
		return res;
		
	}
	
	/**
	 * 获取数据库信息
	 * @param str 执行的sql
	 * @return 数组，初始length为0。 
	 */
	public static String[] getConnSqlData(String str){
		DBInit();
		String[] arrData = {};
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = stmt.executeQuery(str); 
			// 创建查询声明
//			PreparedStatement preparedStatement = conn
//				    .prepareStatement(str);
			// 获取结果
//			ResultSet resultSet = preparedStatement.executeQuery();
			// 获取各个列的信息
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			arrData = new String[columnCount];
			resultSet.first();
				do{
					for(int i=0;i<columnCount;i++){
						if (arrData[i]==null){
							arrData[i] = resultSet.getString(i+1);
						}else{
							arrData[i] = arrData[i] + ";" +resultSet.getString(i+1);
						}
//						arrData[i] = resultSet.getString(i+1);
					}
				}while(resultSet.next());


		} catch (SQLException e) {
			RunLog.printLog(str, 0);
			RunLog.printLog(e.getMessage(), 2);
//			e.printStackTrace();
		} 
		return arrData;
	}
	/**
	 * 验证表是否存在，在的话删除
	 * @param str
	 * @return
	 */
	public static boolean existTable(String str){
		String[][] arr = new String[1][1];
		switch(Parameter.DBTYPE){
		case 0:
			arr = executeSQL("Select count(*) from syscat.tables where tabname='"+str.toUpperCase()+"'");
			if (!arr[0][0].equals("0")){
				executeSingleSQL("drop table "+str);
			}
			break;
		case 1:
			arr = executeSQL("select count(*) from user_tables where TABLE_NAME='"+str.toUpperCase()+"'");
			if (!arr[0][0].equals("0")){
				executeSingleSQL("drop table "+str);
			}
			break;
		case 2:
			arr = executeSQL("Select count(*) from sysobjects where name='"+str+"'");
			if (!arr[0][0].equals("0")){
				executeSingleSQL("drop table "+str);
			}
			break;
		case 3:
//			arr = executeSQL("select table_name from information_schema.tableswheretable_schema='"&&"' where table_name='"+str+"'")
			if (!arr[0][0].equals("0")){
				executeSingleSQL("drop table "+str);
			}
			break;
		}
		return false;
	}

	/**
	 * 复制表
	 * @param expectTable
	 * @param string
	 * @param b
	 */
	public static void copyDataTable(String newTable, String oldTable,
			boolean copyBoolean) {
		String copybool = "";
		if (copyBoolean){
			copybool = "1=1";
		}else{
			copybool = "1=2";
		}
		switch(Parameter.DBTYPE){
		case 0:
			if (RegExp.findCharacters(oldTable, "\\s")){
				executeSingleSQL("create table "+newTable+" as ("+oldTable+")  definition only ");
			}else{
				executeSingleSQL("create table "+newTable+" as (select * from "+oldTable+")  definition only ");
			}
			if (copyBoolean){
				executeSingleSQL("insert into "+newTable+" "+oldTable+" ");
			}
			break;
		case 1:
			oldTable = toDate(oldTable);
			if (RegExp.findCharacters(oldTable, "\\s")){
				executeSingleSQL("create table "+newTable+" as select * from ("+ oldTable + ") a where " + copybool);
			}else{
				executeSingleSQL("create table "+newTable+" as select * from "+ oldTable + " where " + copybool);
			}
			break;
		case 2:
			if (!RegExp.findCharacters(oldTable, "^select top.*")){
				oldTable = "Select top 100 percent" + RegExp.matcherCharacters(oldTable, " .*").get(0);
			}
			if (RegExp.findCharacters(oldTable, "\\s")){
				executeSingleSQL("select * into " + newTable + " from (" + oldTable + ")  a where " + copybool);
			}else{
				executeSingleSQL("select * into " + newTable + " from " + oldTable + " where " + copybool);
			}
			break;
		case 3:
			break;
		}
	}

	/**
	 * 转换日期
	 * @param str
	 * @return
	 */
	private static String toDate(String str) {
		String patrn = "'\\d{4}-\\d{1,2}-\\d{1,2}.*?'";
		String patrnEx = "to_date.?\\(.?'\\d{4}-\\d{1,2}-\\d{1,2}.*?'";
		ArrayList<String> arrList = RegExp.matcherCharacters(str, patrnEx);
		for(int i=0;i<arrList.size();i++){
			str = str.replace(arrList.get(i), "[xxxlimnxxx]"+i);
		}
		
		ArrayList<String> arrListEx = RegExp.matcherCharacters(str, patrn);
		Map<String, String> map = new HashMap<String,String>();
		for(int j=0;j<arrListEx.size()-1;j++){
			String key = arrListEx.get(j);
			if (!map.containsKey(key)){
				map.put(key, "");
				if (key.length() < 14){
					str = str.replace(key, "to_date(" + key + ",'yyyy-mm-dd')");
				}else{
					str = str.replace(key, "to_date(" + key + ",'yyyy-mm-dd hh24:mi:ss')");
				}
			}
		}
		// '还原特殊字符
		for(int k=0;k<arrList.size();k++){
			str = str.replace("[xxxlimnxxx]"+k, arrList.get(k));
		}
		return null;
	}
	
	/**
	 * 重写sql
	 * @param str
	 * @return
	 */
	public static String formatSql(String str){
		switch(Parameter.DBTYPE){
		case 1:
			str = toDate(str);
			break;
		}
		return str;
	}

	/**
	 * 获取列名
	 * @param expectTable
	 */
	public static String[][] getDataColumnName(String expectTable) {
		String[][] columnName = new String[1][1];
		switch(Parameter.DBTYPE){
		case 0:
			columnName = executeSQL("select COLNAME from syscat.columns where TABSCHEMA= '"+Parameter.DBUSER.toUpperCase()+"' and TABNAME='"+expectTable.toUpperCase()+"'");
			break;
		case 1:
			columnName = executeSQL("select CNAME from col where tname='"+expectTable.toUpperCase()+"'");
			break;
		case 2:
			columnName = executeSQL("Select Name  FROM SysColumns Where id=Object_Id('"+ expectTable +"') ");
			break;
		case 3:
			break;
		}
		return columnName;
	}

	/**
	 * 对比2个表
	 * @param expectTable
	 * @param acualTable
	 * @return
	 */
	public static boolean contrastTable(String expectTable, String actualTable) {
		boolean resultsBoolean = false;
		String EDCount = executeSQL("Select Count(*) From "+expectTable)[0][0];
		String ECount = executeSQL(" Select Count(*) From (Select * From "+expectTable+" union Select * From "+expectTable+") a")[0][0];
		String ADCount = executeSQL("Select Count(*) From "+actualTable)[0][0];
		String ACount = executeSQL(" Select Count(*) From (Select * From "+actualTable+"  union Select * From "+actualTable+") a")[0][0];
		String SCount = executeSQL(" Select Count(*) From (Select * From "+actualTable+"  union Select * From "+expectTable+") a")[0][0];
		String[] Result = contrastTableDifference("Select * From "+expectTable,"Select * From "+actualTable);
		String title = "[Comparison results]";
		String minus = "[Missing line record]" + "\r\n" + Result[0];
		String plus = "[Over line record]" + "\r\n" + Result[1];
		String except="[Excepted result]" + "\r\n" + Result[2];
		String actual="[actual result]" + "\r\n" + Result[3];
		String strng = title + "\r\n" + minus + "\r\n" + plus + "\r\n" + except + "\r\n" + actual;
//		String path = Test.runTimeSheetNum+"-"+Test.runTimeRowNum+"-"+Test.runTimeStepNum+".txt";
//		String errorPath = Parameter.ERRORFILE+"\\"+path;
//		FileUtil.writeFile(errorPath,strng); // 把数据库结果写入文件
//		Test.setHyperLink(7, "ResultTxt\\"+ path);
		RunLog.printLog("预期结果：\r\n"+Result[2], 0);
		RunLog.printLog("实际结果：\r\n"+Result[3], 0);
		try{
			if (Integer.parseInt(EDCount) == Integer.parseInt(ECount)
					&& Integer.parseInt(ADCount) == Integer.parseInt(ACount)){
				if (Integer.parseInt(SCount) == Integer.parseInt(ACount)){
					resultsBoolean = true;
				}
			}else if(Integer.parseInt(EDCount) == Integer.parseInt(ADCount)
					&& Integer.parseInt(ECount) == Integer.parseInt(ACount)){
				String[][] expectedArray = DataBase.executeSQL("Select * From "+expectTable);
				String[][] actualArray = DataBase.executeSQL("Select * From "+actualTable);
				resultsBoolean = true;
				for(int i=0;i<expectedArray.length;i++){
					for(int j=0;j<expectedArray[i].length;j++){
						if (!(RegExp.findCharacters(expectedArray[i][j], actualArray[i][j])
								&& RegExp.findCharacters(actualArray[i][j], expectedArray[i][j]))){
							resultsBoolean = false;
						}
					}
				}
			}
		}catch(Exception e){}
		return resultsBoolean;
	}
	
	/**
	 * 数据库表对比
	 * @return
	 */
	public static boolean sqlContrast(String expectTable, String actualTable
			,String whereTable,String fileds, String DBFiles){
		boolean resultsBoolean = false;
		String EDCount = executeSQL("Select Count(*) From "+expectTable)[0][0];
		String ECount = executeSQL(" Select Count(*) From (Select * From "+expectTable+" union Select * From "+expectTable+") a")[0][0];
		String ADCount = executeSQL("Select Count(*) From "+actualTable+whereTable)[0][0];
		String ACount = executeSQL(" Select Count(*) From (Select "+fileds+" From "+actualTable+whereTable+" union Select "+fileds+" From "+actualTable+whereTable+") a")[0][0];
		String SCount = executeSQL(" Select Count(*) From (Select "+fileds+" From "+actualTable+whereTable+"  union Select * From "+expectTable+") a")[0][0];
		try{
			if (Integer.parseInt(EDCount) == Integer.parseInt(ECount)
					&& Integer.parseInt(ADCount) == Integer.parseInt(ACount)){
				if (Integer.parseInt(SCount) == Integer.parseInt(ACount)){
					resultsBoolean = true;
				}
			}else if(Integer.parseInt(EDCount) == Integer.parseInt(ADCount)
					&& Integer.parseInt(ECount) == Integer.parseInt(ACount)){
				String[][] expectedArray = DataBase.executeSQL("Select "+DBFiles+" From "+expectTable+" Order By "+DBFiles);
				//把两张表的顺序排列一致
				String[][] actualArray = DataBase.executeSQL("Select "+fileds+" From "+actualTable+" "+whereTable+" Order By "+DBFiles);
				resultsBoolean = true;
				for(int i=0;i<actualArray.length;i++){
					for(int j=0;j<actualArray[i].length;j++){
						if (!(RegExp.findCharacters(expectedArray[i][j], actualArray[i][j])
								&& RegExp.findCharacters(actualArray[i][j], expectedArray[i][j]))){
							resultsBoolean = false;
						}
					}
				}
			}
		}catch(Exception e){}
		return resultsBoolean;
	}
	/**
	 * 对比两张表
	 * 跟预期结果中 多出来的记录和缺少的记录，0 是缺少，1是多出
	 * @param expectTable
	 * @param acualTable
	 * @return
	 */
	private static String[] contrastTableDifference(String expect, String actual) {
		String[] result = new String[4];
		String expectedMinus = "";
		String expectedPlus = "";
		switch(Parameter.DBTYPE){
		case 0:
			expectedMinus = expect+" except "+actual;
			expectedPlus = actual+" except "+expect;
			break;
		case 1:
			expectedMinus = expect+" minus "+actual;
			expectedPlus = actual+" minus "+expect;
			break;
		case 2:
			expectedMinus = expect+" except "+actual;
			expectedPlus = actual+" except "+expect;
			break;
		case 3:
			break;
		}
		result[0] = formatData(expectedMinus); // 存 预期结果比实际结果多的数据
		result[1] = formatData(expectedPlus); // '存 实际结果比预期结果多的数据
		result[2] = formatData(expect); // 存预期结果
		result[3] = formatData(actual); // 存实际结果
		return result;
	}
	
	/**
	 * 整理数据库结果
	 * @param expectedMinus
	 * @return
	 */
	private static String formatData(String sql) {
		DBInit();
		Statement sta = null;
		String res = null;
		try {
			sta = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			
			ResultSet result = sta.executeQuery(sql);
			// 获取各个列的信息
			ResultSetMetaData metaData = result.getMetaData();
			int columnCount = metaData.getColumnCount();
			result.last();
			int rowCount = result.getRow();
			result.first();
			if(rowCount==0 || columnCount==0){
				return "";
			}
			for(int i=1;i<columnCount;i++){
				res = res + String.valueOf((char)9) + metaData.getColumnName(i);
			}
			res = res + "\r\n" + "---------------------------------------------------------------------------------------------------------"+"\r\n";
			do{ 
				for(int i=0;i<columnCount;i++){
					res = res + String.valueOf((char)9) + result.getString(i+1);
				}
				res = res +"\r\n"; 
			} while(result.next());
			result.close();
			sta.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

//	public static void main(String[] args){
	//	String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	//	String url="jdbc:sqlserver://localhost:1433;databaseName=test831a"; 
	//	String user="sa"; 
	//	String password="blackman";
//		Parameter.DBTYPE = 2;
//		String sqlText = FileUtil.readFile("D:\\BOKEsvn\\Automation\\DataBak\\sqlserverAll.sql");
//		dbDataInit(sqlText);
//	}
	
}
