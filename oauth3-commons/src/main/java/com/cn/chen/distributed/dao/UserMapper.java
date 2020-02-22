package com.cn.chen.distributed.dao;
import com.cn.chen.distributed.domain.User;
import com.cn.chen.distributed.mapper.MyMapper;
//不要被@SpringBootApplication(scanBasePackages="")扫描到
public interface UserMapper extends MyMapper<User> {
}
