package com.cn.chen.distributed.order.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
/**
 * @author Administrator
 * @version 1.0
 **/
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled = true,jsr250Enabled = true)
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {
    public static final String RESOURCE_ID = "res1";
    @Autowired
    TokenStore tokenStore;
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID)//资源 id
                .tokenStore(tokenStore)//使用jwt令牌时,TokenConfig配置类中把TokenStore改为JwtTokenStorejwt存储
//                .tokenServices(tokenService())//验证令牌的服务：不使用jwt令牌,每次请求都远程调用授权服务器http://localhost:53020/uua/oauth/check_token验证,影响性能,
//                所以使用jwt令牌本地存储token,资源服务器本身就可以校验token了,这样提高系统性能。
                .stateless(true);
    }
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//                测试和开发环境使用swagger时打开注释开启,并注释下面一行
//                .antMatchers("/consumer/swagger-ui.html").permitAll()
//                测试该资源服务时打开注释开启,并进入网关资源配置类中配置修改配置
//                .antMatchers("/**").access("#oauth2.hasScope('ROLE_ADMIN')")
//                .antMatchers("/**").access("#oauth2.hasScope('all')")//拥有all权限的用户可以访问所有资源
                .antMatchers("/**").permitAll()//允许所有请求可以访问该资源
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//使用token,session就不用记录了
    }
    //资源服务令牌解析服务:使用jwt令牌存储token时,这里注释不使用,用JwtTokenStorejwt代替
   /* @Bean
    public ResourceServerTokenServices tokenService() {
        //使用远程服务请求授权服务器校验token,必须指定校验token 的url、client_id，client_secret
        RemoteTokenServices service = new RemoteTokenServices();
        service.setCheckTokenEndpointUrl("http://localhost:53020/uua/oauth/check_token");//token验证端点(地址,该地址带上token可解析出用户信息),校验授权服务uua中生成的token,与授权服务器中相关配置一致
        service.setClientId("c1");//客户端id
        service.setClientSecret("123");//客户端密码
        return service;
    }*/
}
