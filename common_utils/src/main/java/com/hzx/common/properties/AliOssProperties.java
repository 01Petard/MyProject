package com.hzx.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hzx.alioss")
@Data
public class AliOssProperties {

    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;

}