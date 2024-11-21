package com.hzx.config;

import com.hzx.common.properties.AliSmsProperties;
import com.hzx.common.utils.AliSmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建AliSMSUtil对象
 */
@Configuration
@Slf4j
public class AliSmsConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AliSmsUtil aliSmsUtil(AliSmsProperties aliSmsProperties){
        log.info("创建阿里云短信类对象：{}", aliSmsProperties);
        return new AliSmsUtil(
                aliSmsProperties.getAccessKeyId(),
                aliSmsProperties.getAccessKeySecret(),
                aliSmsProperties.getRegion(),
                aliSmsProperties.getEndpoint(),
                aliSmsProperties.getSignName(),
                aliSmsProperties.getTemplateCode()
        );
    }





}
