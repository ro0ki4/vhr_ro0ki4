package com.rookie.vhr.config;

import com.rookie.vhr.model.Menu;
import com.rookie.vhr.model.Role;
import com.rookie.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * @author ro0ki4
 * @data 2020/9/14 14:07
 * version 1.0
 * 根据前端请求的url地址，分析出请求需要的角色
 */

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    MenuService menuService;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {

        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        List<Menu> menus = menuService.getAllMenusWithRole();//寻找到对应查看某个菜单需要的角色
        for (Menu menu : menus) {
            if(antPathMatcher.match(menu.getUrl(),requestUrl)){
                List<Role> roles = menu.getRoles();//当前请求需要的角色
                String[] str = new String[roles.size()];
                for(int i = 0; i < roles.size(); i++){
                    str[i] = roles.get(i).getName();
                }
                return SecurityConfig.createList(str);

            }
        }
        return SecurityConfig.createList("ROLE_LOGIN");//表示没匹配上的登陆上就可以访问
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
