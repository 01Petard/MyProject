package com.hzx.controller.v2;

import com.hzx.common.result.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class PingController {

    /**
     * 用于前端询问服务是否开启
     * @return
     */
    @GetMapping("/ping")
    public Result ping() {
        return Result.success("HzxShopApplication服务端运行中...");
    }


}
