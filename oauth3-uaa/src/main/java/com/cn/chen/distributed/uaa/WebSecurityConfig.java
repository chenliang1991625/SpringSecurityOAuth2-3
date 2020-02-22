package com.cn.chen.distributed.uaa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * @author Administrator
 * @version 1.0
 **/
//本授权服务认证服务器配置:经过该授权服务器认证通过才能进入其他资源服务器(其它资源可再进行二次认证)访问已授权路径资源
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //认证管理器
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    //密码编码器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //安全拦截机制:拦截认证(最重要）
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/r1").hasAnyAuthority("p1")//特别指定:有p1权限用户可访问"/r1"路径中的资源
//                .antMatchers("/user/info","/user/logout").hasAnyAuthority("user")
                .antMatchers("/login*").permitAll()//登录页面公开:允许访问"/login*进行登录认证 ,"/**/swagger-ui.html"
//                .antMatchers("/login*","/**/user/login","/**/user/info/**","/**/user/logout/**").permitAll()
                .anyRequest().authenticated()//其它访问需要先认证 ,"/**/user/login"
                .and()
                .formLogin();
    }
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers("/**/user/login","/**/user/info/**");//允许/**/user/login进入登录
//    }
}
