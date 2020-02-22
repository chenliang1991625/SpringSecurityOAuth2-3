package com.cn.chen.distributed.feign.fallback;
import com.cn.chen.distributed.domain.User;
import com.cn.chen.distributed.domain.returnDto.ResponseResult;
import com.cn.chen.distributed.dto.params.IconParam;
import com.cn.chen.distributed.dto.params.PasswordParam;
import com.cn.chen.distributed.dto.params.ProfileParam;
import com.cn.chen.distributed.feign.ProfileFeign;
import org.springframework.stereotype.Component;
/**
 * 个人信息服务熔断器
 * <p>
 * Description:
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-08-27 23:50:08
 */
@Component
public class ProfileFeignFallback implements ProfileFeign {
    public static final String BREAKING_MESSAGE = "请求失败了,请检查您的网络";
    @Override
    public ResponseResult<User> info(String username) {
        try {
            return new ResponseResult<User>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ResponseResult<Integer> update(ProfileParam profileParam) {
        try {
            return new ResponseResult<>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ResponseResult<User> modifyPassword(PasswordParam passwordParam) {
        try {
            return new ResponseResult<User>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public ResponseResult<Integer> modifyIcon(IconParam iconParam) {
        try {
            return new ResponseResult<>(ResponseResult.CodeStatus.BREAKING, BREAKING_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
