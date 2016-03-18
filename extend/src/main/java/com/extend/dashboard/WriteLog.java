package com.extend.dashboard;

import com.extend.tool.parameter.Parameters;
import com.automation.exception.DBException;
import com.automation.report.CaseGather;
import com.automation.report.CaseInfo;
import com.automation.report.TestGather;
import com.automation.tool.util.DataBaseHelper;
import com.automation.tool.util.DateFormat;
import com.automation.tool.util.Print;
import com.automation.tool.util.StringUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by snow.zhang on 2016/1/19.
 */
public class WriteLog {


    public static void insert(TestGather testGather){
        if (Parameters.isInsertDashBoard.equals("true")){
            try {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(Parameters.dashBoardDBUrl,Parameters.dashBoardUserName,Parameters.dashBoardPassword);
                String sql = "insert into test_log values(?)";
                String values = "";
                values += "null,";
                String date = DateFormat.getDate("yyyy-MM-dd");
                String batchNo = date;
                String prjName = "";
                if (com.automation.tool.parameter.Parameters.PRJ_NAME.equals("AE")){
                    prjName = "ApolloAE_UI_New";
                }else{
                    prjName = com.automation.tool.parameter.Parameters.TESTCASE_NAME;
                }
                dataBaseHelper.executeQuery("select BatchNo from test_log where ProjectName='"+prjName+"' and BatchNo like '"+date+"%' order by BatchNo desc limit 1");
                LinkedList<Map<String, String>> sqlData = dataBaseHelper.getSqlData();
                if (sqlData.size() == 1){
                    batchNo += getBatchNo(sqlData.get(0).get("BatchNo"));
                }
                values += "'"+batchNo+"',";
                values += "'"+prjName+"',";
                List<CaseGather> listCaseGather =  testGather.getSheetList();
                for(CaseGather caseGather:listCaseGather) {
                    String valueT = values;
                    valueT += "'" + caseGather.getSheetName() + "',";
                    for(CaseInfo caseInfo:caseGather.getCaseInfoList()) {
                        String valueTW = valueT;
                        valueTW += "'" + caseInfo.getCaseName() + "',";
                        valueTW += "'" + DateFormat.getDate("yyyy-MM-dd HH:mm:ss",testGather.getStartTimeMillis()) + "',";
                        valueTW += "'" + DateFormat.getDate("yyyy-MM-dd HH:mm:ss",testGather.getEndTimeMillis()) + "',";
                        valueTW += "'" + caseInfo.getSpendTime() + "',";
                        if(CaseInfo.status.FAIL.equals(caseInfo.getCaseStatus())
                                ||CaseInfo.status.ERROR.equals(caseInfo.getCaseStatus())) {
                            valueTW += "'FAILURE',";
                        }else if(CaseInfo.status.SKIP.equals(caseInfo.getCaseStatus())) {
                            valueTW += "'SKIP',";
                        }else if(CaseInfo.status.PASS.equals(caseInfo.getCaseStatus())
                                ||CaseInfo.status.DONE.equals(caseInfo.getCaseStatus())){
                            valueTW += "'SUCCESS',";
                        }
                        valueTW += "''";
                        String singleSql = sql.replace("?",valueTW);
                        dataBaseHelper.executeUpdate(singleSql);
                    }
                }
            } catch (DBException e) {
                Print.log(e.getMessage());
            }
        }
    }

    private static String getBatchNo(String strBatchNo){
        String batchNo = "";
        if (!strBatchNo.isEmpty()){
            if (strBatchNo.contains("_")){
                String str = strBatchNo.split("_")[1];
                if (!str.isEmpty()){
                    try{
                        batchNo = "_" + StringUtil.addZeroToIntStr(Integer.parseInt(str) + 1, 3);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }else {
                batchNo = "_" + StringUtil.addZeroToIntStr(1, 3);
            }
        }
        return batchNo;
    }


}
