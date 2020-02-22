package com.cn.chen.distributed.provider.service;
import com.cn.chen.distributed.domain.User;
import com.cn.chen.distributed.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
/**
 * 用户管理服务
 * <p>
 * Description:
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-07-26 09:41:08
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public int insert(User user) {
        // 初始化用户对象
        initUser(user);
        return userMapper.insert(user);
    }
    /**
     * 熔断器的使用
     * @param username {@code String} 用户名
     * @return {@link User}
     */
    @Override
//    @SentinelResource(value = "getByUsername", fallback = "getByUsernameFallback",blockHandlerClass = UserServiceFallback.class)
    public User get(String username) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", username);
        User user = userMapper.selectOneByExample(example);
        return user;
//      return userMapper.selectByPrimaryKey("1");
    }
    @Override
    public User get(User user) {
        return userMapper.selectOne(user);
    }
    @Override
    public int update(User user) {
        // 获取原始用户信息
        User oldAdmin = get(user.getUsername());
        // 仅更新 邮箱、昵称、备注、状态
        oldAdmin.setEmail(user.getEmail());
        oldAdmin.setNickname(user.getNickname());
        oldAdmin.setNote(user.getNote());
        oldAdmin.setStatus(user.getStatus());
        return userMapper.updateByPrimaryKey(oldAdmin);
    }
    @Override
    public int modifyPassword(String username, String password) {
        User user = get(username);
        user.setPassword(passwordEncoder.encode(password));
        return userMapper.updateByPrimaryKey(user);
    }
    @Override
    public int modifyIcon(String username, String path) {
        User user = get(username);
        user.setIcon(path);
        return userMapper.updateByPrimaryKey(user);
    }
    /**
     * 初始化用户对象
     *
     * @param user {@link User}
     */
    private void initUser(User user) {
        // 初始化创建时间
        user.setCreatetime(new Date());
        user.setLogintime(new Date());
        // 初始化状态
        if (user.getStatus() == null) {
            user.setStatus(0);
        }
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }
}
