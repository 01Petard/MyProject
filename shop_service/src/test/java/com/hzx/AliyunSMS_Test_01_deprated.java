package com.hzx;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.jetbrains.annotations.NotNull;


public class AliyunSMS_Test_01_deprated {


    public static void main(String[] args) {

        final String ALIBABA_CLOUD_ACCESS_KEY_ID = "LTAI5tSKq7KqkbZLuMNv71yP";
        final String ALIBABA_CLOUD_ACCESS_KEY_SECRET = "hYt8m0ru7cTNE6GZ8zFF38rCjddFRI";

        // Please ensure that the environment variables ALIBABA_CLOUD_ACCESS_KEY_ID and ALIBABA_CLOUD_ACCESS_KEY_SECRET are set.
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",
                ALIBABA_CLOUD_ACCESS_KEY_ID,
                ALIBABA_CLOUD_ACCESS_KEY_SECRET
        );


        /** use STS Token
         DefaultProfile profile = DefaultProfile.getProfile(
         "<your-region-id>",           // The region ID
         System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID"),       // The AccessKey ID of the RAM account
         System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET"),   // The AccessKey Secret of the RAM account
         System.getenv("ALIBABA_CLOUD_SECURITY_TOKEN"));     // STS Token
         **/

        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = getCommonRequest();
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    @NotNull
    private static CommonRequest getCommonRequest() {

        CommonRequest request = new CommonRequest();

        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("SignName", "早安花火酱");
        request.putQueryParameter("TemplateCode", "SMS_465885179");
        request.putQueryParameter("PhoneNumbers", "13248684099");
        request.putQueryParameter("TemplateParam", "{\"code\":\"114514\"}");
        return request;
    }
}
