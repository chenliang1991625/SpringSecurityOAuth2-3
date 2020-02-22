package com.cn.chen.distributed.gateway.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
/**
 * @author Administrator
 * @version 1.0
 * 授权服务配置
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
//授权服务权限配置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        security
                .tokenKeyAccess("permitAll()")                    //oauth/token_key是公开:获取token的站点是公开的
                .checkTokenAccess("permitAll()")                  //oauth/check_token公开:校验token的站点是公开的
                .allowFormAuthenticationForClients();			//表单认证方式(申请令牌）
    }
/*授权endpoint：http://localhost:53010/uaa/oauth/authorize?response_type=code&client_id=c1
令牌endpoint: http://localhost:53010/uaa/oauth/token
 post请求加上5个参数
 */
}
