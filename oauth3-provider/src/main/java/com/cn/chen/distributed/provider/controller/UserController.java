package com.cn.chen.distributed.provider.controller;
import com.cn.chen.distributed.domain.User;
import com.cn.chen.distributed.domain.returnDto.ResponseResult;
import com.cn.chen.distributed.dto.params.IconParam;
import com.cn.chen.distributed.dto.params.PasswordParam;
import com.cn.chen.distributed.dto.params.ProfileParam;
import com.cn.chen.distributed.provider.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 个人信息管理
 * <p>
 * Description:
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-07-30 22:34:41
 */
@RestController
@RequestMapping(value = "/my/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;
    /**
     * 获取个人信息
     * @param username {@code String} 用户名
     * @return {@link ResponseResult}
     */
    @GetMapping(value = "info/{username}")
//    @SentinelResource(value = "info", fallback = "infoFallback",blockHandlerClass/* fallbackClass*/ = ProfileControllerFallback.class)
    public ResponseResult<User> info(@PathVariable String username) {
        User user = userService.get(username);
        System.out.println("UserController>>info()>>>"+user.toString());
        return new ResponseResult<User>(ResponseResult.CodeStatus.OK,"获取个人信息",user);
    }
    /**
     * 更新个人信息
     * @param profileParam {@link ProfileParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "update")
    public ResponseResult<Integer> update(@RequestBody ProfileParam profileParam) {
        User newUser = new User();
        BeanUtils.copyProperties(profileParam,newUser);
        int result = userService.update(newUser);
        // 成功
        if (result > 0) {
            return new ResponseResult<Integer>(ResponseResult.CodeStatus.OK, "更新个人信息成功",result);
        }
        // 失败
        else {
            return new ResponseResult<Integer>(ResponseResult.CodeStatus.FAIL, "更新个人信息失败",0);
        }
    }
    /**
     * 修改密码
     * @param passwordParam {@link PasswordParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "modify/password")
    public ResponseResult<User> modifyPassword(@RequestBody PasswordParam passwordParam) {
        User user = userService.get(passwordParam.getUsername());
        // 旧密码正确
        if (passwordEncoder.matches(passwordParam.getOldPassword(), user.getPassword())) {
            int result = userService.modifyPassword(user.getUsername(), passwordParam.getNewPassword());
            if (result > 0) {
                return new ResponseResult<User>(ResponseResult.CodeStatus.OK, "修改密码成功",user);
            }
        }
        // 旧密码错误
        else {
            return new ResponseResult<User>(ResponseResult.CodeStatus.FAIL, "旧密码不匹配,请重试");
        }
        return new ResponseResult<User>(ResponseResult.CodeStatus.FAIL, "修改密码失败");
    }
    /**
     * 修改头像
     * @param iconParam {@link IconParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "modify/icon")
    public ResponseResult<Integer> modifyIcon(@RequestBody IconParam iconParam) {
        int result = userService.modifyIcon(iconParam.getUsername(), iconParam.getPath());
        // 成功
        if (result > 0) {
            return new ResponseResult<Integer>(ResponseResult.CodeStatus.OK, "更新头像成功",result);
        }
        // 失败
        else {
            return new ResponseResult<Integer>(ResponseResult.CodeStatus.FAIL, "更新头像失败",0);
        }
    }
}
