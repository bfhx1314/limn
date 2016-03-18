package com.automation.tool.util;

import com.automation.exception.DBException;
import com.google.gson.Gson;

import java.sql.*;
import java.util.*;

/**
 * Created by snow.zhang on 2015/9/7.
 */
public class DataBaseHelper {

    public static final int MYSQL = 0;
    public static final int ORACLE = 1;

    private Connection conn = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    private String dbUrl = null;
    private String dbDriver = null;
    private String user = null;
    private String password = null;
    private int dbType = MYSQL;

    private LinkedList<Map<String, String>> sqlData = null;
    private String sqlDataToJSON = "";

    private String dbConnectInfo = "";
    public DataBaseHelper(){
//        throw new Exception("缺少数据库URL，用户名，密码。");
    }

    public DataBaseHelper(String dbUrl, String user, String password) throws DBException {
        setDbUrl(dbUrl);
        setDbDriver();
        setUser(user);
        setPassword(password);
        setDbConnectInfo();
        init();
    }

    public void init() throws DBException {
        try {
            String dbDriver = getDbDriver();
            if (null == dbDriver){
                throw new DBException("缺少数据库驱动名称。dbDriver="+dbDriver);
            }
            Class.forName(dbDriver);
            if (null == conn){
                if (null == dbUrl || null == user || null == password) {
                    throw new DBException("数据库URL或用户名或密码 为空。"+getDbConnectInfo());
                }else{
                    setConn(DriverManager.getConnection(getDbUrl(), getUser(), getPassword()));
                }
            }
        } catch (ClassNotFoundException e) {
            throw new DBException(e.getMessage());
        } catch (SQLException e) {
            throw new DBException("创建数据库连接失败："+e.getMessage());
        }
    }


    /**
     * 执行select
     * @param sql
     * @return LinkedList<HashMap<String, String>>
     * @throws DBException
     */
    public void executeQuery(String sql) throws DBException {
        LinkedList<Map<String, String>> sqlData = new LinkedList<Map<String, String>>();
        try {
            Statement sta = getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = sta.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.first();
            if (columnCount != 0 && rowCount != 0){
                Map<String, String> colData = null;
                do{
                    colData = new HashMap<String, String>();
                    for(int i=0;i<columnCount;i++){
                        colData.put(metaData.getColumnLabel(i+1), resultSet.getString(i+1));
                    }
                    sqlData.add(colData);
                } while(resultSet.next());
            }
        } catch (SQLException e) {
            throw new DBException(e.getErrorCode(),e.getMessage() + "\r\n SQL :"+sql);
        }
        setSqlData(sqlData);
        setSqlDataToJSON(new Gson().toJson(sqlData));
    }

    /**
     * 执行select
     * @param sql
     * @return LinkedList<HashMap<String, String>>
     * @throws DBException
     */
    public List<List<String>> executeQueryReturnList(String sql) throws DBException {
        List<List<String>> sqlData = new ArrayList<List<String>>();
        Statement sta = null;
        try {
            sta = getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (getDbUrl().indexOf("mysql") != -1){
                sta.setPoolable(false);
            }
            ResultSet resultSet = sta.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.first();
            if (columnCount != 0 && rowCount != 0){
                List<String> colData = null;
                do{
                    colData = new ArrayList<String>();
                    for(int i=0;i<columnCount;i++){
                        colData.add(resultSet.getString(i+1));
                    }
                    sqlData.add(colData);
                } while(resultSet.next());
            }
            return sqlData;
        } catch (SQLException e) {
            throw new DBException(e.getErrorCode(),e.getMessage() + "\r\n SQL :"+sql);
        } finally {
            try {
                if(sta != null) {
                    sta.close();
                }
                if(getConn()!=null) {
                    getConn().close();
                }
            } catch (SQLException e) {
                throw new DBException(e.getErrorCode(),e.getMessage() + "\r\n SQL :"+sql);
            }
        }

    }

    /**
     * 可执行insert update delete drop
     * 操作需谨慎
     * @param sql
     */
    public void executeUpdate(String sql) throws DBException {
        try {
            Statement sta = getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sta.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DBException(e.getErrorCode(),e.getMessage() + "\r\n SQL :"+sql);
        }
    }

    /**
     * 可执行insert update delete drop
     * 操作需谨慎
     * @param sql
     * @return 受影响行数（更新计数）
     */
    private int executeUpdateReturnRow(String sql) throws DBException {
        int rowCount = -1;
        try {
            Statement sta = getConn().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rowCount = sta.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DBException(e.getErrorCode(),e.getMessage() + "\r\n SQL :"+sql);
        }
        return rowCount;
    }

    private void setConn(Connection conn) {
        this.conn = conn;
    }
    private Connection getConn() throws DBException {
        return conn;
    }

    public Statement getStatement() {
        return statement;
    }
    private void setStatement(Statement statement) {
        this.statement = statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }
    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    private void setDbConnectInfo() {
        this.dbConnectInfo = "DBURL="+dbUrl + ",USER="+user+",PASSWORD="+password+";";
    }
    public String getDbConnectInfo() {
        return this.dbConnectInfo;
    }
    private void setPassword(String password) {
        this.password = password;
    }
    public String getPassword() {
        return password;
    }

    public String getDbUrl(){
        return this.dbUrl;
    }
    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbDriver(){
        return this.dbDriver;
    }
    private void setDbDriver() throws DBException {
        if (null == getDbDriver()){
            String dburl = getDbUrl();
            if (null == dburl) throw new DBException("dbUrl="+dburl);
            if (dburl.indexOf("mysql") != -1){
                setDbDriver("com.mysql.jdbc.Driver");
            }else if(dburl.indexOf("oracle") != -1){
                setDbDriver("oracle.jdbc.driver.OracleDriver");
            }else if(dburl.indexOf("sqlserver") != -1){
                setDbDriver("com.microsoft.jdbc.sqlserver.SQLServerDriver");
            }
        }
    }
    private void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    private void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }
    private void setDbType(int type){
        this.dbType = type;
    }

    private void setSqlData(LinkedList<Map<String, String>> sqlData) {
        this.sqlData = sqlData;
    }

    private void setSqlDataToJSON(String sqlDataToJSON) {
        this.sqlDataToJSON = sqlDataToJSON;
    }

    public String getSqlDataToJSON() {
        return sqlDataToJSON;
    }

    public LinkedList<Map<String, String>> getSqlData() {
        return sqlData;
    }
}
