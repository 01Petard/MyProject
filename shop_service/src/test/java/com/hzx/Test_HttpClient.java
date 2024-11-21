package com.hzx;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Test_HttpClient {
    /**
     * 测试通过httpclient发送GET方式请求
     */
    @Test
    public void testGET() throws Exception {
        //创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建请求对象
        HttpGet httpGet = new HttpGet("http://localhost:8008/ping");
        //将"your_token_here"替换为实际token
//        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjJ9.FDx0E_-u8rKTCNaW6QG33Idmahee_m0vlqu3SXYxw_4";
//        httpGet.addHeader("authentication", token);
        //发送请求，并接收相应结果
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //获取服务端返回的状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("返回的响应码：" + statusCode);
        HttpEntity entity = response.getEntity();
        //获得服务端的和响应体
        String body = EntityUtils.toString(entity);
        System.out.println("返回的数据：" + body);
        //关闭资源
        httpClient.close();
        response.close();
    }


    /**
     * 测试通过httpclient发送POST方式请求
     */
    @Test
    public void testPOST() throws Exception {
        //创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建请求对象
        HttpPost httpPost = new HttpPost("http://localhost:8008/user/login");
        //创建请求体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "123");
        jsonObject.put("password", "123456");
        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        //指定请求的编码方式
        stringEntity.setContentEncoding("utf-8");
        //指定请求的数据格式
        stringEntity.setContentType("application/json");
        //设置请求参数
        httpPost.setEntity(stringEntity);
        //发送请求，并接收相应结果
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //获取服务端返回的状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("返回的响应码：" + statusCode);
        //获得服务端的和响应体
        HttpEntity entity = response.getEntity();
        String body = EntityUtils.toString(entity);
        System.out.println("返回的数据：" + body);
        //关闭资源
        httpClient.close();
        response.close();
    }
}
