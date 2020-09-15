package com.rookie.vhr.controller;

import com.rookie.vhr.model.Hr;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ro0ki4
 * @data 2020/9/1 21:08
 * version 1.0
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        System.out.println("hello方法启动");
        return "hello";
    }

    @PostMapping("/doLogin")
    public String doLogin(@RequestBody()Hr hr){
        System.out.println("doLogin方法启动"+hr);

        return "success";
    }

    @GetMapping("/employee/basic/hello")
    public String hello2(){
        return "employee/basic";
    }

    @GetMapping("/employee/advanced/hello")
    public String hello3(){
        return "employee/advanced";
    }

}
