package com.limn.tool.common;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by limengnan on 2018/2/27.
 */
public class HttpClientCommon {

    public static String httpGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        return execute(httpGet);
    }

    public static String postFile(MultipartEntityBuilder meb , String url){
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(meb.build());
        return execute(httppost);
    }

    private static String execute(HttpRequestBase http){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;

        CloseableHttpResponse res;
        try {
            res = httpClient.execute(http);
            HttpEntity entity = res.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();//释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return httpStr;


    }





}
