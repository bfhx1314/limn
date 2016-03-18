package com.automation.tool.parameter;

import com.automation.keyword.ExcelType;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class Parameters {


    public static Properties properties = getProperties();

    public static String chromeDriverPath = properties.getProperty("ChromeDriverPath", "res/chromedriver_for_win.exe");
    public static String chromeDriverMacPath = properties.getProperty("ChromeDriverMacPath", "res/chromedriver");
    public static String BrowserName = properties.getProperty("BrowserName", "2");
    public static String savePath = properties.getProperty("savePath", "download");

    public static String userDir = System.getProperty("user.dir");

    public static String prjDir = "";
    /**
     * 运行时间，用于生成结果excel
     */
    public static String runDate = "";
    /**
     * 环境路径
     */
    public static String DFAULT_TEST_PATH = System.getProperty("user.dir");

    /**
     * 系统
     */
    public static String OS_NAME = System.getProperty("os.name");

    /**
     * tmp路径
     */
    public static String TMP = System.getProperty("java.io.tmpdir") + "\\";
    public static String DEFAULT_TEMP_PATH = System.getProperty("user.dir") + "\\temp";

    /**
     * testcase路径
     */
    public static String TESTCASE_PATH = userDir + "\\testcase";
    /**
     * 结果路径
     */
    public static String RESULT_PATH = userDir + "\\Result";
    /**
     * 项目名称
     */
    public static String PRJ_NAME = "";
    /**
     * excel名
     */
    public static String TESTCASE_NAME = "";
    public static String TESTCASE_FULLNAME = "";
    public static String RESULT_PRJ_PATH = RESULT_PATH + "\\" + Parameters.PRJ_NAME;
    public static String RESULT_PRJ_TIME_PATH = "";
    public static String RESULT_PRJ_TIME_TESTCASE_REPORT_PATH = "";
    public static String RESULT_PRJ_TIME_TESTCASE_PATH = "";
    public static boolean RESULT_EXCEL_EXIST = false;
    /**
     * 验证控件、模糊验证控件 的实际值
     */
    public static String ACTUAL_VALUE = "";
    //excel标题行的标准格式
    //是否执行	用例名称	操作步骤	操作对象	对象参数	预期结果	实际结果	是否通过	关联属性	正则描述
    public static String[] titles = new String[]{ExcelType.DOES_RUN, ExcelType.CASE_NAME, ExcelType.STEP, ExcelType.OBJECT,
            ExcelType.PARAM, ExcelType.EXPECT, ExcelType.ACTUAL, ExcelType.ISPASS, ExcelType.XPATH, ExcelType.EXPECT_DESCRIBE};

    public static String implicitlyWait = properties.getProperty("implicitlyWait","10");
    public static String pageLoadTimeout = properties.getProperty("pageLoadTimeout","35");
    public static String CLICK_WAIT_FOR_PAGE_LOAD = properties.getProperty("clickWaitForPageLoad","35");;

    /**
     * 所有动态变量,内存
     */
    public static Map<String,String> mapParameters = new HashMap<String,String>();

    public static Properties getProperties() {
        Properties prop = new Properties();
        try {
            FileInputStream file = new FileInputStream("prop.properties");
            prop.load(file);
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }
}
