package com.automation.frame.panel;

import com.automation.driver.Driver;
import com.automation.exception.ExceptionInfo;
import com.automation.exception.MException;
import com.automation.exception.RunException;
import com.automation.keyword.ExcelType;
import com.automation.keyword.BaseKeyWordType;
import com.automation.keyword.KeyWordImpl;
import com.automation.tool.parameter.Parameters;
import com.automation.tool.util.Common;
import com.automation.tool.util.Print;
import org.openqa.selenium.WebElement;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class ExecuteSingleStepRunPanel extends CustomPanel {

    // 执行
    JButton execute = new JButton("执行");
    // 插入执行
    JButton executeInsert = new JButton("插入执行");
    // 验证
    JButton verify = new JButton("验证");

    // 提示是否插入到“用例步骤”面板
    public static boolean popDialog = false;

    /**
     * 选中的WebElement
     */
    private WebElement selectedHighLightWebElement;

    public ExecuteSingleStepRunPanel(){
        // 设置"执行"监听
        setExecuteListener();
        // 设置“插入执行”监听
        setExecuteInsertListener();
        // 设置“验证”监听
        setVerifyListerer();
        this.getConstraints().insets = new Insets(5,3,1,3);
        this.addComponent(execute,1,1,1,1,0.1,0.1);
        this.addComponent(executeInsert,1,2,1,1,0.1,0.1);
        this.addComponent(verify,1,3,1,1,0.1,0.1);
//        this.addComponent(executeAgain,1,3,1,1,0.1,0.1);
    }

    /**
     * 设置“验证”监听
     */
    private void setVerifyListerer() {
        verify.setEnabled(false);

        verify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean flag = true;
                // TODO 统一在ExecuteSingleStepEditPanel中处理提示
                String locator = ExecuteSingleStepEditPanel.getWebElementXPathValue();
                String keyword = ExecuteSingleStepEditPanel.getKeyWordSelectedValue();
                int keywordType = ExecuteSingleStepEditPanel.getKeyWordType(keyword);
                if (keywordType == 4){
                    if (locator.isEmpty()){
                        // 判断xpath为空，提示并高亮
                        Print.logRed("XPath输入框不能为空。");
                        ExecuteSingleStepEditPanel.xpathValue.setBorder(new LineBorder(Color.RED.darker()));
                        flag = false;
                    }else {
                        ExecuteSingleStepEditPanel.setDefaultBorder(ExecuteSingleStepEditPanel.xpathValue);

                        try {
                            //取消高亮
                            if(selectedHighLightWebElement!=null) {
                                Driver.cancelHighLightWebElement(selectedHighLightWebElement);
                            }
                        } catch (RunException e1) {
                            Print.logRed("验证时，取消元素高亮失败");
                            e1.printStackTrace();
                        }

                        try {
                            // 设置该元素高亮
                            selectedHighLightWebElement = Driver.getElement(locator);
                            Driver.highLightWebElement(selectedHighLightWebElement);
                        } catch (RunException e1) {
                            Print.logRed("验证时，设置元素高亮失败");
                            e1.printStackTrace();
                        }
                    }
                }
/*
                String expect = ExecuteSingleStepEditPanel.getExpectValue();
                if (expect.isEmpty()){
                    // 判断预期结果为空，提示并高亮
                    Print.logRed("预期值不能为空。");
                    ExecuteSingleStepEditPanel.expectValue.setBorder(new LineBorder(Color.RED.darker()));
                    flag = false;
                }else {
                    ExecuteSingleStepEditPanel.setDefaultBorder(ExecuteSingleStepEditPanel.expectValue);
                }
                */
                if (flag){
                    // 开始验证，验证时不插入到“用例步骤”面板
                    executeStep(3);
                }
            }
        });

    }

    /**
     * 设置"执行"监听
     */
    private void setExecuteListener() {
        execute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO 执行的时候，判断参数为非空。
                if (popDialog){
                    if (isPopDialog()){
                        // 执行
                        executeStep(1);
                    }
                }else {
                    // 执行
                    executeStep(1);
                }
            }
        });
    }

    /**
     * 设置“插入执行”监听
     */
    private void setExecuteInsertListener() {
        executeInsert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 执行
                if (MultiStepExecutionPanel.hasSelectedRow()) {
                    if (popDialog) {
                        if (isPopDialog()) {
                            // 执行
                            executeStep(2);
                        }
                    }else {
                        executeStep(2);
                    }
                }else {
                    Print.logRed("请选择“用例步骤”面板中被插入的行");
                }
            }
        });
    }

    private boolean isPopDialog(){
        boolean bool;
        int status = JOptionPane.showConfirmDialog(ExecuteSingleStepRunPanel.this, "验证失败，是否要执行并插入到“用例步骤”", "警告", JOptionPane.YES_NO_OPTION);
        if(status == 0){
            bool = true;
        }else {
            bool = false;
        }
        return bool;
    }

    /**
     * 执行单步用例
     * @param executeType 1:执行，2：插入执行
     */
    private void executeStep(int executeType){
        Map runStep = new HashMap<String,String>();
        String step = ExecuteSingleStepEditPanel.getKeyWordSelectedValue();
        // 如果是启动浏览器，清空日志内容
        if (step.equals(BaseKeyWordType.START_BROWSER)){
            Print.clearLog();
        }
        runStep.put(ExcelType.STEP,step);
        runStep.put(ExcelType.OBJECT,ExecuteSingleStepEditPanel.getWebElementName());
        runStep.put(ExcelType.XPATH,ExecuteSingleStepEditPanel.getWebElementXPathValue());
        runStep.put(ExcelType.PARAM,ExecuteSingleStepEditPanel.getParameterValue());
        runStep.put(ExcelType.EXPECT,ExecuteSingleStepEditPanel.getExpectValue());
        execute.setEnabled(false);
        executeInsert.setEnabled(false);
        verify.setEnabled(false);
        // 执行
        new Thread(new ExecuteStep(runStep, executeType)).start();
        if (!runStep.get(ExcelType.STEP).equals(BaseKeyWordType.ENCRYPT_BASE64)
                &&!runStep.get(ExcelType.STEP).equals(BaseKeyWordType.DECRYPT_BASE64)){
            if (executeType == 1){
                MultiStepExecutionPanel.addRowToTable(runStep);
            }else if(executeType == 2){
                MultiStepExecutionPanel.insertRowToTable(runStep);
            }
        }
    }

    /**
     * 单步执行
     */
    private class ExecuteStep implements Runnable {
        Map runStep;
        int executeType;
        String url = "";
        public ExecuteStep(Map step, int executeType) {
            this.runStep = step;
            this.executeType = executeType;
        }

        @Override
        public void run() {
            try {
                if (runStep.get(ExcelType.STEP).equals(BaseKeyWordType.ENCRYPT_BASE64)){
                    ExecuteSingleStepEditPanel.setActualValue(Common.encryptBASE64(runStep.get(ExcelType.PARAM).toString().getBytes()));
                }else if (runStep.get(ExcelType.STEP).equals(BaseKeyWordType.DECRYPT_BASE64)){
                    ExecuteSingleStepEditPanel.setActualValue(new String(Common.decryptBASE64(runStep.get(ExcelType.PARAM).toString())));
                }else {
                    // 根据关键字初始化panel
                    setPanelSetting();
                    KeyWordImpl keyWordImpl = new KeyWordImpl();
                    keyWordImpl.setKWD(KeyWordInfoPanel.getKWD());
                    keyWordImpl.start(runStep);
                    setPanelSettingAfter();
                    // 普通关键字，吧所有输入框清空
                    ExecuteSingleStepEditPanel.setJTextFieldEmpty();
                    popDialog = false;
                }
            } catch (MException e) {
                // 判断 验证结果，失败时保留所有参数，否则非验证时清空。
                if (e.getMessage().contains(ExceptionInfo.get(RunException.VALIDATE_FAIL))) {
                    // 验证失败时，把对比结果设置为红色
                    ExecuteSingleStepEditPanel.setResultValueFail();
                    popDialog = true;
                    if (runStep.get(ExcelType.STEP).equals(BaseKeyWordType.SYNC)){
                        Parameters.ACTUAL_VALUE = "NULL";
                    }
                    ExecuteSingleStepEditPanel.setActualValue(Parameters.ACTUAL_VALUE);
                }else if(e.getMessage().contains(ExceptionInfo.get(RunException.VALIDATE_PASS))){
                    // 如果是验证步骤，把对比结果设置为绿色
                    ExecuteSingleStepEditPanel.setResultValuePass();
                    popDialog = false;
                    if (runStep.get(ExcelType.STEP).equals(BaseKeyWordType.SYNC)){
                        Parameters.ACTUAL_VALUE = runStep.get(ExcelType.EXPECT).toString();
                    }
                    ExecuteSingleStepEditPanel.setActualValue(Parameters.ACTUAL_VALUE);
                }else {
                    // 普通关键字，吧所有输入框清空
                    ExecuteSingleStepEditPanel.setJTextFieldEmpty();
                    popDialog = false;
                    ExecuteSingleStepEditPanel.setActualValue("");
                    try {
                        setPanelSettingAfter();
                    } catch (RunException e1) {
                        e1.printStackTrace();
                    }
                }
//                e.printStackTrace();
            }finally {
                execute.setEnabled(true);
                executeInsert.setEnabled(true);
                verify.setEnabled(true);
//                String keyWord = runStep.get(ExcelType.STEP).toString();
//                ExecuteSingleStepEditPanel.setEditadbleByKeyWord(keyWord);
            }
        }


        private void setPanelSetting() throws RunException {
            if (null != Driver.getWebDriver()){
                if (!runStep.get(ExcelType.STEP).equals(BaseKeyWordType.VERIFY_ALERT)
                        && !runStep.get(ExcelType.STEP).equals(BaseKeyWordType.CLOSE_ALERT)){
                    url = Driver.getCurrentUrl();
                }
                if (runStep.get(ExcelType.STEP).equals(BaseKeyWordType.ENTER_IFRAME)
                        || runStep.get(ExcelType.STEP).equals(BaseKeyWordType.CHANGE_BRO_TAB)
                        || runStep.get(ExcelType.STEP).equals(BaseKeyWordType.CLOSE_BRO_TAB)
                        || runStep.get(ExcelType.STEP).equals(BaseKeyWordType.CLOSE_BROWSER)
                        || runStep.get(ExcelType.STEP).equals(BaseKeyWordType.EXIT_IFRAME)
                        || runStep.get(ExcelType.STEP).equals(BaseKeyWordType.ACCESS_URL)){
                    // 清空webElement列表
                    LoadBrowserPanel.removeAllWebElements();
                    Driver.cancelHighLightWebElement("");
                    if (!runStep.get(ExcelType.STEP).equals(BaseKeyWordType.CLOSE_BRO_TAB)){
                        LoadBrowserPanel.initBaseWebElementTagNameValue();
                    }
                }
                if (runStep.get(ExcelType.STEP).equals(BaseKeyWordType.START_BROWSER)
                        || runStep.get(ExcelType.STEP).equals(BaseKeyWordType.ACCESS_URL)){
                    LoadBrowserPanel.setBaseWebElementButtonUnEnabled();
                    LoadBrowserPanel.setBaseSelectWebElementTagNameUnEditable();
                    LoadBrowserPanel.setBaseSelectWebElementTagNameUnEnabled();
                    LoadBrowserPanel.setSpecialWebElementButtonUnEnabled();
//                    LoadBrowserPanel.setSpecialSelectWebElementTagNameUnEditable();
//                    LoadBrowserPanel.setSpecialSelectWebElementTagNameUnEnabled();
                }
            }
        }

        private void setPanelSettingAfter() throws RunException {
            String url = "";
            url = Driver.getCurrentUrl();
            if (!url.equals(this.url)){
                // 清空webElement列表
                LoadBrowserPanel.setUrlToTile(url);
                LoadBrowserPanel.clearFindWebElementsInfo();
                LoadBrowserPanel.initBaseWebElementTagNameValue();
            }
            if (runStep.get(ExcelType.STEP).equals(BaseKeyWordType.START_BROWSER)
                    || runStep.get(ExcelType.STEP).equals(BaseKeyWordType.ACCESS_URL)){
                LoadBrowserPanel.setBaseWebElementButtonEnabled();
                LoadBrowserPanel.setBaseSelectWebElementTagNameEditable();
                LoadBrowserPanel.setSpecialWebElementButtonEnabled();
//            LoadBrowserPanel.setSpecialSelectWebElementTagNameEditable();
                LoadBrowserPanel.setSearchLocatorValueEditable();
            }
        }
    }

    /**
     * 设置验证按钮是否可编辑
     * @param enabled true||false
     */
    public void setVerifyButtonEnabled(boolean enabled){
        verify.setEnabled(enabled);
    }

}
