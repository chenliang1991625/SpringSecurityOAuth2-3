package com.cn.chen.distributed.order.controller;
import com.alibaba.fastjson.JSON;
import com.cn.chen.distributed.domain.User;
import com.cn.chen.distributed.domain.returnDto.LoginInfo;
import com.cn.chen.distributed.domain.returnDto.LoginParam;
import com.cn.chen.distributed.domain.returnDto.OrderStatus;
import com.cn.chen.distributed.domain.returnDto.ResponseResult;
import com.cn.chen.distributed.domain.returnDto.UserDTO;
import com.cn.chen.distributed.exceptionHander.OrderException;
import com.cn.chen.distributed.feign.ProfileFeign;
import com.cn.chen.distributed.service.SpringDataUserDetailsService;
import com.cn.chen.distributed.utils.MapperUtils;
import com.cn.chen.distributed.utils.OkHttpClientUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
//@CrossOrigin(origins = "*",maxAge =3600)//允许跨域请求访问:注释掉,因为网关统一处理跨域
@RestController
@Api(value = "consumer-LoginController|一个用来测试swagger注解的控制器")//swagger在类上注解
public class LoginController {
//    登录test
    @PostMapping(value = "/login-success", produces = {"text/plain;charset=UTF-8"})
//如果用swagger动态生成文档API,如果使用RequestMapping要指定请求方法，不然swagger会出现所有请求方法(该方法get/post/delete/put..请求测试都会出现)的test API
    @ApiOperation(value = "测试登录成功与否的方法", notes = "test: 简单测试")//swagger在方法上注解,RequestMapping
//    @ApiImplicitParam(paramType="query", name = "userNumber", value = "用户编号", required = true, dataType = "Integer")//swagger对方法参数注解
    public String loginSuccess() {
        UserDTO userDTO = JSON.parseObject(getUsername(),UserDTO.class);
        //提示具体用户名称登录成功
        return userDTO.getUsername() +" 登录成功";
    }
// 测试资源1
    @GetMapping(value = "/r/r1", produces = {"text/plain;charset=UTF-8"})
    @PreAuthorize("hasAuthority('p1')")//拥有p1权限才可以访问
    @ApiOperation(value = "访问/r/r1", notes = "测试有没有访问资源1的权限")
    public String r1() {
        return getUsername() + " 访问资源1";
    }
// 测试资源2
    @GetMapping(value = "/r/r2", produces = {"text/plain;charset=UTF-8"})
    @PreAuthorize("hasAuthority('p2')")//拥有p2权限才可以访问
    @ApiOperation(value = "访问/r/r2", notes = "测试有没有访问资源2的权限")
    public String r2() {
        return getUsername() + " 访问资源2";
    }
    //获取当前用户信息的方法:获取用户名
    private String getUsername() {
        String username = null;
        //当前认证通过的用户身份
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //用户身份
        Object principal = authentication.getPrincipal();
        if (principal == null) {
            username = "匿名";
        }
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            username = userDetails.getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }
    @Value("${security.oauth2.client.access-token-uri}")
    private String URL_OAUTH_TOKEN;
    @Value("${security.oauth2.client.grant_type}")
    public String oauth2GrantType;
    @Value("${security.oauth2.client.client-id}")
    public String oauth2ClientId;
    @Value("${security.oauth2.client.client-secret}")
    public String oauth2ClientSecret;
//    SpringDataUserDetailsService在distribute-commons公共类中,该启动类上在扫描到
    @Autowired
    public SpringDataUserDetailsService springDataUserDetailsService;
    @Autowired
   public TokenStore tokenStore;
    @Autowired
    public PasswordEncoder passwordEncoder;
    @Autowired
    private ProfileFeign profileFeign;
 /*   @Reference(version = "1.0.0")
    private UserService userService;
    @Reference(version = "1.0.0")
    private MessageService messageService;*/
    /**
     * 用OkHttp(就像postman一样)获取到access_token准入令牌,加入到请求头中才能实现登录
     * 登录
     * @param loginParam 登录参数
     * @return {@link ResponseResult}
     */
//    @RequestMapping(value = "/user/login")
    @PostMapping(value = "/user/login")//把登录用户名,密码和access_token传给前端校验和认证
//    @RequestBody加上不能通过form-data 表单提交,可以通过json请求，或者多加@ModelAttribute注解
    public ResponseResult<Map<String, Object>> login(@RequestBody /*@ModelAttribute*/ LoginParam loginParam, HttpServletRequest request) throws Exception {
        // 封装返回的结果集
        Map<String, Object> result = Maps.newHashMap();
//      获取登录用户信息添加入map返回给前端
        UserDetails userDetails = springDataUserDetailsService.loadUserByUsername(loginParam.getUsername());
        result.put("username",loginParam.getUsername());
        result.put("password",loginParam.getPassword());
        // 验证密码是否正确
        if (userDetails == null || !passwordEncoder.matches(loginParam.getPassword(),userDetails.getPassword())) {
            throw new OrderException(OrderStatus.ADMIN_PASSWORD);
        }
        // 通过HTTP客户端请求登录接口
        Map<String, String> params = Maps.newHashMap();
        params.put("username", loginParam.getUsername());
        params.put("password", loginParam.getPassword());
        params.put("grant_type", oauth2GrantType);
        params.put("client_id", oauth2ClientId);
        params.put("client_secret", oauth2ClientSecret);
        try {
            // 解析响应结果封装并返回
            Response response = OkHttpClientUtil.getInstance().postData(URL_OAUTH_TOKEN,params);
            String jsonString = Objects.requireNonNull(response.body()).string();
            Map<String, Object> jsonMap = MapperUtils.json2map(jsonString);
            String token = String.valueOf(jsonMap.get("access_token"));
//            把access_token放入map前端接受
//            result.put("access_token",token);
            result.put("token",token);
//            result.put("json_token",token);
//            RequestTemplate requestTemplate=new RequestTemplate();
//            放入request域中,陈亮加的
//           request.setAttribute("access_token",token);
//           requestTemplate.header("Authorization","Bearer " +token);
//            request.setAttribute("json_token",token);
            // 发送登录日志
//            sendAdminLoginLog(userDetails.getUsername(), request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("result="+result.toString());
        return new ResponseResult<Map<String, Object>>(ResponseResult.CodeStatus.OK,"登录成功",result);
    }
    /**
     * 获取用户信息
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('user')")//拥有user用户权限才可以访问
    @GetMapping(value = "/user/info")//在前端请求头中加入access_token
    public ResponseResult<LoginInfo> info() throws Exception {
//        获取用户信息对象
        UserDTO userDTO = JSON.parseObject(getUsername(),UserDTO.class);
        // 获取个人信息
        ResponseResult<User> responseResult = profileFeign.info(userDTO.getUsername()/*user1.getUsername()*//*"zhangsan"*//*authentication.getName()*/);
        System.out.println("/user/info>>>"+userDTO.getUsername());
        User user = responseResult.getData();
        // 封装并返回结果
        LoginInfo loginInfo = new LoginInfo();
        if (user!=null){
            loginInfo.setName(user.getUsername());
            loginInfo.setAvatar(user.getIcon());
            loginInfo.setNickname(user.getNickname());
            System.out.println("consumer-loginInfo="+loginInfo.toString());
            return new ResponseResult<LoginInfo>(ResponseResult.CodeStatus.OK,"获取用户信息",loginInfo);
        }else
        // 如果触发熔断则返回熔断结果
//            return  new ResponseResult<LoginInfo>(ResponseResult.CodeStatus.OK,"获取用户信息",MapperUtils.json2pojo(MapperUtils.obj2json(user),ResponseResult.class));
//            return  MapperUtils.json2pojo(MapperUtils.obj2json(loginInfo),ResponseResult.class);
        return null;
    }
    /**
     * 注销
     * @return {@link ResponseResult}
     */
    @PreAuthorize("hasAuthority('user')")
    @PostMapping(value = "/user/logout")//在前端请求头中加入access_token
    public ResponseResult<Void> logout(HttpServletRequest request) {
        // 获取 token
        String token = request.getParameter("access_token");
        System.out.println("consumer-logout-token="+token);//打印测试
        // 删除 token 以注销
        OAuth2AccessToken oAuth2AccessToken =null;
        if (token!=null){
            oAuth2AccessToken = tokenStore.readAccessToken(token);
        }
        if (oAuth2AccessToken!=null){
            tokenStore.removeAccessToken(oAuth2AccessToken);
//            String token1 = request.getParameter("access_token");
            return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "用户已注销!");
        }
        return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "该用户没有登录或已经注销!");
    }
    /**
     * 发送登录日志
     * @param request {@link HttpServletRequest}
     */
/*    private void sendAdminLoginLog(String username, HttpServletRequest request) {
        User user = userService.get(username);
        if (user != null) {
            // 获取请求的用户代理信息
            Browser browser = UserAgentUtils.getBrowser(request);
            String ip = UserAgentUtils.getIpAddr(request);
            String address = UserAgentUtils.getIpInfo(ip).getCity();
            UserLoginLogDTO dto = new UserLoginLogDTO();
            dto.setAdminId(user.getId());
            dto.setCreateTime(new Date());
            dto.setIp(ip);
            dto.setAddress(address);
            dto.setUserAgent(browser.getName());
            messageService.sendAdminLoginLog(dto);
        }
    }*/
}
