package com.rookie.vhr.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author ro0ki4
 * @data 2020/9/15 0:18
 * version 1.0
 */
public class MyDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //三个形参分别是用户所得到的授权角色，第三个形参代表访问当前url需要的角色集合
        for (ConfigAttribute configAttribute : configAttributes) {
            //通过比较角色名来判断是否我们可以访问
            String needRole = configAttribute.getAttribute();
            if("ROLE_LOGIN".equals(needRole)){
                //代表我们之前定义的filter得到的url不需要角色就可以访问，但我们应该防止这种情况发生
                if(authentication instanceof AnonymousAuthenticationToken){
                    //用户的角色为匿名
                    throw new AccessDeniedException("不允许匿名用户访问，请先登录");
                }else{
                    //代表当前的url不是我们之前有filter过滤得到的菜单的url
                    //这里返回代表用户只要具有某个非匿名的身份就可以访问该url
                    return;
                }
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                if(authority.getAuthority().equals(needRole)){
                    //发现当前url需要的角色已被当前用户所拥有
                    return;
                }
            }
        }
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
