package com.extend.keyword;

import com.extend.tool.httpClient.HttpClientUtil;
import com.extend.tool.httpClient.ResponseInfo;
import com.automation.driver.Driver;
import com.automation.exception.*;
import com.automation.keyword.ExcelType;
import com.automation.tool.parameter.Parameters;
import com.automation.tool.util.*;
import com.google.gson.Gson;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by snow.zhang on 2015/12/29.
 */
public class OSRunKeyWordImpl {

    private static final String wsResultFlag = "<WS_RESULT>";

    /**
     * 验证canvas中“最近七日的发电量比较”单个柱子>0
     * @param step
     */
    public void canvasSingleGreatethenZero(Map<String,String> step) throws RunException {
        boolean boolResult = false;
        String title = step.get(ExcelType.PARAM);
        String result = Driver.runScript(getJsScript() + "return canvasSingleGreatethenZero(arguments[0])", title).toString();
        if (result.equals("1")){
            boolResult = true;
        }
        Robot rb = null;
        try {
            rb = new Robot();
            rb.mouseMove(683, 384);
            rb.mousePress(KeyEvent.BUTTON1_MASK);
            rb.mouseRelease(KeyEvent.BUTTON1_MASK);
            rb.delay(5000);
            rb.mousePress(KeyEvent.BUTTON1_MASK);
            rb.mouseRelease(KeyEvent.BUTTON1_MASK);
            rb.delay(1000);
            rb.keyPress(KeyEvent.VK_END);
            rb.keyRelease(KeyEvent.VK_END);
            rb.delay(500);
            rb.keyPress(KeyEvent.VK_END);
            rb.keyRelease(KeyEvent.VK_END);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        // 截图
//        WebElement webElement = getElementByLocator(xpath);
//        CaptureAndLog("OS生产报告-最近七日的发电量比较", webElement);
        String xpath = step.get(ExcelType.XPATH);
        if(boolResult) {
            WebElement webElement = Driver.getElement(xpath,1);
            Driver.highLightWebElement(webElement);
            Screenshot screenshot = new Screenshot();
            screenshot.snapShot(Print.getLogs());
        }else {
            ErrorInfo errorInfo = new ErrorInfo(xpath);
            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL),errorInfo);
        }
    }

    private static String getJsScript(){
        return "// 设置高亮\n" +
                "function setHighLight(node){\n" +
                "    var nodeName = node.nodeName;\n" +
                "    if (nodeName=='g'||(nodeName=='text'&& node.parentNode.nodeName=='g')){\n" +
                "        node.style.stroke = 'yellow';\n" +
                "        node.style.strokeWidth = '2px';\n" +
                "    }else {\n" +
                "        node.style.border = '2px solid yellow';\n" +
                "    }\n" +
                "}\n" +
                "// 取消高亮\n" +
                "function cancelHighLight(node){\n" +
                "    var nodeName = node.nodeName;\n" +
                "    if (nodeName=='g'||(nodeName=='text'&&node.parentNode.nodeName=='g')){\n" +
                "        node.style.stroke = '';\n" +
                "        node.style.strokeWidth = '';\n" +
                "    }else {\n" +
                "        node.style.border = '';\n" +
                "    }\n" +
                "}\n" +
                "function canvasSingleGreatethenZero(title){\n" +
                "\tvar boolResult = false;\n" +
                "\tvar charDataLen = charData.length;\n" +
                "\tfor(var i=0;i<charDataLen;i++){\n" +
                "\t\tvar charDataObject = charData[i];\n" +
                "\t\tvar charDataTitle = charDataObject.title;\n" +
                "\t\tif (title == charDataTitle){\n" +
                "\t\t\tvar series = charDataObject.option.series;\n" +
                "\t\t\tvar seriesLen = series.length;\n" +
                "\t\t\tfor(var j=0;j<seriesLen;j++){\n" +
                "\t\t\t\tvar sery = series[j];\n" +
                "\t\t\t\t// 暂时只验发电量>0\n" +
                "\t\t\t\tif (\"发电量\" == sery.name){\n" +
                "\t\t\t\t\tvar seryData = sery.data;\n" +
                "\t\t\t\t\tvar seryDataLen = seryData.length;\n" +
                "\t\t\t\t\tvar result = false;\n" +
                "\t\t\t\t\tfor(var k=0;k<seryDataLen;k++){\n" +
                "\t\t\t\t\t\tif (seryData[k] > 0){\n" +
                "\t\t\t\t\t\t\tresult = true;\n" +
                "\t\t\t\t\t\t\tbreak;\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t\tif (result){\n" +
                "\t\t\t\t\t\tboolResult = true;\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "\tif (!boolResult){\n" +
                "\t\treturn 0;\n" +
                "\t}else {\n" +
                "\t\treturn 1;\n" +
                "\t}\n" +
                "}";
    }

    /*
    Result:["<value><time>%",{"value":["data[0].pr[0].value.{%}", "100", "2", "0.00#", "=", "-1", "--"], "time":["data[0].pr[0].time"]}];
    URL:http://apolloae-beta.envisioncn.com/apollohds/ws/rest/timeSeqKpisV2;
    Param:{"mdmIds":["d41c9775703e4631bbff2fcb4899637e"],
     "objType":"SITE"
    }
    //Result:["<value><time>%",{"value":["data[0].pr[0].value.{%}", "100", "2", "0.00#", "=", "-1", "--"], "time":["data[0].pr[0].time"]}]
    //说明：["总表达式<别名>", {"别名":["ws值及连接字串","乘数","保留位数","格式化","异常计算符号","异常值","异常情况表达式"]}]
    //总表达式：引用别名时用<>引入
    //ws值及连接字串：ws中所引用的值data[0].pr[0].value，以及连接字串用.{}/{}.引入
    //异常情况计算符号：=,<,>,<=,>=,match
    //异常值：计算符号所处理的值
    //异常情况表达式：ws中所引用的值data[0].pr[0].value，以及连接字串用.{}/{}.引入
    */
    /**
     * 验证ws post请求
     * @param step
     * @throws MException
     */
    public void wsPostVerify(Map<String,String> step) throws MException {
        try {
            String expect = step.get(ExcelType.EXPECT);
            if(RegExp.matchRegExp(expect, "^(Result:([\\s\\S]*);([\\s\\S]*)URL:([\\s\\S]*);([\\s\\S]*)Param:([\\s\\S]*))$")) {
                String[] expectArray = expect.split(";");
                String result = StringUtil.trimAllSpace(expectArray[0].replaceFirst("Result:", ""));
                String url = StringUtil.trimAllSpace(expectArray[1].replaceFirst("URL:", ""));
                String param = expectArray[2].replaceFirst("Param:", "");
                if(!StringUtil.isEmpty(url) && !StringUtil.isEmpty(param)) {
                    HttpClientUtil httpClientUtil = new HttpClientUtil();
                    ResponseInfo responseInfo = httpClientUtil.executePost(url, StringUtil.trim(param));
                    if(responseInfo.status==200 && !StringUtil.isEmpty(responseInfo.content)) {
                        String content = responseInfo.content;
//                        String expectResult = getResultFromWS(result, content);

                        Gson gson = new Gson();
                        //获取result结果集
                        List resultList = gson.fromJson(result, List.class);
                        String expectResult = (String)resultList.get(0);
                        Map<String, List> resultInfoMap = (Map<String, List>)resultList.get(1); //预期结果中的值
                        for(String key:resultInfoMap.keySet()) {
                            String singleResult = getSingleResultFromWS(resultInfoMap.get(key), content);
                            expectResult = expectResult.replace("<"+key+">", singleResult);
                        }

                        //比较
                        String actualValue;
                        String attribute = step.get(ExcelType.PARAM);
                        String xpath = step.get(ExcelType.XPATH);
                        WebElement webElement = Driver.getElement(xpath);
                        if (StringUtil.isEmpty(attribute)) {
                            actualValue = Driver.getValueByTag(webElement);
                        } else {
                            actualValue = Driver.getAttributeValue(webElement, attribute);
                        }
                        Parameters.ACTUAL_VALUE = actualValue;
                        boolean validatedResult = Driver.checkPoint(expectResult, actualValue, step.get(ExcelType.OBJECT),webElement);
                        if(!validatedResult) {
                            ErrorInfo errorInfo = new ErrorInfo(xpath);
                            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
                        }else {
                            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
                        }

                    }else {
                        //请求出错
                        throw new MException("请求出错。url："+url+"， result："+result+"，param："+param);
                    }
                }else {
                    //预期结果格式错误
                    throw new MException("请求url或参数为空");
                }
            }else {
                //预期结果格式错误
                throw new MException("预期结果格式错误。预期结果："+expect);
            }
        }catch (MException e) {
            throw e;
        }catch (Exception e) {
            throw new MException(e);
        }

    }

    /**
     * 处理result中的map里的单条数据
     * @param resultInfo
     * @param wsContent
     * @return
     * @throws MException
     */
    public String getSingleResultFromWS(List<String> resultInfo, String wsContent) throws MException{
        try {
            String result = resultInfo.get(0);
            String[] returnResultArray = getSingleReturnResultFromWS(result, wsContent);
            String wsReturnResult = returnResultArray[0];
            String finalResult = returnResultArray[1];

            //处理数字 "value":["data[0].pr[0].value.{%}", "100", "2", "0.00#", "=", "-1", "{--}"]
            if(resultInfo.size()>1 && !StringUtil.isEmpty(wsReturnResult)) {
                wsReturnResult = getNumberFormatReturnStr(resultInfo, wsReturnResult, wsContent);
            }

            //数字处理结束，处理字串
            String returnResult = "";
            if(!StringUtil.isEmpty(wsReturnResult)) {
                returnResult = finalResult.replace(wsResultFlag, wsReturnResult);
            }else {
                returnResult = finalResult.replace(wsResultFlag, "");
            }

            //处理异常 "value":["data[0].pr[0].value.{%}", "100", "2", "0.00#", "=", "-1", "--"]
            if(resultInfo.size()==7) {
                String symbol = resultInfo.get(4); //异常情况计算符号：=,<,>,<=,>=,match
                String exceptStr = resultInfo.get(5);
                String exceptReturnResult = resultInfo.get(6);

                if(isSymbolTrue(wsReturnResult, symbol, exceptStr)) {
                    String[] expectResultArray = getSingleReturnResultFromWS(exceptReturnResult, wsContent);
                    String expectWsReturnResult = expectResultArray[0];
                    String expectFinalResult = expectResultArray[1];
                    //处理数字 "value":["data[0].pr[0].value.{%}", "100", "2", "0.00#", "=", "-1", "{--}"]
                    if(resultInfo.size()>1 && !StringUtil.isEmpty(expectWsReturnResult)) {
                        expectWsReturnResult = getNumberFormatReturnStr(resultInfo, expectWsReturnResult, wsContent);
                    }

                    //数字处理结束，处理字串
                    if(!StringUtil.isEmpty(expectWsReturnResult)) {
                        returnResult = expectFinalResult.replace(wsResultFlag, expectWsReturnResult);
                    }else {
                        returnResult = expectFinalResult.replace(wsResultFlag, "");
                    }
                }
            }

            return returnResult;
        } catch (MException e) {
            throw e;
        } catch (Exception e) {
            throw new MException(e);
        }
    }

    /**
     * 处理数字
     * @param resultInfo
     * @param returnResult
     * @param wsContent
     * @return
     * @throws MException
     */
    public String getNumberFormatReturnStr(List<String> resultInfo, String returnResult, String wsContent) throws MException {
        try {
            //处理数字 "value":["data[0].pr[0].value.{%}", "100", "2", "0.00#", "=", "-1", "{--}"]
            if(resultInfo.size()>1 && !StringUtil.isEmpty(returnResult)) {
                String multiplyNum = resultInfo.size()>1?resultInfo.get(1):null;
                if(!StringUtil.isEmpty(multiplyNum) && !StringUtil.isNumber(multiplyNum)) {
                    String expectMultiplyNum = getSingleReturnResultFromWS(multiplyNum, wsContent)[0];
                    if(!StringUtil.isEmpty(expectMultiplyNum) && StringUtil.isNumber(expectMultiplyNum)) {
                        multiplyNum = expectMultiplyNum;
                    }
                }
                Integer scale = resultInfo.size()>2?StringUtil.strToInt(resultInfo.get(2)):null;
                RoundingMode roundingMode = RoundingMode.HALF_UP;
                String format = resultInfo.size()>3?resultInfo.get(3):null;
                returnResult = StringUtil.strNumberFormat(returnResult, multiplyNum, scale, roundingMode, format);
            }
            return returnResult;
        } catch (MException e) {
            throw e;
        } catch (Exception e) {
            throw new MException(e);
        }
    }

    /**
     * 从 wsContent 中 获取 result
     * 例：wsContent：{"pr":[{"value":123, "time":2015}]}
     *    result:pr[0].value.{%}
     *    那么返回的是123%
     * @param result
     * @param wsContent
     * @return
     * @throws MException
     */
    public String[] getSingleReturnResultFromWS(String result, String wsContent) throws MException{
        try {
            if(StringUtil.isEmpty(result) || StringUtil.isEmpty(wsContent)) {
                //请求结果为空，或者预期结果为空
                throw new MException("请求结果为空，或者预期结果为空。请求结果："+wsContent+", 预期结果："+result);
            }

            //获取result结果集
            List<String> resultList = new ArrayList<String>();
            String[] resultArray = result.split("\\.");
            String resultStart = "";
            for(String resultStr:resultArray) {
                if(resultStr.contains("[")) {
                    resultList.add(resultStr.substring(0, resultStr.indexOf("[")));
                    resultList.add(resultStr.substring(resultStr.indexOf("["), resultStr.length()));
                }else {
                    resultList.add(resultStr);
                }
                //过滤字串表达式
                if(!resultStr.contains("{") && StringUtil.isEmpty(resultStart)) {
                    resultStart = resultStr;
                }
            }

            //获取ws结果集
            Gson gson = new Gson();
            Object wsObject = null;
            if(resultStart.startsWith("[") && wsContent.startsWith("[")) {
                //List
                wsObject = gson.fromJson(wsContent, List.class);
            }else if(!resultStart.startsWith("[") && wsContent.startsWith("{")) {
                //Map
                wsObject = gson.fromJson(wsContent, Map.class);
            }else if(result.startsWith("{")) {
                //只有一个字串表达式{}
            }else {
                //请求结果和预期结果 格式不一致
                throw new MException("请求结果和预期结果 格式不一致。请求结果："+wsContent+", 预期结果："+result);
            }


            //获取值
            String finalResult = "";
            boolean handleWSObject = false;
            for(String singleResult:resultList) {
                if(singleResult.startsWith("[")) {
                    int index = StringUtil.strToInt(singleResult.replace("[", "").replace("]", ""));
                    wsObject = ((List)wsObject).get(index);
                    handleWSObject = true;
                }else if(singleResult.startsWith("{")) {
                    finalResult = finalResult + singleResult.replace("{", "").replace("}", "");
                }else {
                    wsObject = ((Map)wsObject).get(singleResult);
                    handleWSObject = true;
                }

                if(handleWSObject && !finalResult.contains(wsResultFlag)) {
                    finalResult = finalResult + wsResultFlag;
                }
            }

            String wsReturnResult = handleWSObject?StringUtil.objectToString(wsObject):"";
            return new String[]{wsReturnResult, finalResult};
        } catch (MException e) {
            throw e;
        } catch (Exception e) {
            throw new MException(e);
        }
    }

    /**
     * 通过symbol验证originalStr 和 exceptStr
     * 例如：exceptStr为0，symbol为=，如果originalStr=0，那么返回true，否则返回false
     * 异常情况计算符号：=,<,>,<=,>=,match
     * @param originalStr
     * @param symbol
     * @param exceptStr
     * @return
     */
    public boolean isSymbolTrue(String originalStr, String symbol, String exceptStr) {
        try {
            //异常情况计算符号：=,<,>,<=,>=,match
            switch (symbol) {
                case "=": return StringUtil.equalValue(originalStr, exceptStr);
                case "<": return StringUtil.biggerThan(exceptStr, originalStr);
                case ">": return StringUtil.biggerThan(originalStr, exceptStr);
                case "<=": return StringUtil.biggerThan(exceptStr, originalStr)||StringUtil.equalValue(originalStr, exceptStr);
                case ">=": return StringUtil.biggerThan(originalStr, exceptStr)||StringUtil.equalValue(originalStr, exceptStr);
                case "match": return RegExp.matchRegExp(originalStr, exceptStr);
                default: return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

   /* "Result:[""<value> "",{""value"":[""data[0].pr[i].value"", ""1"", ""1"", ""0.00#"", ""="", ""8882.46"", ""{--}""]}];
    URL:http://apolloae-beta.envisioncn.com/apollohds/ws/rest/timeSeqKpisV2;
    Param:{""mdmIds"":[""d41c9775703e4631bbff2fcb4899637e""],
        ""objType"":""SITE"",
                ""returnType"":""SITE"",
                ""beginTime"":""2015-01-01 00:00:00"",
                ""endTime"":""2015-12-31 23:59:59"",
                ""kpis"":[""pr""],
        ""timeGroup"":""Y""
    };
    MouseOverNum:1;
    MouseOverXpath:"*/
    //Result:[""<value>"",{""value"":[""data[0].pr[i].value"", ""1"", ""1"", ""0.00#"", ""="", ""8882.46"", ""{--}""]}]
    //说明：[i]根据MouseOverNum来迭代生成
    //MouseOverXpath根据MouseOverNum迭代生成，并执行mouseOver事件
    /**
     * 验证柱状图  ws post请求
     * @param step
     * @throws MException
     */
    public void wsPostVerifyColumn(Map<String,String> step) throws MException {
        try {
            String expect = step.get(ExcelType.EXPECT);
            if(RegExp.matchRegExp(expect, "^(Result:([\\s\\S]*);([\\s\\S]*)URL:([\\s\\S]*);([\\s\\S]*)Param:([\\s\\S]*);([\\s\\S]*)MouseOverNum:([\\s\\S]*);([\\s\\S]*)MouseOverXpath:([\\s\\S]*))$")) {
                String[] expectArray = expect.split(";");
                String result = expectArray[0].replaceFirst("Result:", "");
                String url = StringUtil.trimAllSpace(expectArray[1].replaceFirst("URL:", ""));
                String param = expectArray[2].replaceFirst("Param:", "");
                String mouseOverNumStr = StringUtil.trimAllSpace(expectArray[3].replaceFirst("MouseOverNum:", ""));
                String mouseOverXpath = StringUtil.trimAllSpace(expectArray[4].replaceFirst("MouseOverXpath:", ""));
                String[] heightArray = null;
                if(expect.contains("SkipHeight:") && expectArray.length>=6) {
                    String skipHeight = StringUtil.trimAllSpace(expectArray[5].replaceFirst("SkipHeight:", ""));
                    heightArray = skipHeight.split(",");
                }

                if(!StringUtil.isEmpty(url) && !StringUtil.isEmpty(param)) {
                    HttpClientUtil httpClientUtil = new HttpClientUtil();
                    ResponseInfo responseInfo = httpClientUtil.executePost(url, StringUtil.trim(param));
                    if(responseInfo.status==200 && !StringUtil.isEmpty(responseInfo.content)) {
                        String content = responseInfo.content;
                        Gson gson = new Gson();
                        //获取result结果集
                        List resultList = gson.fromJson(result, List.class);
                        String resultValue = (String)resultList.get(0);
                        Map<String, List> resultInfoMap = (Map<String, List>)resultList.get(1); //预期结果中的值
                        Integer mouserOverNum = StringUtil.strToInt(mouseOverNumStr);
                        if(mouserOverNum==null || mouserOverNum<=0) {
                            throw new MException("预期结果中MouseOverNum不正确");
                        }
                        Map<String, List<String>> toolTipMap = new HashMap<String, List<String>>();
                        for(String key:resultInfoMap.keySet()) {
                            List<String> resultInfoList = resultInfoMap.get(key);
                            String resultStr = resultInfoList.get(0);
                            List<String> resultStrList = new ArrayList<String>(); //解析每个值
                            for(int i=0; i<mouserOverNum; i++) {
                                if(resultStr.contains("[i]")) {
                                    resultInfoList.set(0, resultStr.replace("[i]", "["+i+"]"));
                                }
                                String singleResult = getSingleResultFromWS(resultInfoList, content);
                                resultStrList.add(singleResult);
                            }
                            toolTipMap.put("<"+key+">", resultStrList);
                        }

                        //list结果集
                        boolean returnFlag = true;
                        for(int i=0; i<mouserOverNum; i++) {
                            String expectResult = replaceKey(resultValue, toolTipMap, i);
                            String subMouseOverXpath = mouseOverXpath+"["+(i+1)+"]";

                            //判断柱子的高度是否大于0，如果等于0，那么跳过
                            WebElement mouseOverElement = Driver.getElement(subMouseOverXpath);
                            String height = Driver.getHtmlAttributeValue(mouseOverElement, "height");
                            String exceptHeight = "";
                            if(!StringUtil.isEmpty(height) && heightArray!=null && heightArray.length>0) {
                                for(String exHeight:heightArray) {
                                    if(StringUtil.equalValue(exHeight, height)) {
                                        exceptHeight = exHeight;
                                        break;
                                    }
                                }
                            }

                            if(StringUtil.isEmpty(exceptHeight) && !StringUtil.isEmpty(height)) {
                                //鼠标 mouseOver
                                Driver.mouseOver(subMouseOverXpath);
                                //比较
                                String actualValue;
                                String attribute = step.get(ExcelType.PARAM);
                                String xpath = step.get(ExcelType.XPATH);
                                WebElement webElement = Driver.getElement(xpath);
                                if (StringUtil.isEmpty(attribute)) {
                                    actualValue = Driver.getValueByTag(webElement);
                                } else {
                                    actualValue = Driver.getAttributeValue(webElement, attribute);
                                }

                                Parameters.ACTUAL_VALUE = actualValue;
                                boolean validatedResult = Driver.checkPoint(expectResult, actualValue, step.get(ExcelType.OBJECT),webElement);
                                returnFlag = validatedResult&&returnFlag;
                                if(!validatedResult) {
                                    ErrorInfo errorInfo = new ErrorInfo(xpath);
                                    throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
                                }
                            }else {
                                Print.log("第"+(i+1)+"根柱子高度为:"+(StringUtil.isEmpty(exceptHeight)?"0":exceptHeight)+"，或者不存在height属性，预期结果为："+expectResult);
                            }
                        }
                        //最终结果
                        if(returnFlag) {
                            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
                        }
                    }else {
                        //请求出错
                        throw new MException("请求出错。url："+url+"， result："+result+"，param："+param);
                    }
                }else {
                    //预期结果格式错误
                    throw new MException("请求url或参数为空");
                }
            }else {
                //预期结果格式错误
                throw new MException("预期结果格式错误。预期结果："+expect);
            }
        }catch (MException e) {
            throw e;
        }catch (Exception e) {
            throw new MException(e);
        }

    }

    //将关键字替换为实际值
    public String replaceKey(String resultValue, Map<String, List<String>> toolTipMap, int i) throws MException {
        try {
            int start = resultValue.indexOf("<");
            int end = resultValue.indexOf(">");
            if(start>=0 && end>=0) {
                String key = resultValue.substring(start, end + 1);
                resultValue = resultValue.replace(key, toolTipMap.get(key).get(i));
                if(resultValue.contains("<") && resultValue.contains(">")) {
                    resultValue = replaceKey(resultValue, toolTipMap, i);
                }
                return resultValue;
            }else {
                return resultValue;
            }
        } catch (Exception e) {
            throw new MException(e);
        }
    }

    public void sqlVerifyTable(Map<String,String> step) throws MException{
        try {
            String expect = step.get(ExcelType.EXPECT);
            if(RegExp.matchRegExp(expect, "^(DBConnect:([\\s\\S]*);([\\s\\S]*)TableRow:([\\s\\S]*);([\\s\\S]*)TableColumn:([\\s\\S]*);([\\s\\S]*)SQL:([\\s\\S]*))$")) {
                String[] expectArray = expect.split(";");
                String dbConnect = StringUtil.trimAllSpace(expectArray[0].replaceFirst("DBConnect:", ""));
                String tableRow = StringUtil.trimAllSpace(expectArray[1].replaceFirst("TableRow:", ""));
                String tableColumn = StringUtil.trimAllSpace(expectArray[2].replaceFirst("TableColumn:", ""));
                String sql = StringUtil.trim(expectArray[3].replaceFirst("SQL:", ""));
                if(StringUtil.isEmpty(dbConnect) || StringUtil.isEmpty(tableRow) || StringUtil.isEmpty(tableColumn) || StringUtil.isEmpty(sql)) {
                    throw new MException("预期结果格式错误，存在空值");
                }
                DataBaseHelper dataBaseHelper = getDataBaseHelper(dbConnect);
                List<List<String>> sqlData = dataBaseHelper.executeQueryReturnList(sql);
                List<Integer> tableRowList = getTableIndexes(tableRow);
                List<Integer> tableColumnList = getTableIndexes(tableColumn);

                if(sqlData==null || sqlData.size()==0 || tableRowList==null || tableRowList.size()==0
                        || tableColumnList==null || tableColumnList.size()==0) {
                    throw new MException("数据库查询结果 或 TableRow 或 TableColumn 为空");
                }

                //处理attribute  title[i,2];text
                Map<String, String> attributeMap = new HashMap<String, String>();
                String attributeTemp = step.get(ExcelType.PARAM);
                if(!StringUtil.isEmpty(attributeTemp)) {
                    String[] attributeArray = attributeTemp.split(";");
                    for(String attr:attributeArray) {
                        if(attr.contains("[")&&attr.contains("]")) {
                            attributeMap.put(StringUtil.trimAllSpace(attr.substring(attr.indexOf("[")+1, attr.indexOf("]"))), attr.substring(0, attr.indexOf("[")));
                        }else {
                            attributeMap.put("i,j", attr);
                        }
                    }
                }

                //以设定的TableRow和TableColumn为准进行比较
                boolean returnFlag = true;
                String xpath = step.get(ExcelType.XPATH);
                for(int i=0; i<tableRowList.size(); i++) {
                    for(int j=0; j<tableColumnList.size(); j++) {
                        String expectResult = sqlData.get(i).get(j);

                        //比较
                        String actXpath = xpath;
                        Integer rowIndex = tableRowList.get(i);
                        Integer columnIndex = tableColumnList.get(j);
                        actXpath = actXpath.replace("[i]", "["+rowIndex+"]");
                        actXpath = actXpath.replace("[j]", "["+columnIndex+"]");
                        String actualValue;
                        //获取attribute
                        String attribute = "";
                        if(!StringUtil.isEmpty(attributeMap.get(rowIndex+","+columnIndex))) {
                            attribute = attributeMap.get(rowIndex+","+columnIndex);
                        }else if(!StringUtil.isEmpty(attributeMap.get("i,"+columnIndex))) {
                            attribute = attributeMap.get("i,"+columnIndex);
                        }else if(!StringUtil.isEmpty(attributeMap.get(rowIndex+",j"))) {
                            attribute = attributeMap.get(rowIndex+",j");
                        }else if(!StringUtil.isEmpty(attributeMap.get("i,j"))) {
                            attribute = attributeMap.get("i,j");
                        }
                        //获取element
                        WebElement webElement = Driver.getElement(actXpath);
                        if (StringUtil.isEmpty(attribute)) {
                            actualValue = Driver.getValueByTag(webElement);
                        } else {
                            actualValue = Driver.getAttributeValue(webElement, attribute);
                        }
                        Parameters.ACTUAL_VALUE = actualValue;
                        boolean validatedResult = Driver.checkPoint(expectResult, actualValue, step.get(ExcelType.OBJECT),webElement);
                        returnFlag = validatedResult&&returnFlag;
                        if(!validatedResult) {
                            ErrorInfo errorInfo = new ErrorInfo(xpath);
                            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
                        }
                    }
                }

                if(returnFlag) {
                    throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
                }
            }else {
                //预期结果格式错误
                throw new MException("预期结果格式错误。预期结果："+expect);
            }
        }catch (MException e) {
            throw e;
        }catch (Exception e) {
            throw new MException(e);
        }
    }

    public DataBaseHelper getDataBaseHelper(String dbConnect) throws MException{
        try {
            Properties properties = com.extend.tool.parameter.Parameters.getProperties();
            String dbUrl = properties.getProperty(dbConnect + ".url");
            String userName = properties.getProperty(dbConnect + ".user");
            String password = properties.getProperty(dbConnect + ".pass");
            DataBaseHelper dataBaseHelper = new DataBaseHelper(dbUrl, userName, password);
            if(dataBaseHelper!=null) {
                return dataBaseHelper;
            }else {
                throw new DBException("连接数据库失败");
            }
        }catch (DBException e) {
            throw e;
        }catch (Exception e) {
            throw new MException(e);
        }
    }

    //1~5,7~13,16
    public List<Integer> getTableIndexes(String tableIndexStr) throws MException{
        try {
            List<Integer> tableIndexList = new ArrayList<Integer>();
            String tableIndexGroupArray[] = tableIndexStr.split(",");
            for(String tableIndexGroup:tableIndexGroupArray) {
                if(tableIndexGroup.contains("~")) {
                    String[] rangeArray = tableIndexGroup.split("~");
                    Integer startNum = StringUtil.strToInt(rangeArray[0]);
                    Integer endNum = StringUtil.strToInt(rangeArray[1]);
                    if(startNum==null || endNum==null || startNum>endNum) {
                        throw new MException("TableRow格式错误:"+tableIndexGroup);
                    }
                    for(int i=startNum; i<=endNum; i++) {
                        tableIndexList.add(i);
                    }
                }else {
                    Integer num = StringUtil.strToInt(tableIndexGroup);
                    int lastIndex = tableIndexList.size()-1;
                    if(num==null || (lastIndex>0 && num<=tableIndexList.get(lastIndex))) {
                        throw new MException("TableRow格式错误:"+tableIndexGroup);
                    }
                    tableIndexList.add(num);
                }
            }
            return tableIndexList;
        } catch (MException e) {
            throw e;
        } catch (Exception e) {
            throw new MException(e);
        }
    }

    public void sqlVerifyCharData(Map<String,String> step) throws MException{
        try {
            String expect = step.get(ExcelType.EXPECT);
            if(RegExp.matchRegExp(expect, "^(DBConnect:([\\s\\S]*);([\\s\\S]*)SQL:([\\s\\S]*);([\\s\\S]*)JS:([\\s\\S]*))$")) {
                String[] expectArray = expect.split(";");
                String dbConnect = StringUtil.trimAllSpace(expectArray[0].replaceFirst("DBConnect:", ""));
                String sql = StringUtil.trim(expectArray[1].replaceFirst("SQL:", ""));
                String tempExpect = expect;
                tempExpect = tempExpect.replace(expectArray[0], "");
                tempExpect = tempExpect.replace(expectArray[1], "");
                String js = StringUtil.trim(tempExpect.replaceFirst("JS:", ""));
                if(StringUtil.isEmpty(dbConnect) || StringUtil.isEmpty(js) || StringUtil.isEmpty(sql)) {
                    throw new MException("预期结果格式错误，存在空值");
                }
                DataBaseHelper dataBaseHelper = getDataBaseHelper(dbConnect);
                List<List<String>> sqlData = dataBaseHelper.executeQueryReturnList(sql);
                if(sqlData==null || sqlData.size()==0) {
                    throw new MException("数据库查询结果为空");
                }

                List<List> jsResultList = (List<List>)Driver.runScript(js);
                if(jsResultList==null || jsResultList.size()==0) {
                    throw new MException("JS查询结果为空");
                }

                if(sqlData.size()!=jsResultList.size() || sqlData.get(0).size()!=jsResultList.get(0).size()) {
                    throw new MException("数据库结果数目："+sqlData.size()+" JS结果数目："+jsResultList.size()
                            +"；数据库列数目："+sqlData.get(0).size()+" JS列数目："+jsResultList.get(0).size());
                }

                //验证结果
                boolean returnFlag = true;
                for(int i=0; i<sqlData.size(); i++) {
                    for(int j=0; j<sqlData.get(i).size(); j++) {
                        String actualValue = StringUtil.objectToString(jsResultList.get(i).get(j));
                        String expectResult = sqlData.get(i).get(j);
                        Parameters.ACTUAL_VALUE = actualValue;
                        boolean validatedResult = Driver.checkPoint(expectResult, actualValue, step.get(ExcelType.OBJECT), null);
                        returnFlag = validatedResult&&returnFlag;
                    }
                }

                if(returnFlag) {
                    throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
                }else {
                    throw new MException("验证结果失败");
                }
            }else {
                //预期结果格式错误
                throw new MException("预期结果格式错误。预期结果："+expect);
            }
        }catch (MException e) {
            throw e;
        }catch (Exception e) {
            throw new MException(e);
        }
    }

    public void sqlVerifyColumn(Map<String,String> step) throws MException {
        try {
            String expect = step.get(ExcelType.EXPECT);
            if(RegExp.matchRegExp(expect, "^(DBConnect:([\\s\\S]*);([\\s\\S]*)SQL:([\\s\\S]*);([\\s\\S]*)MouseOverNum:([\\s\\S]*);([\\s\\S]*)MouseOverXpath:([\\s\\S]*))$")) {
                String[] expectArray = expect.split(";");
                String dbConnect = StringUtil.trimAllSpace(expectArray[0].replaceFirst("DBConnect:", ""));
                String sql = StringUtil.trim(expectArray[1].replaceFirst("SQL:", ""));
                String mouseOverNumStr = StringUtil.trimAllSpace(expectArray[2].replaceFirst("MouseOverNum:", ""));
                String mouseOverXpath = StringUtil.trimAllSpace(expectArray[3].replaceFirst("MouseOverXpath:", ""));

                if(StringUtil.isEmpty(dbConnect) || StringUtil.isEmpty(sql)) {
                    throw new MException("预期结果格式错误，存在空值");
                }
                DataBaseHelper dataBaseHelper = getDataBaseHelper(dbConnect);
                List<List<String>> sqlData = dataBaseHelper.executeQueryReturnList(sql);
                if(sqlData==null || sqlData.size()==0) {
                    throw new MException("数据库查询结果为空");
                }
                Integer mouserOverNum = StringUtil.strToInt(mouseOverNumStr);
                int tempMouserOverNum = mouserOverNum;
                if(mouserOverNum<0) {
                    tempMouserOverNum = mouserOverNum * (-1);
                }
                if(mouserOverNum==null || tempMouserOverNum<=0) {
                    throw new MException("预期结果中MouseOverNum不正确");
                }else if(tempMouserOverNum!=sqlData.size()) {
                    throw new MException("预期结果中MouseOverNum和数据库中结果数不一致");
                }
                //list结果集
                boolean returnFlag = true;
                for(int i=0; i<tempMouserOverNum; i++) {
                    String expectResult = sqlData.get(i).get(0);
                    String subMouseOverXpath = mouseOverXpath+"["+(i+1)+"]";
                    if(mouserOverNum<0) {
                        subMouseOverXpath = mouseOverXpath+"["+(tempMouserOverNum-i)+"]";
                    }
                    //鼠标 mouseOver
                    Driver.mouseOver(subMouseOverXpath);
                    //比较
                    String actualValue;
                    String attribute = step.get(ExcelType.PARAM);
                    String xpath = step.get(ExcelType.XPATH);
                    WebElement webElement = Driver.getElement(xpath);
                    if (StringUtil.isEmpty(attribute)) {
                        actualValue = Driver.getValueByTag(webElement);
                    } else {
                        actualValue = Driver.getAttributeValue(webElement, attribute);
                    }

                    //判断柱子的高度是否大于0，如果等于0，那么跳过
                    WebElement textElement = Driver.getElement(subMouseOverXpath);
                    String height = Driver.getHtmlAttributeValue(textElement, "height");
                    if(StringUtil.biggerThan(height, "0") || StringUtil.isEmpty(height)) {
                        Parameters.ACTUAL_VALUE = actualValue;
                        boolean validatedResult = Driver.checkPoint(StringUtil.trimAllSpace(expectResult), StringUtil.trimAllSpace(actualValue), step.get(ExcelType.OBJECT), webElement);
                        returnFlag = validatedResult&&returnFlag;
                        if(!validatedResult) {
                            ErrorInfo errorInfo = new ErrorInfo(xpath);
                            throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
                        }
                    }else {
                        Print.log("第"+(i+1)+"根柱子高度为0，或者不存在height属性，预期结果为："+expectResult);
                    }
                }

                //最终结果
                if(returnFlag) {
                    throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
                }

            }else {
                //预期结果格式错误
                throw new MException("预期结果格式错误。预期结果："+expect);
            }
        }catch (MException e) {
            throw e;
        }catch (Exception e) {
            throw new MException(e);
        }

    }

    public void sqlVerifyPoints(Map<String,String> step) throws MException {
        try {
            String expect = step.get(ExcelType.EXPECT);
            if(RegExp.matchRegExp(expect, "^(DBConnect:([\\s\\S]*);([\\s\\S]*)SQL:([\\s\\S]*);([\\s\\S]*)MouseOverNum:([\\s\\S]*);([\\s\\S]*)MouseOverXpath:([\\s\\S]*))$")) {
                String[] expectArray = expect.split(";");
                String dbConnect = StringUtil.trimAllSpace(expectArray[0].replaceFirst("DBConnect:", ""));
                String sql = StringUtil.trim(expectArray[1].replaceFirst("SQL:", ""));
                String mouseOverNumStr = StringUtil.trimAllSpace(expectArray[2].replaceFirst("MouseOverNum:", ""));
                String mouseOverXpath = StringUtil.trimAllSpace(expectArray[3].replaceFirst("MouseOverXpath:", ""));

                if(StringUtil.isEmpty(dbConnect) || StringUtil.isEmpty(sql)) {
                    throw new MException("预期结果格式错误，存在空值");
                }
                DataBaseHelper dataBaseHelper = getDataBaseHelper(dbConnect);
                List<List<String>> sqlData = dataBaseHelper.executeQueryReturnList(sql);
                if(sqlData==null || sqlData.size()==0) {
                    throw new MException("数据库查询结果为空");
                }
                Integer mouserOverNum = StringUtil.strToInt(mouseOverNumStr);
                int tempMouserOverNum = mouserOverNum;
                if(mouserOverNum<0) {
                    tempMouserOverNum = mouserOverNum * (-1);
                }
                if(mouserOverNum==null || tempMouserOverNum<=0) {
                    throw new MException("预期结果中MouseOverNum不正确");
                }else if(tempMouserOverNum!=sqlData.size()) {
                    throw new MException("预期结果中MouseOverNum和数据库中结果数不一致");
                }
                //list结果集
                boolean returnFlag = true;
                for(int i=0; i<tempMouserOverNum; i++) {
                    String expectResult = sqlData.get(i).get(0);
                    String subMouseOverXpath = mouseOverXpath+"["+(i+1)+"]";
                    if(mouserOverNum<0) {
                        subMouseOverXpath = mouseOverXpath+"["+(tempMouserOverNum-i)+"]";
                    }
                    //鼠标 mouseOver
                    Driver.mouseOver(subMouseOverXpath);
                    //比较
                    String actualValue;
                    String attribute = step.get(ExcelType.PARAM);
                    String xpath = step.get(ExcelType.XPATH);
                    WebElement webElement = Driver.getElement(xpath);
                    if (StringUtil.isEmpty(attribute)) {
                        actualValue = Driver.getValueByTag(webElement);
                    } else {
                        actualValue = Driver.getAttributeValue(webElement, attribute);
                    }

                    Parameters.ACTUAL_VALUE = actualValue;
                    boolean validatedResult = Driver.checkPoint(StringUtil.trimAllSpace(expectResult), StringUtil.trimAllSpace(actualValue), step.get(ExcelType.OBJECT), webElement);
                    returnFlag = validatedResult&&returnFlag;
                    if(!validatedResult) {
                        ErrorInfo errorInfo = new ErrorInfo(xpath);
                        throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
                    }
                }

                //最终结果
                if(returnFlag) {
                    throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
                }

            }else {
                //预期结果格式错误
                throw new MException("预期结果格式错误。预期结果："+expect);
            }
        }catch (MException e) {
            throw e;
        }catch (Exception e) {
            throw new MException(e);
        }

    }

    public void sqlFuzzyMatchingPoints(Map<String,String> step) throws MException {
        try {
            String expect = step.get(ExcelType.EXPECT);
            if(RegExp.matchRegExp(expect, "^(DBConnect:([\\s\\S]*);([\\s\\S]*)SQL:([\\s\\S]*);([\\s\\S]*)MouseOverNum:([\\s\\S]*);([\\s\\S]*)MouseOverXpath:([\\s\\S]*))$")) {
                String[] expectArray = expect.split(";");
                String dbConnect = StringUtil.trimAllSpace(expectArray[0].replaceFirst("DBConnect:", ""));
                String sql = StringUtil.trim(expectArray[1].replaceFirst("SQL:", ""));
                String mouseOverNumStr = StringUtil.trimAllSpace(expectArray[2].replaceFirst("MouseOverNum:", ""));
                String mouseOverXpath = StringUtil.trimAllSpace(expectArray[3].replaceFirst("MouseOverXpath:", ""));

                if(StringUtil.isEmpty(dbConnect) || StringUtil.isEmpty(sql)) {
                    throw new MException("预期结果格式错误，存在空值");
                }
                DataBaseHelper dataBaseHelper = getDataBaseHelper(dbConnect);
                List<List<String>> sqlData = dataBaseHelper.executeQueryReturnList(sql);
                if(sqlData==null || sqlData.size()==0) {
                    throw new MException("数据库查询结果为空");
                }
                Integer mouserOverNum = StringUtil.strToInt(mouseOverNumStr);
                int tempMouserOverNum = mouserOverNum;
                if(mouserOverNum<0) {
                    tempMouserOverNum = mouserOverNum * (-1);
                }
                if(mouserOverNum==null || tempMouserOverNum<=0) {
                    throw new MException("预期结果中MouseOverNum不正确");
                }else if(tempMouserOverNum!=sqlData.size()) {
                    throw new MException("预期结果中MouseOverNum和数据库中结果数不一致");
                }
                //list结果集
                boolean returnFlag = true;
                for(int i=0; i<tempMouserOverNum; i++) {
                    String expectResult = sqlData.get(i).get(0);
                    String subMouseOverXpath = mouseOverXpath+"["+(i+1)+"]";
                    if(mouserOverNum<0) {
                        subMouseOverXpath = mouseOverXpath+"["+(tempMouserOverNum-i)+"]";
                    }

                    //鼠标 mouseOver
                    Driver.mouseOver(subMouseOverXpath);
                    //比较
                    String actualValue;
                    String attribute = step.get(ExcelType.PARAM);
                    String xpath = step.get(ExcelType.XPATH);
                    WebElement webElement = Driver.getElement(xpath);
                    if (StringUtil.isEmpty(attribute)) {
                        actualValue = Driver.getValueByTag(webElement);
                    } else {
                        actualValue = Driver.getAttributeValue(webElement, attribute);
                    }

                    Parameters.ACTUAL_VALUE = actualValue;
                    boolean validatedResult = Driver.checkPointMatch(StringUtil.trimAllSpace(expectResult), StringUtil.trimAllSpace(actualValue), step.get(ExcelType.OBJECT), webElement);
                    returnFlag = validatedResult&&returnFlag;
                    if(!validatedResult) {
                        ErrorInfo errorInfo = new ErrorInfo(xpath);
                        throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
                    }
                }

                //最终结果
                if(returnFlag) {
                    throw new RunException(ExceptionInfo.get(RunException.VALIDATE_PASS));
                }

            }else {
                //预期结果格式错误
                throw new MException("预期结果格式错误。预期结果："+expect);
            }
        }catch (MException e) {
            throw e;
        }catch (Exception e) {
            throw new MException(e);
        }

    }

    /**
     * JS操作元素
     * @param step excel中所有参数
     */
    public void jsOperate(Map<String, String> step) throws RunException {
        Driver.runScript(step.get(ExcelType.PARAM));
        Driver.printLog("操作动作：JS操作，已完成");
    }

    /**
     * 验证控件的内容是否为当前时间
     * @param step excel中所有参数
     */
    public void verifyCurrentTime(Map<String, String> step) throws MException {
        try {
            //日;0;yyyy-MM-dd
            String[] params = step.get(ExcelType.EXPECT).split(";");
            String formatStr = step.get(ExcelType.EXPECT).replaceFirst(params[0],"").replaceFirst(params[1],"").replaceFirst(";","").replaceFirst(";","");
            String expectValue = getStringDate(params[0], StringUtil.strToInt(params[1]), formatStr, null);

            //比较
            String actualValue;
            String attribute = step.get(ExcelType.PARAM);
            String xpath = step.get(ExcelType.XPATH);
            WebElement webElement = Driver.getElement(xpath);
            if (StringUtil.isEmpty(attribute)) {
                actualValue = Driver.getValueByTag(webElement);
            } else {
                actualValue = Driver.getAttributeValue(webElement, attribute);
            }

            Parameters.ACTUAL_VALUE = actualValue;
            boolean validatedResult = Driver.checkPoint(StringUtil.trimAllSpace(expectValue), StringUtil.trimAllSpace(actualValue), step.get(ExcelType.OBJECT), webElement);
            if(!validatedResult) {
                ErrorInfo errorInfo = new ErrorInfo(xpath);
                throw new RunException(ExceptionInfo.get(RunException.VALIDATE_FAIL), errorInfo);
            }
        } catch (MException e) {
            throw e;
        } catch (Exception e) {
            throw new MException(e);
        }
    }

    //获取时间单位对应的时间字串
    public String getStringDate(String dateUnit, int countNum, String formatStr, Calendar calendar) throws MException{
        try {
            Calendar newCalendar = calendar;
            if(newCalendar==null) {
                newCalendar = Calendar.getInstance();
                newCalendar.setTime(new Time(System.currentTimeMillis()));
            }
            if(StringUtil.isEmpty(formatStr)) {
                Driver.printLog("格式化字串不正确:"+formatStr);
                return null;
            }
            SimpleDateFormat format = null;
            String actualDate = null;
            boolean replaceYear = false;
            if("日".equals(dateUnit)) {
                newCalendar.add(Calendar.DATE, countNum);
                format = new SimpleDateFormat(formatStr);
            }else if("周".equals(dateUnit)) {
                newCalendar.add(Calendar.DATE, countNum*7);
                //周一到周四 正常格式化
                //如果是周，那么天要减一天
                if(1==newCalendar.get(Calendar.DAY_OF_WEEK)) {
                    newCalendar.add(Calendar.DATE, -1);
                }
                //按周六算
                if(newCalendar.get(Calendar.DAY_OF_WEEK)>1) {
                    newCalendar.add(Calendar.DATE, 7-newCalendar.get(Calendar.DAY_OF_WEEK));
                }

                //判断当前年的1月1号是否在周四前
                //如果在周四前，那么按照正常的格式进行格式化
                //如果不在周四前，那么需要减一个周
                Calendar firstDate = Calendar.getInstance();
                firstDate.set(newCalendar.get(Calendar.YEAR), 0, 1);
                if(firstDate.get(Calendar.DAY_OF_WEEK)>5) {
                    //当前年减一周
                    if(1==newCalendar.get(Calendar.WEEK_OF_YEAR)) {
                        newCalendar.set(Calendar.WEEK_OF_YEAR, newCalendar.getWeeksInWeekYear());
                        replaceYear = true;
                    }else if(2==newCalendar.get(Calendar.WEEK_OF_YEAR)) {
                        newCalendar.add(Calendar.WEEK_OF_YEAR, -1);
                    }else {
                        newCalendar.add(Calendar.WEEK_OF_YEAR, -1);
                    }
                }else {
                    //周一到周四 正常格式化
                    //如果是周，那么天要减一天
                    if(1==newCalendar.get(Calendar.DAY_OF_WEEK)) {
                        newCalendar.add(Calendar.DATE, -1);
                    }
                }
                format = new SimpleDateFormat(formatStr);
            }else if("月".equals(dateUnit)) {
                newCalendar.add(Calendar.MONTH, countNum);
                format = new SimpleDateFormat(formatStr);
            }else if("年".equals(dateUnit)) {
                newCalendar.add(Calendar.YEAR, countNum);
                format = new SimpleDateFormat(formatStr);
            }else {
                Driver.printLog("默认时间单位不正确:"+dateUnit);
                return null;
            }
            actualDate = format.format(newCalendar.getTime());
            if(replaceYear) {
                actualDate = actualDate.replace(StringUtil.objectToString(newCalendar.get(Calendar.YEAR)), StringUtil.objectToString(newCalendar.get(Calendar.YEAR)-1));
            }

            return actualDate;
        } catch (Exception e) {
            throw new MException(e);
        }
    }
}


