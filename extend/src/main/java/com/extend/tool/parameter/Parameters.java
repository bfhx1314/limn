package com.extend.tool.parameter;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class Parameters {

    public static Properties properties = getProperties();
    /**
     * dashboard数据库连接
     */
    public static String dashBoardDBUrl = properties.getProperty("dashBoardDBUrl","jdbc:mysql://172.20.144.26:3306/greenwichtest?useUnicode=true&characterEncoding=UTF-8");
    public static String dashBoardUserName = properties.getProperty("dashBoardUserName","root");
    public static String dashBoardPassword = properties.getProperty("dashBoardPassword","root");
    public static String isInsertDashBoard = properties.getProperty("isInsertDashBoard","false");

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
