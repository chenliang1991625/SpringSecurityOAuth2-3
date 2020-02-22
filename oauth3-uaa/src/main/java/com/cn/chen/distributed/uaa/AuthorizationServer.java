package com.cn.chen.distributed.uaa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import javax.sql.DataSource;
import java.util.Arrays;
/**
 * @author Administrator
 * @version 1.0
 * 授权服务配置
 **/
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userService;
    //将客户端信息存储到数据库
    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }
    //客户端详情服务
    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        clients.withClientDetails(clientDetailsService);
//        不用内存测试时下面注释
        /*clients.inMemory()// 使用in-memory存储
                .withClient("c1")// client_id
                .secret(new BCryptPasswordEncoder().encode("123"))//客户端密钥
                .resourceIds("res1")//资源id/资源标识,可以使数组:.resourceIds("res1","res2")
                .authorizedGrantTypes("authorization_code","password","client_credentials","implicit","refresh_token")// 该client允许的授权类型authorization_code,password,refresh_token,implicit,client_credentials
                .scopes("all")// 允许的授权范围
                .autoApprove(false)//false跳转到授权页面
                //加上验证回调地址
                .redirectUris("https://www.baidu.com");*/
    }
    //令牌管理服务
    @Bean
    public AuthorizationServerTokenServices tokenService() {
   DefaultTokenServices service=new DefaultTokenServices();
        service.setClientDetailsService(clientDetailsService);//客户端详情服务
        service.setSupportRefreshToken(true);//支持刷新令牌
        service.setTokenStore(tokenStore);//令牌存储策略
        //令牌增强:不用内存测试时下面三行注释打开使用
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        service.setAccessTokenValiditySeconds(7200); // 令牌默认有效期2小时
        service.setRefreshTokenValiditySeconds(259200); // 刷新令牌默认有效期3天
        return service;
    }
    //设置授权码模式的授权码如何存取，暂时采用内存方式
  /*  @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }*/
//设置授权码模式的授权码如何存取:Jdbc数据库存储
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);//设置授权码模式的授权码如何存取
    }
//授权服务端点配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)//认证管理器:密码模式需要
                .authorizationCodeServices(authorizationCodeServices)//授权码服务:授权码模式需要
                .tokenServices(tokenService())//令牌管理服务:任何磨砂黑都需要
                .allowedTokenEndpointRequestMethods(HttpMethod.POST,HttpMethod.GET);//授权请求方法类型
//                .allowedTokenEndpointRequestMethods(HttpMethod.GET);
    }
/*  @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints){
        endpoints.authenticationManager(authenticationManager)
                .userDetailsService(userService);
    }*/
//授权服务权限配置
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security){
        security
                .tokenKeyAccess("permitAll()")                    //oauth/token_key是公开:获取token的站点是公开的
                .checkTokenAccess("permitAll()")                  //oauth/check_token公开:校验token的站点是公开的
                // 允许已认证的客户端访问,比如:/oauth/check_token检查token
//                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();				//表单认证方式(申请令牌）
    }
//    授权endpoint：http://localhost:53020/uaa/oauth/authorize?response_type=code&client_id=c1
//    授权码模式获取授权码: localhost:53020/uaa/oauth/authorize?client_id=c1&response_type=code&scope=ROLE_ADMIN&redirect_URI=https://www.baidu.com
    /*1.令牌endpoint,获取token令牌: http://localhost:53020/uaa/oauth/token
 post请求加上6个参数:
 client_id：c1   (客户端准入标识)。
 client_secret：123  (客户端秘钥)。
 grant_type：authorization_code  (授权类型，填写authorization_code，表示授权码模式)
 code： XMJkw0 (授权码，就是刚刚获取的授权码，注意：授权码只使用一次就无效了，需要重新申请)。
 username: zhangsan
 password: 123
 2.密码模式获取token:http://localhost:53010/uaa/oauth/token?client_id=c1&client_secret=123&grant_type=password&username=zhangsan&password=123
    * */
}
