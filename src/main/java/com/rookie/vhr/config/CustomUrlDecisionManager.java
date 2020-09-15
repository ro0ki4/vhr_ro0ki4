package com.rookie.vhr.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author ro0ki4
 * @data 2020/9/14 14:36
 * version 1.0
 * 判断当前用户是否具备某些角色
 */

@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //第一个参数为用户得到的授权角色，而第三个参数代表需要的角色
        for (ConfigAttribute configAttribute : configAttributes) {
            String needrole = configAttribute.getAttribute();
            if("ROLE_LOGIN".equals(needrole)){
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new AccessDeniedException("尚未登陆，请登录");
                }else{
                    return;
                }
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            for (GrantedAuthority authority : authorities) {
                if(authority.getAuthority().equals(needrole)) {
                    System.out.println("需要的角色"+needrole);
                    return;
                }
            }

        }
        throw new AccessDeniedException("权限不足，请联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
