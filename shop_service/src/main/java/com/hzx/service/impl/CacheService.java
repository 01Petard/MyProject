package com.hzx.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试Caffeine
 */
@Service
@Slf4j
public class CacheService {

    @Cacheable("config")
    public Map<String, String> getConfig() throws InterruptedException {
        log.info("进入配置查询方法内");
        Thread.sleep(1000);
        Map<String, String> map = new HashMap<>();
        map.put("username", "root");
        map.put("password", "123456");
        map.put("dbUrl", "jdbc:mysql://localhost:13306/HzxSpringBoot");
        return map;
    }

    @Cacheable("queryUser")
    public List getUser(String id) throws InterruptedException {
        log.info("进入数据查询方法内，id为" + id);
        // 模拟数据查询的逻辑
        Thread.sleep(1000);
        ArrayList list = new ArrayList<>();
        list.add("1");
        list.add("2");
        return list;
    }



}
