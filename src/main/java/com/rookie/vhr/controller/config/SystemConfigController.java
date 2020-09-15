package com.rookie.vhr.controller.config;

import com.rookie.vhr.model.Menu;
import com.rookie.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ro0ki4
 * @data 2020/9/13 21:33
 * version 1.0
 */
@RestController
@RequestMapping("/system/config")
public class SystemConfigController {

    @Autowired
    MenuService menuService;

    @GetMapping("/menu")
    public List<Menu> getMenusById(){
        return menuService.getMenusById();
    }
}
