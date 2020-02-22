package com.cn.chen.distributed.order.controller;
import com.alibaba.fastjson.JSON;
import com.cn.chen.distributed.domain.returnDto.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * consumer订单controller测试
 * @author Administrator
 * @version 1.0
 **/
@CrossOrigin//允许跨域访问
@RestController
public class ControllerTest {
    @GetMapping(value = "/r1")
    @PreAuthorize("hasAuthority('p1')")//单个权限:拥有p1权限方可访问此url
//    @PreAuthorize("hasAnyAuthority('p1','p2')")//多个权限
    public String r1() {
        //获取用户身份信息
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = JSON.parseObject((String) principal,UserDTO.class);
        return userDTO.getUsername().toString() + "访问资源1";
//       return "访问资源1";
    }
    @GetMapping(value = "/r2")
    @PreAuthorize("hasAuthority('p2')")
    public String r2() {//通过Spring Security API获取当前登录用户
        UserDTO user = JSON.parseObject((String)SecurityContextHolder.getContext().getAuthentication().getPrincipal(),UserDTO.class);
        return user.getUsername() + "访问资源2";
    }
}
