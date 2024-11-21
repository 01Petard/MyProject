package com.hzx.controller.v2;


import com.hzx.service.impl.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("cache/")
public class CacheController {

    @Autowired
    private CacheService cacheService;




    @GetMapping("testConfig")
    public Map testConfig() throws InterruptedException {
        Map<String, String> config = cacheService.getConfig();
        return config;
    }

    @GetMapping("queryUser")
    public List testquery(@RequestParam(value = "id") String id) throws InterruptedException {
        List list = cacheService.getUser(String.valueOf(1));
        return list;
    }




}
