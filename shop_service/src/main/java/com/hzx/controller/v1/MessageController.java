package com.hzx.controller.v1;


import com.hzx.common.result.Result;
import com.hzx.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;


    @GetMapping("/send/{phone}")
    public Result sendRegisterMessage(@PathVariable String phone) {
        messageService.sendRegisterMessage(phone);
        return Result.success();
    }



}
