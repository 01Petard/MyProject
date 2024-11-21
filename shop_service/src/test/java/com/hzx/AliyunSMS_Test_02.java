package com.hzx;


import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import darabonba.core.client.ClientOverrideConfiguration;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class AliyunSMS_Test_02 {

    @Test
    public void send() throws Exception {

        final String ALIBABA_CLOUD_ACCESS_KEY_ID = "LTAI5tSKq7KqkbZLuMNv71yP";
        final String ALIBABA_CLOUD_ACCESS_KEY_SECRET = "hYt8m0ru7cTNE6GZ8zFF38rCjddFRI";


        String message = "Hello, World!";

        // HttpClient Configuration
        /*HttpClient httpClient = new ApacheAsyncHttpClientBuilder()
                .connectionTimeout(Duration.ofSeconds(10)) // Set the connection timeout time, the default is 10 seconds
                .responseTimeout(Duration.ofSeconds(10)) // Set the response timeout time, the default is 20 seconds
                .maxConnections(128) // Set the connection pool size
                .maxIdleTimeOut(Duration.ofSeconds(50)) // Set the connection pool timeout, the default is 30 seconds
                // Configure the proxy
                .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("<your-proxy-hostname>", 9001))
                        .setCredentials("<your-proxy-username>", "<your-proxy-password>"))
                // If it is an https connection, you need to configure the certificate, or ignore the certificate(.ignoreSSL(true))
                .x509TrustManagers(new X509TrustManager[]{})
                .keyManagers(new KeyManager[]{})
                .ignoreSSL(false)
                .build();*/

        // Configure Credentials authentication information, including ak, secret, token
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                // Please ensure that the environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and ALIBABA_CLOUD_ACCESS_KEY_SECRET are set.
                .accessKeyId(ALIBABA_CLOUD_ACCESS_KEY_ID)
                .accessKeySecret(ALIBABA_CLOUD_ACCESS_KEY_SECRET)
                //.securityToken(System.getenv("ALIBABA_CLOUD_SECURITY_TOKEN")) // use STS token
                .build());

        // Configure the Client
        AsyncClient client = AsyncClient.builder()
                .region("cn-hangzhou") // Region ID
                //.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
                .credentialsProvider(provider)
                //.serviceConfiguration(Configuration.create()) // Service-level configuration
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
                                .setEndpointOverride("dysmsapi.aliyuncs.com")
                                .setConnectTimeout(Duration.ofSeconds(30))
                )
                .build();

        // Parameter settings for API request
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName("早安花火酱")
                .templateCode("SMS_465885179")
                .phoneNumbers("13248684099")
                .templateParam("{\"code\":\"114514\"}")
                // Request-level configuration rewrite, can set Http request parameters, etc.
                // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                .build();

        // Asynchronously get the return value of the API request
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        System.out.println("短信请求结果: " + response.isDone());

        // Synchronously get the return value of the API request
        SendSmsResponseBody body = response.get().getBody();
        System.out.println("短信发送RequestId: " + body.getRequestId());
        System.out.println("短信发送状态码: " + body.getCode());
        System.out.println("短信发送结果: " + body.getMessage());

        boolean isSend;
        if (body.getCode().contains("isv.BUSINESS_LIMIT_CONTROL")) {
            isSend = false;
        } else {
            isSend = true;
        }

        System.out.println(isSend);
//        System.out.println(new Gson().toJson(resp));

        // Asynchronous processing of return values
//        response.thenAccept(resp -> {
//            System.out.println(new Gson().toJson(resp));
//        }).exceptionally(throwable -> { // Handling exceptions
//            System.out.println(throwable.getMessage());
//            return null;
//        });

        // Finally, close the client
        client.close();

    }



}
