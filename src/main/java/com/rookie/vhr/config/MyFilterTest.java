package com.rookie.vhr.config;

import com.rookie.vhr.model.Menu;
import com.rookie.vhr.model.Role;
import com.rookie.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * @author ro0ki4
 * @data 2020/9/15 0:03
 * version 1.0
 */
public class MyFilterTest implements FilterInvocationSecurityMetadataSource {
    @Autowired
    MenuService menuService;
    @Autowired
    AntPathMatcher antPathMatcher;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //获取当前请求的url地址，用来判断是否权限足够访问该url
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        //数据库中获取到菜单和角色的信息，用来权限控制
        List<Menu> menus = menuService.getAllMenusWithRole();
        for (Menu menu : menus) {
            if(antPathMatcher.match(menu.getUrl(),requestUrl)){
                //如果当前请求的url符合当前的菜单对应的url
                List<Role> roles = menu.getRoles();
                //我们是需要字符串的
                String[] tar = new String[roles.size()];
                for(int i = 0; i < roles.size(); i++){
                    tar[i] = roles.get(i).getName();
                }
                //返回访问该url需要的角色
                return SecurityConfig.createList(tar);
            }
        }
        //这里我们做一个简化的判断，如果该路径不属于菜单路径，那么我们要求只需要登陆就可以放行
        return SecurityConfig.createList("ROLE_LOGIN");
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
