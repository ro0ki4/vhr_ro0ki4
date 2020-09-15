package com.rookie.vhr.service;

import com.rookie.vhr.mapper.HrMapper;
import com.rookie.vhr.model.Hr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author ro0ki4
 * @data 2020/9/1 17:38
 * version 1.0
 */
@Service
public class HrService implements UserDetailsService {

    @Autowired
    HrMapper hrMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername 方法启动");
        Hr hr = hrMapper.loadUserByUsername(s);
        System.out.println(hr);
        if(hr == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        hr.setRoles(hrMapper.getHrRolesById(hr.getId()));
        return hr;
    }
}
