package com.cn.chen.distributed.gateway.filter;
import com.alibaba.fastjson.JSON;
import com.cn.chen.distributed.gateway.common.EncryptUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
/**
 * @author Administrator
 * @version 1.0
 **/
//过滤器从请求的安全上下文获取用户信息和权限,并把身份信息和权限信息放在json中,加入http的header中,转发给微服务,相关微服务可以获取使用
@Component
public class AuthFilter extends ZuulFilter {
    @Override
    public boolean shouldFilter() {
        return true;
    }
    @Override
    public String filterType() {
        return "pre";
    }
    @Override
    public int filterOrder() {
        return 0;
    }
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        //从安全上下文中拿 到用户身份对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof OAuth2Authentication)){
            return null;
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        //取出用户身份信息
        String principal = userAuthentication.getName();

        //取出用户权限
        List<String> authorities = new ArrayList<>();
        //从userAuthentication取出权限，放在authorities
        userAuthentication.getAuthorities().stream().forEach(c->authorities.add(((GrantedAuthority) c).getAuthority()));

        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String,Object> jsonToken = new HashMap<>(requestParameters);
        if(userAuthentication!=null){
            jsonToken.put("principal",principal);
            jsonToken.put("authorities",authorities);
        }
        //与oauth3-consumer-feign模块>>TokenAuthenticationFilter>>String token = httpServletRequest.getHeader("json-token");该行代码存入取值的名称保持一致
        //把身份信息和权限信息放在json中,加入http的header中,转发给微服务
        //用json-token或不是access_token的任意名测试通过(这时toekn没加入请求头),所以不能用access_token,不然会报错(一旦token加入请求头,json转为pojo失败就报错):
        // com.alibaba.fastjson.JSONException: syntax error, expect {, actual error, pos 0, fastjson-version 1.2.62或"Unrecognized token 'User': was expecting ('true', 'false' or 'null')\n at [Source: (String)\"User(id=1, username=zhangsan, password=$2a$10$aFsOFzujtPCnUCUKcozsHux0rQ/3faAHGFSVb9Y.B1ntpmEhjRtru, mobile=12342, icon=null, nickname=null, fullname=张三, email=null, note=null, createtime=null, logintime=null, status=null)\"; line: 1, column: 5]"
        //但是加入时用json-token或不是access_token的任意名,其它服务模块获取时用access_token貌似冲突了:应该是oauth2-consumer-feign模块>>LoginController>>info()>>User user1 = JSON.parseObject(getUsername(), User.class);
        //解析出错了,"json-token"是Map类型，不能直接把json-token转为pojo
        ctx.addZuulRequestHeader("json-token",EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));
        return null;
    }
}
