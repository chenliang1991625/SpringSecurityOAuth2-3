package com.cn.chen.distributed.service;
import com.alibaba.fastjson.JSON;
import com.cn.chen.distributed.dao.UserDao;
import com.cn.chen.distributed.domain.returnDto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author Administrator
 * @version 1.0
 **/
//用户详情服务
@Service
public class SpringDataUserDetailsService implements UserDetailsService {
    @Autowired
    UserDao userDao;
    //根据账号查询用户信息
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //将来连接数据库根据账号查询用户信息
        UserDTO userDTO = userDao.getUserByUsername(username);
//        模拟数据测试
       /* userDTO.setUsername("admin");
        userDTO.setPassword("123456");*/
        if(userDTO == null){
            //如果用户查不到，返回null，由provider来抛出异常
            return null;
        }
        //根据用户的id查询用户的权限
        List<String> permissions = userDao.findPermissionsByUserId(userDTO.getId());
        //将permissions转成数组
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        //将userDTO转成json
        String principal = JSON.toJSONString(userDTO);
        UserDetails userDetails = User.withUsername(principal/*username*/).password(userDTO.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }
}
