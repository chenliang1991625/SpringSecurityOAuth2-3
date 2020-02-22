package com.cn.chen.distributed.order.filter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.chen.distributed.utils.EncryptUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/*
 * @author Administrator
 * @version 1.0
*/
//spring-security Autho2:解析出头中的json-token,用户信息和用户权限信息
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//解析出头中的json-token;与oauth3-gateway>>AuthFilter>> ctx.addZuulRequestHeader("json-token", EncryptUtil.encodeUTF8StringBase64(JSON.toJSONString(jsonToken)));该行代码存入的名称一致
        String token = httpServletRequest.getHeader("json-token");
        //解析出头中的token,如果测试不通换成json-token测试
//        String token = httpServletRequest.getHeader("access_token");
//        String token = httpServletRequest.getHeader("Authorization");//这样写不可以
        if(token!=null){
            String json = EncryptUtil.decodeUTF8StringBase64(token);
//            String json = EncryptUtil.decodeUTF8StringBase64(JSON.toJSONString(token));
            //将token转成json对象
            JSONObject jsonObject = JSON.parseObject(json);
            //用户身份信息
//            UserDTO userDTO = new UserDTO();
//            String principal = jsonObject.getString("principal");
//            userDTO.setUsername(principal);
//            UserDTO userDTO = JSON.parseObject(jsonObject.getString("principal"), UserDTO.class);
            String principal = jsonObject.getString("principal");
            //用户权限
            JSONArray authoritiesArray = jsonObject.getJSONArray("authorities");
            String[] authorities = authoritiesArray.toArray(new String[authoritiesArray.size()]);
            //将用户信息和权限填充 到用户身份token对象中
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(principal,null, AuthorityUtils.createAuthorityList(authorities));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            //将authenticationToken填充到安全上下文
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
