package com.cn.chen.distributed.provider.service;
import com.cn.chen.distributed.domain.User;
/**
 * 用户管理服务
 * <p>
 * Description:
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-07-26 09:32:31
 */
public interface UserService {
    /**
     * 新增用户
     * @param user {@link User}
     * @return {@code int} 大于 0 则表示添加成功
     */
    int insert(User user);
    /**
     * 获取用户
     * @param username 用户名
     * @return {@link User}
     */
    User get(String username);
    /**
     * 获取用户
     * @param user {@link User}
     * @return {@link User}
     */
    User get(User user);
    /**
     * 更新用户
     * <p>
     * 仅允许更新 邮箱、昵称、备注、状态
     * </p>
     * @param user {@link User}
     * @return {@code int} 大于 0 则表示更新成功
     */
    int update(User user);
    /**
     * 修改密码
     * @param username {@code String} 用户名
     * @param password {@code String} 明文密码
     * @return {@code int} 大于 0 则表示更新成功
     */
    int modifyPassword(String username, String password);
    /**
     * 修改头像
     * @param username {@code String} 用户名
     * @param path     {@code String} 头像地址
     * @return {@code int} 大于 0 则表示更新成功
     */
    int modifyIcon(String username, String path);
}
