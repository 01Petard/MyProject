package com.hzx.common.utils;


import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Data
@AllArgsConstructor
@Slf4j
public class AliSmsUtil {

    private String accessKeyId;
    private String accessKeySecret;
    private String region;
    private String endpoint;
    private String signName;
    private String templateCode;

    public Boolean sendSms(String phone, String message) {
        try {
            return sendCode(phone, message);
        } catch (Exception e) {
            log.error("发送短信失败", e);
            return false;
        } finally {
            log.info("（后台消息）发送验证码：" + phone + ": " + message);
        }
    }

    public String generateCode() {
        // 生成6位验证码
        return String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
    }

    /**
     * 发送验证码工具类
     * @param phone 用户手机号
     * @return Map<String, String>
     */
    public Boolean sendCode(String phone, String message) {

        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());

        AsyncClient client = AsyncClient.builder()
                .region(region)
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(endpoint)
                )
                .build();

        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(signName)
                .templateCode(templateCode)
                .phoneNumbers(phone)
                .templateParam("{\"code\":\"" + message + "\"}") // 使用随机生成的验证码
                .build();
        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        try {
            SendSmsResponse resp = response.get();
            String json = new Gson().toJson(resp);
            return parseJson(json);
        } catch (Exception e) {
            log.error("发送短信发生异常", e);
            return false;
        } finally {
            client.close();
        }
//        Map<String, String> codeMap = new HashMap<>();
//        codeMap.put(sendSmsRequest.getPhoneNumbers(), sendSmsRequest.getTemplateParam());
    }

    /**
     * 解析返回的 JSON 数据
     * @param json 阿里云短信服务的响应Json
     * @return 服务结果是否OK，OK返回true，失败返回false
     * @throws Exception
     */
    public Boolean parseJson(String json) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        // 获取 headers 节点
        JsonNode headersNode = rootNode.path("headers");

        // 获取 Keep-Alive 节点
        String keepAlive = headersNode.path("Keep-Alive").asText();
        String timeout = keepAlive.split("=")[1];
        log.info("连接时间: {}", timeout);

        // 获取 Connection 节点
        String connection = headersNode.path("Connection").asText();
        log.info("连接状态: {}", connection);

        // 获取 Date 节点
        String date = headersNode.path("Date").asText();
        log.info("连接日期: {}", date);

        // 获取 body 节点
        JsonNode bodyNode = rootNode.path("body");

        // 获取 code 和 message 节点
        String code = bodyNode.path("code").asText();
        switch (code) {
            case "OK":
                log.info("阿里云短信发送成功！");
                break;
            case "isv.BUSINESS_LIMIT_CONTROL":
                log.error("短信发送过于频繁");
                break;
            case "isv.TEMPLATE_PARAMS_ILLEGAL":
                log.error("模板参数错误");
                break;
            case "isv.SMS_TEST_NUMBER_LIMIT":
                log.error("只能向已回复授权信息的手机号发送");
                break;
            default:
                log.error("未知错误：{}", code);
                break;
        }
        log.info("服务状态: {}", code);

        // 信息结果
        String message = bodyNode.path("message").asText();
        log.info("服务信息: {}", message);

        return code.equals("OK") ? true : false;
    }
}
