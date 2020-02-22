package com.cn.chen.distributed.feign;
import com.cn.chen.distributed.config.FeignRequestConfiguration;
import com.cn.chen.distributed.domain.User;
import com.cn.chen.distributed.domain.returnDto.ResponseResult;
import com.cn.chen.distributed.dto.params.IconParam;
import com.cn.chen.distributed.dto.params.PasswordParam;
import com.cn.chen.distributed.dto.params.ProfileParam;
import com.cn.chen.distributed.feign.fallback.MyFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 个人信息管理
 * feign请求拦截:通过FeignRequestConfiguration配置类中FeignRequestIntercepto认证拦截器在请求头中加入access_token准入令牌才能认证通过实现访问资源
 * <p>
 * Description:
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-07-31 00:47:14
 */
//服务熔断fallbackFactory和fallback选其一
@FeignClient(value = "provider-service", path = "my/user",configuration = FeignRequestConfiguration.class,fallbackFactory = MyFallBack.class/*,fallback = ProfileFeignFallback.class*/)
public interface ProfileFeign {
    /**
     * 获取个人信息
     * @param username {@code String} 用户名
     * @return {@code String} JSON
     */
    @GetMapping(value = "info/{username}")
    ResponseResult<User> info(@PathVariable String username);
    /**
     * 更新个人信息
     * @param profileParam {@link ProfileParam}
     * @return {@code String} JSON
     */
    @PostMapping(value = "update")
    ResponseResult<Integer> update(@RequestBody ProfileParam profileParam);
    /**
     * 修改密码
     * @param passwordParam {@link PasswordParam}
     * @return {@code String} JSON
     */
    @PostMapping(value = "modify/password")
    ResponseResult<User> modifyPassword(@RequestBody PasswordParam passwordParam);
    /**
     * 修改头像
     * @param iconParam {@link IconParam}
     * @return {@code String} JSON
     */
    @PostMapping(value = "modify/icon")
    ResponseResult<Integer> modifyIcon(@RequestBody IconParam iconParam);
}
