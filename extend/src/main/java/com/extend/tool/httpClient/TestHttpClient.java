package com.extend.tool.httpClient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class TestHttpClient {
	
	public static void main(String[] args) {
        HttpClientUtil httpClientUtil = new HttpClientUtil();
        ResponseInfo responseInfo = httpClientUtil.executeGet("http://10.21.5.14:8080/greenwich/user_home.htmls?_search=false&nd=1442301863577&rows=10000&page=1&sidx=category+asc%2C+&sord=asc&_=1442301863579");
        System.out.print(responseInfo.status);
	}


	
	public static void test2() {
        HttpClient httpClient=null;
//        HttpPost httpPost=new HttpPost("http://10.21.5.14:8080/greenwich/user_login.htmls?username=uitest&password=10a76d67107da115f6e8b322113a65e1fc91ba61a662908c84cdef76a1b90df44913368c784988104fc0f6b0c21380d79bcb918b1ed6c244d23df188a6f1f372d0b7c4d14b38655b6c429a965aaa025b15671d730b03d0d050c4223abfdd25e3517541ce08f94c30628d2006d85e4b815e4820db65658f344242dfa8e90da838&validatecode=11");
        HttpGet httpGet = new HttpGet("http://10.21.5.14:8080/greenwich/user_home.htmls?_search=false&nd=1442301863577&rows=10000&page=1&sidx=category+asc%2C+&sord=asc&_=1442301863579");
//		HttpPost httpPost=new HttpPost("http://172.20.146.14:8080/rating/ws/rest/user/login");
//		httpClient = (HttpClient)HttpClientUtil.getOneHttpClient();
//		HttpResponse httpResponse=null;

	}

}
