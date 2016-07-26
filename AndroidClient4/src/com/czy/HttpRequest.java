package com.czy;
/*Android应用开发中，会经常要提交数据到服务器和从服务器得到数据,POST请求和Get请求*/
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpRequest
{
    /**
     *Post请求
     */
	public String doPost(String url , List<NameValuePair> nameValuePairs){  
        //新建HttpClient对象    
        HttpClient httpclient = new DefaultHttpClient();  
        //创建POST连接  
        HttpPost httppost = new HttpPost(url);  
        try {  
//          //使用PSOT方式，必须用NameValuePair数组传递参数  
//          List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();  
//          nameValuePairs.add(new BasicNameValuePair("id", "12345"));  
//          nameValuePairs.add(new BasicNameValuePair("stringdata","hps is Cool!"));  
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));    //通过setEntity方法来发送HTTP请求
            HttpResponse response = httpclient.execute(httppost);    //  通过DefaultHttpClient 的 execute方法来获取HttpResponse
            return EntityUtils.toString(response.getEntity());   //通过getEntity()从Response中获取内容
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
            return "";
        } catch (IOException e) {  
            e.printStackTrace();
            return "";
        }
    }  
      
    /** 
     *Get请求 
     */  
    public String doGet(String url){  
        HttpParams httpParams = new BasicHttpParams();    //创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
        HttpConnectionParams.setConnectionTimeout(httpParams,30000);    //置连接超时和 Socket 超时，
        HttpConnectionParams.setSoTimeout(httpParams, 30000);    
              
        HttpClient httpClient = new DefaultHttpClient(httpParams);     //创建一个 HttpClient 实例
        // GET  
        HttpGet httpGet = new HttpGet(url);      //创建 HttpGet 方法，该方法会自动处理 URL 地址的重定向
        try {  
            HttpResponse response = httpClient.execute(httpGet);  
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){  //SC_OK = 200
            	return EntityUtils.toString(response.getEntity());    //通过getEntity()从Response中获取内容
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            return "";
        }  
        return "";
    } 
}