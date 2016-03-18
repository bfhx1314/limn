package com.extend.tool.httpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.automation.exception.MException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**

 * http连接、抓取管理类

 * @author syblike

 * @createtime Oct 18, 2012 1:55:18 PM

 * 

 */

public class HttpClientUtil {
    
    //连接池里的最大连接数
    public static final int MAX_TOTAL_CONNECTIONS = 100;
    //每个路由的默认最大连接数
    public static final int MAX_ROUTE_CONNECTIONS = 50;
    //连接超时时间
    public static final int CONNECT_TIMEOUT = 30000;
    //套接字超时时�?10分钟
    public static final int SOCKET_TIMEOUT = 60000;
    //连接池中 连接请求执行被阻塞的超时时间
    public static final long CONN_MANAGER_TIMEOUT = 60000;

    public ResponseInfo executeGet(String url) {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
        HttpClient httpClient = null;
        HttpGet httpGet = new HttpGet(url);
        httpClient = (HttpClient)getOneHttpClient();
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpGet);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
            if(200 == statusCode) {
                String allHtml = readHtmlContentFromEntity(httpResponse.getEntity());
                responseInfo.setContent(allHtml);
            }else{
                responseInfo.setContent(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MException(e.getMessage());
        }finally{
            if(httpGet!=null){
                httpGet.releaseConnection();
                httpGet.abort();
            }
            if(httpClient!=null){
                httpClient.getConnectionManager().shutdown();
            }
            return responseInfo;
        }
    }

    public ResponseInfo executePost(String url) {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
        HttpClient httpClient = null;
        HttpPost httpPost = new HttpPost(url);
        httpClient = (HttpClient)getOneHttpClient();
        HttpResponse httpResponse=null;
        try {
            httpResponse=httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
            if(200 == statusCode) {
                String allHtml = readHtmlContentFromEntity(httpResponse.getEntity());
                responseInfo.setContent(allHtml);
            }else{
                responseInfo.setContent(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new MException(e.getMessage());
        }finally{
            if(httpPost!=null){
                httpPost.releaseConnection();
                httpPost.abort();
            }
            if(httpClient!=null){
                httpClient.getConnectionManager().shutdown();
            }
            return responseInfo;
        }
    }

    public ResponseInfo executePost(String url, String jSon) {
        ResponseInfo responseInfo = new ResponseInfo();
        long startTime = System.currentTimeMillis();
        HttpClient httpClient = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpClient = (HttpClient)getOneHttpClient();
        HttpResponse httpResponse=null;
        try {
            StringEntity se = new StringEntity(jSon, HTTP.UTF_8);
            httpPost.setEntity(se);
            httpResponse=httpClient.execute(httpPost);
            long endTime = System.currentTimeMillis();
            long executeTime = (endTime-startTime);
            responseInfo.setTime(executeTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            responseInfo.setStatus(statusCode);
            if(200 == statusCode) {
                String allHtml = readHtmlContentFromEntity(httpResponse.getEntity());
                responseInfo.setContent(allHtml);
            }else{
                responseInfo.setContent(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            responseInfo.setContent(message);
            throw new MException(message);
        }finally{
            if(httpPost!=null){
                httpPost.releaseConnection();
                httpPost.abort();
            }
            if(httpClient!=null){
                httpClient.getConnectionManager().shutdown();
            }
            return responseInfo;
        }
    }

    private synchronized HttpClient getOneHttpClient(){
    	 HttpParams parentParams = new BasicHttpParams(); 
    	 parentParams.setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
         parentParams.setParameter(ClientPNames.CONN_MANAGER_TIMEOUT, CONN_MANAGER_TIMEOUT);

         parentParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);

         parentParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
         parentParams.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
         parentParams.setParameter(ClientPNames.HANDLE_REDIRECTS, true);
         //设置头信�?模拟浏览�?

         List<BasicHeader> collection = new ArrayList<BasicHeader>();
         collection.add(new BasicHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0)"));
         collection.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));
         collection.add(new BasicHeader("Accept-Language", "zh-cn,zh,en-US,en;q=0.5"));
         collection.add(new BasicHeader("Accept-Charset", "ISO-8859-1,utf-8,gbk,gb2312;q=0.7,*;q=0.7"));
         collection.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
         parentParams.setParameter(ClientPNames.DEFAULT_HEADERS, collection);
    	 DefaultHttpClient httpClient = new DefaultHttpClient(parentParams);
         return httpClient;
    }

    /**

     * 从response返回的实体中读取页面代码

     * @param httpEntity Http实体

     * @return 页面代码

     * @throws org.apache.http.ParseException

     * @throws java.io.IOException
     */
    private String readHtmlContentFromEntity(HttpEntity httpEntity) throws ParseException, IOException {
        if(httpEntity==null){
            return null;
        }
        String html = "";

        Header header = httpEntity.getContentEncoding();

        if(httpEntity.getContentLength() < 2147483647L){            //EntityUtils无法处理ContentLength超过2147483647L的Entity
            if(header != null && "gzip".equals(header.getValue())){
                html = EntityUtils.toString(new GzipDecompressingEntity(httpEntity),HTTP.UTF_8);
            } else {
                html = EntityUtils.toString(httpEntity, HTTP.UTF_8);
            }

        } else {
            InputStream in = httpEntity.getContent();
            if(header != null && "gzip".equals(header.getValue())){
                html = unZip(in, ContentType.getOrDefault(httpEntity).getCharset().toString());
            } else {
                html = readInStreamToString(in, ContentType.getOrDefault(httpEntity).getCharset().toString());
            }
            if(in != null){
                in.close();
            }

        }
        return html;
    }
    /**

     * 解压服务器返回的gzip浿

     * @param in 抓取返回的InputStream浿

     * @param charSet 页面内容编码

     * @return 页面内容的String格式

     * @throws java.io.IOException

     */

    private String unZip(InputStream in, String charSet) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        GZIPInputStream gis = null;

        try {

            gis = new GZIPInputStream(in);

            byte[] _byte = new byte[1024];

            int len = 0;

            while ((len = gis.read(_byte)) != -1) {

                baos.write(_byte, 0, len);

            }

            String unzipString = new String(baos.toByteArray(), charSet);

            return unzipString;

        } finally {

            if (gis != null) {

                gis.close();

            }

            if(baos != null){

                baos.close();

            }

        }

    }
    /**

     * 读取InputStream�?

     * @param in InputStream�?

     * @return 从流中读取的String

     * @throws java.io.IOException

     */

    private String readInStreamToString(InputStream in, String charSet) throws IOException {

        StringBuilder str = new StringBuilder();

        String line;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, charSet));

        while((line = bufferedReader.readLine()) != null){

            str.append(line);

            str.append("\n");

        }

        if(bufferedReader != null) {

            bufferedReader.close();

        }

        return str.toString();

    }

}