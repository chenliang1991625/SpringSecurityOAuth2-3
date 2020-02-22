package com.cn.chen.distributed.gateway.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
/**
 * @author Administrator
 * @version 1.0
 **/
//网关资源服务配置:即网关进入其他模块的权限配置
@Configuration
public class ResouceServerConfig  {

    public static final String RESOURCE_ID = "res1";

    //uaa资源服务配置
    @Configuration
    @EnableResourceServer
    public class UAAServerConfig extends ResourceServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources){
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                 .antMatchers("/uaa/**").permitAll();//网关可以请求资源distributed-security-uaa(/uaa是distributed-security-uaa模块的context-path请求路径前缀)
        }
    }
    //consumer资源
    //uaa资源服务配置
    @Configuration
    @EnableResourceServer
    public class OrderServerConfig extends ResourceServerConfigurerAdapter {
        @Autowired
        private TokenStore tokenStore;

        @Override
        public void configure(ResourceServerSecurityConfigurer resources){
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID)
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
//OrderServerConfig指定了若请求匹配/consumer/**，也就是访问统一用户服务，接入客户端需要有scope中包含read，并且authorities(权限)中需要包含ROLE_API。
//                  测试该consumer资源服务时打开注释开启,并在该资源服务的资源配置中修改配置
                    .antMatchers("/consumer/**").access("#oauth2.hasScope('ROLE_ADMIN')");
//                  测试和开发环境使用swagger时打开注释开启,并注释下上面一行
//                    .antMatchers("**/swagger-ui.html","/consumer/**").permitAll();//网关可以请求swagger API测试页面和/consumer/**路径的distributed-security-consumer资源
        }
    }
    //配置其它的资源服务..
}
