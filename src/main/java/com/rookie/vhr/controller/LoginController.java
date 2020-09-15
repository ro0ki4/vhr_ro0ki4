package com.rookie.vhr.controller;

import com.rookie.vhr.model.RespBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ro0ki4
 * @data 2020/9/2 22:01
 * version 1.0
 */
@RestController
public class LoginController {

    @CrossOrigin
    @GetMapping("/login")
    public RespBean login(){
        return RespBean.error("尚未登陆，请登录");
    }
}
