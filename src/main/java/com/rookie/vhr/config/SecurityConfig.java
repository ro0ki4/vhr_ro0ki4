package com.rookie.vhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rookie.vhr.model.Hr;
import com.rookie.vhr.model.RespBean;
import com.rookie.vhr.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ro0ki4
 * @data 2020/9/1 21:04
 * version 1.0
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    CustomFilterInvocationSecurityMetadataSource customFilterInvocationSecurityMetadataSource;

    @Autowired
    CustomUrlDecisionManager customUrlDecisionManager;

    @Autowired
    HrService hrService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login");
        super.configure(web);
    }


    /**
     * 配置密码编码bean
     * @return
     */
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



    /**
     * 配置springsecurity 的权限控制方法
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 验证账号密码是否正确，调用持久层接口
         */
        auth.userDetailsService(hrService);
//        auth.inMemoryAuthentication()
//                .withUser("admin")
//                .password(passwordEncoder().encode("123"))
//                .roles("user");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 配置登陆接口、提交登陆的接口、参数，必须通过登陆之后才可以访问其他页面
         */
        http.authorizeRequests()
//                .anyRequest().authenticated()
                //下面这种方式是通过定义一个过滤器来实现权限控制，也可以通过多个antMatcher方法叠加来实现同样的效果
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFilterInvocationSecurityMetadataSource);
                        return object;
                    }
                })


//                .and()
//                .antMatcher("/admin/**").authorizeRequests().anyRequest().hasRole("admin")
//                .antMatchers("/user/**").hasRole("user")
//
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/doLogin")
                .loginPage("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    /**
                     * 登陆成功之后做的处理
                     *
                     * @param req
                     * @param resp
                     * @param authentication
                     * @throws IOException
                     * @throws ServletException
                     */
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        Hr hr = (Hr)authentication.getPrincipal();
                        /**
                         * 希望前端返回的数据不包含密码口令，可以通过 hr.setPassword("")实现或者禁止序列化@JsonIgnore
                         */
                        hr.setPassword(null);
                        RespBean ok = RespBean.ok("登陆成功", hr);
                        String s = new ObjectMapper().writeValueAsString(ok);
                        out.write(s);
                        out.flush();
                        out.close();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    /**
                     * 登陆失败做的处理
                     *
                     * @param req
                     * @param resp
                     * @param e
                     * @throws IOException
                     * @throws ServletException
                     */
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        RespBean respBean = RespBean.error("登陆失败");
                        if(e instanceof LockedException){
                            respBean.setMsg("账户被锁定，请联系管理员");
                        }else if(e instanceof CredentialsExpiredException){
                            respBean.setMsg("密码过期，请联系管理员");
                        }else if(e instanceof AccountExpiredException){
                            respBean.setMsg("账户过期，请联系管理员");
                        } else if (e instanceof DisabledException) {
                            respBean.setMsg("账户被禁用，请联系管理员");
                        } else if (e instanceof BadCredentialsException) {
                            respBean.setMsg("用户名或密码错误");
                        }
                        out.write(new ObjectMapper().writeValueAsString(respBean));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    /**
                     * 登出成功之后做的处理
                     *
                     * @param req
                     * @param resp
                     * @param authentication
                     * @throws IOException
                     * @throws ServletException
                     */
                    @Override
                    public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                        resp.setContentType("application/json;charset=utf-8");
                        PrintWriter out = resp.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()
                .and()
                .csrf().disable();
    }
}
