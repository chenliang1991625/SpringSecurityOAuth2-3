package com.cn.chen.distributed.interceptor;
import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Enumeration;
import org.springframework.http.HttpHeaders;
/**
 * Feign请求拦截器1
 * <p>
 * Description: 用于设置请求头,自动传递 access_Token
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-07-31 00:17:20
 * @see com.cn.chen.distributed.interceptor*/
//@Component
@Configuration
@EnableFeignClients(basePackages = "com.cn.chen.distributed")
public class FeignRequestInterceptor implements RequestInterceptor  {
    @Override
    public void apply(RequestTemplate requestTemplate) {
  /*      ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //添加token
        requestTemplate.header("Authorization", request.getHeader("Authorization"));*/
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;//attributes总是为null,why?
        HttpServletRequest request=null;
        if (attributes != null){
            request = attributes.getRequest();
        }
        if (request!=null){
            // 设置请求头
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String value = request.getHeader(name);
                    requestTemplate.header(name, value);
                }
            }
            // 设置请求体，这里主要是为了传递 access_token
            Enumeration<String> parameterNames = request.getParameterNames();
            StringBuilder body = new StringBuilder();
            if (parameterNames != null) {
                while (parameterNames.hasMoreElements()) {
                    String name = parameterNames.nextElement();
                    String value = request.getParameter(name);
                    //将登录时生成的access_token加入请求头
                    if ("access_token".equals(name)) {
//                    Authorization首字母到底是大写还是小写 postman中是大写
//                        requestTemplate.header("Authorization","Bearer " + value);
                        requestTemplate.header(/*"authorization"*/HttpHeaders.AUTHORIZATION,"Bearer " + value);
                    }
                    // 其它参数加入请求体
                    else {
                        body.append(name).append("=").append(value).append("&");
                    }        }
                // 设置请求体
            }
            if (body.length() > 0) {
                // 去掉最后一位 & 符号
                body.deleteCharAt(body.length() - 1);
                requestTemplate.body(Request.Body.bodyTemplate(body.toString(), Charset.defaultCharset()));
            }
        }
    }
}
