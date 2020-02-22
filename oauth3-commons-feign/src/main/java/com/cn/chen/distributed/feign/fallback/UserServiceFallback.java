/*
package com.cn.chen.distributed.feign.fallback;
import com.cn.chen.distributed.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
*/
/**
 * 用户服务提供者熔断器
 * <p>
 * Description:
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-08-31 01:59:55
 *//*
public class UserServiceFallback {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceFallback.class);
    */
/**
     * 熔断方法
     * @param username {@code String} 用户名
     * @param ex       {@code Throwable} 异常信息
     * @return {@link User} 熔断后的固定结果
     *//*
    public static User getByUsernameFallback(String username, Throwable ex) {
        logger.warn("Invoke getByUsernameFallback: " + ex.getClass().getTypeName());
        ex.printStackTrace();
        return null;
    }
}
*/
