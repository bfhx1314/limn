package com.automation.tool.excelEdit;

import com.automation.exception.MException;
import com.automation.tool.util.Print;

import java.util.List;
import java.util.Map;

/**
 * Excel放在Data文件夹下</p>
 *Excel命名方式：模块名称.xlsx</p>
 *Sheet名称为案例名称
 */
public class ExcelDataProvider {

    public Object getExcelData(String excelPath) {
        Object retrunData = null;
        try {
            ExcelEditor excelEditor = new ExcelEditor();
            excelEditor.openExcelTestCase(excelPath);
            retrunData = excelEditor.getExcelCaseResult();
        }catch (Exception e){
            MException.printAllStackTrace(e);
        }
        return retrunData;
    }

    /**
     * 报存到指定excel
     * @param excelDataObject 所有case的数据 数据结构：Map<String, Map<String, List<Map<String, String>>>>
     * @param excelPath excel全路径
     */
    public void setExcelData(Object excelDataObject, String excelPath) {
        try{
            ExcelEditor excelEditor = new ExcelEditor();
            excelEditor.openExcelTestCase(excelPath);
            Map<String, ?> excelData = (Map<String, Map<String, List<Map<String, String>>>>) excelDataObject;
            for(Map.Entry<String,?> entry : excelData.entrySet()){
                String sheetName = entry.getKey();
                Map<String,?> sheetMap = (Map<String, ?>) entry.getValue();
                for(Map.Entry<String,?> sheetEntry : sheetMap.entrySet()){
                    String caseName = sheetEntry.getKey();
                    List<Map<String,String>> caseRunStepList = (List<Map<String,String>>) sheetEntry.getValue();
                    excelEditor.saveModuleCase(sheetName,caseName,caseRunStepList);
                }
            }
        }catch (Exception e){
            Print.log("保存用例失败。");
            MException.printAllStackTrace(e);
        }

    }
}