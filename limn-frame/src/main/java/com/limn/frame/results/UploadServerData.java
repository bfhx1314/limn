package com.limn.frame.results;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.tools.ant.taskdefs.optional.net.SetProxy;




import com.limn.tool.exception.ParameterException;
import com.limn.frame.control.Test;
import com.limn.tool.parameter.Parameter;
import com.limn.tool.common.DateFormat;

public class UploadServerData implements DataResults {
	private Connection conn = null;

	private boolean isConnect = false;
	
	private String moduleName = null;
	
	private int ID = 0;
	private String testCaseName = null;
	private int executedCase = 0;
	private int successedCase = 0;
	private int sumCaseCount = 0;
	
	private int stepID = 0;
	private int stepNumID = 0;
	@Override
	public void init() {

		initDataBase();
		if(isConnect){
			String[][] id = executeSQL("Select ID from TestCase order by ID Desc");
			if(id.length==0){
				ID = 1;
			}else{
				ID = Integer.valueOf(id[0][0]) + 1;
			}
			
//			String[][] name = executeSQL("Select id from cp_testitem where code = '" + Parameter.TESTNAME + "'");
//			if(name.length ==0){
//				testCaseName = "unKonw";
//			}else{
//				testCaseName = name[0][0];
//			}

			testCaseName = Parameter.TESTNAME;

			InetAddress addr = null;
			String IP = "";
			try {
				addr = InetAddress.getLocalHost();
				IP = addr.getHostAddress().toString();
			} catch (UnknownHostException e) {
			}
//			sumCaseCount = Test.getCurrentRowCount();
			sumCaseCount = 0;
			String sql = "insert into TestCase(id,version,CaseName,SQLType,ChatType,ResultsPath,CreateTime,CreatePC,TotalNum,ExecutedNum,SuccessfulNum) "
					+ "values('" + ID +"','" + Parameter.VERSION + "','"
					+ testCaseName + "','"
					+ Parameter.DBTYPE + "','"
					+ Parameter.PLATVERSION + "','"
					+ Parameter.RESULT_FOLDER + "','"
					+ DateFormat.getDate() + "','"
					+ IP + "','"
					+ sumCaseCount + "','"
					+ executedCase + "','"
					+ successedCase + "')";
			executeSingleSQL(sql);
		}
		
	}

	@Override
	public void addSheet(int index) {

		if(isConnect){
			sumCaseCount = sumCaseCount + Test.tc.getTableSheetCount();
			executeSingleSQL("update  TestCase set TotalNum = '" + sumCaseCount + "' where id = '" + ID + "'");
		}
	}

	@Override
	public void addModule(String moduleName) {
		this.moduleName = moduleName;
		if(isConnect){
			
		}
		
	}

	@Override
	public void addCase(String caseNo) {
		executedCase++;
		if(isConnect){
			int currentRow = Test.tc.getCurrentRow() + 1;
			int sum = sumCaseCount -  (currentRow - executedCase) - 1;
			executeSingleSQL("update TestCase Set TotalNum ='" + sum + "' where ID = '" + ID + "'");
			executeSingleSQL("update TestCase Set ExecutedNum ='" + executedCase + "' where ID = '" + ID + "'");
			
			
			
			String[][] tmpStepIDArr = executeSQL("Select StepID from TestCaseStep order by StepID Desc");
			
			if(tmpStepIDArr.length == 0){
				stepID = 1;
			}else{
				stepID = Integer.parseInt(tmpStepIDArr[0][0]) + 1;
			}
			
			String sql = "insert into TestCaseStep(StepID,ID,StepNo,ModuleName,CreateTime,ExcelNo) Values("
					+ "'" + stepID 	+ "'," + "'" 
					+	ID + "','" + caseNo + "','" 
					+  moduleName + "','" + DateFormat.getDate() + "','"
					+ Test.getSheetIndex() + "-" + Test.getCurrentRow() + "')";
			executeSingleSQL(sql);
		}
		stepNumID = 1;
	}
	
	@Override
	public void addStep(String stepName, String result) {
		if(isConnect){
			String sql = "insert into StepDescription(id,StepID,StepNumID,Step,CreateTime) Values('" 
					+ ID + "','" + stepID + "','" + stepNumID + "','"  + stepName  + "','" + DateFormat.getDate() + "')";
			executeSingleSQL(sql);
			stepNumID ++;
		}
		
	}
	
	@Override
	public void addActualResults(String[] results) {
		if(isConnect){
			String sumResults = null;
			for(String result:results){
				if(sumResults==null){
					sumResults = result;
				}else{
					sumResults = sumResults + "\n" + result;
				}
			}
			executeSingleSQL("update TestCaseStep Set Actual ='" + sumResults + "' where StepID = '" + stepID + "'");
		}
		
	}

	@Override
	public void addExpectedResults(String[] results) {
		if(isConnect){
			String sumResults = null;
			for(String result:results){
				if(sumResults==null){
					sumResults = result;
				}else{
					sumResults = sumResults + "\n" + result;
				}
			}
			executeSingleSQL("update TestCaseStep Set Expected ='" + sumResults + "' where StepID = '" + stepID + "'");
		}
		
	}

	@Override
	public void addResult(boolean isPass) {
		int result = 0;
		if(!isPass){
			result = -1;
		}
		
		if(isConnect){
			if(isPass){
				successedCase++;
				executeSingleSQL("update TestCase Set SuccessfulNum ='" + successedCase + "' where ID = '" + ID + "'");
			}
			executeSingleSQL("update TestCaseStep Set Results ='" + result + "' where StepID = '" + stepID + "'");
			
		}
		
	}

	
	private void initDataBase(){
		
//		isConnect = true;
//		try {
//			Class.forName(Variable.resolve("[UploadServer_driver]"));
//			conn = DriverManager.getConnection(Variable.resolve("[UploadServer_url]"),
//					Variable.resolve("[UploadServer_user]"),
//					Variable.resolve("[UploadServer_passwd]"));
//		} catch (ClassNotFoundException e) {
//			isConnect = false;
//		} catch (SQLException e) {
//			isConnect = false;
//			try {
//				throw new ParameterException(ParameterException.SQL_Exception, "上传测试结果服务器连接失败");
//			} catch (ParameterException e1) {
//				e1.printStackTrace();
//			}
//		}
		
	}

	
	private String[][] executeSQL(String sql){
		initDataBase();
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
			e.printStackTrace();
		}
		return res;
		
		
	}
	
	private int executeSingleSQL(String sql){
		initDataBase();
		Statement sta = null;
		try {
			sta = conn.createStatement();
			sta.executeUpdate(sql);

//			RunLog.printLog(sql, 0);

		} catch (SQLException e) {
//			RunLog.printLog(sql + " 未执行成功:" + e.getMessage(), 2);
			return -1;
		} finally {
			
			try {
				sta.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public void addBitMap(String bitMapPath) {
		if(isConnect){
			String sql = "update StepDescription Set BitMap = '"
					+ bitMapPath + "' where ID = '" + ID + "' and StepID = '" 
							+ stepID + "' and StepNumID = '" + String.valueOf(stepNumID -1)+ "'";
			executeSingleSQL(sql);
		}
		
		
	}

	@Override
	public void addRelatedCase(String CaseNo) {
		if(isConnect){
			String sql = "update TestCaseStep Set RelatedCase = '" 
					+ CaseNo + "' , RelatedCaseID = '" 
					+ String.valueOf(stepID-1) + "' where ID = '" + ID + "' and StepID = '" + stepID + "'";
			executeSingleSQL(sql);

		}
		
	}

	@Override
	public void addCustom(String node, String value) {
		
	}

	@Override
	public void addTestCaseCount(String count) {
		if(isConnect){
			int sum = executedCase;
			executeSingleSQL("update TestCase Set TotalNum ='" + sum + "' where ID = '" + ID + "'");
		}
		
	}
	
	

}
