package com.hzx.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hzx.alisms")
@Data
public class AliSmsProperties {

    private String accessKeyId;
    private String accessKeySecret;
    private String region;
    private String endpoint;
    private String signName;
    private String templateCode;

}
