package com.cn.chen.distributed.order.controller;
import com.cn.chen.distributed.domain.User;
import com.cn.chen.distributed.domain.returnDto.AdminDTO;
import com.cn.chen.distributed.domain.returnDto.ResponseResult;
import com.cn.chen.distributed.dto.params.IconParam;
import com.cn.chen.distributed.dto.params.PasswordParam;
import com.cn.chen.distributed.dto.params.ProfileParam;
import com.cn.chen.distributed.feign.ProfileFeign;
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
@RequestMapping(value = "profile")
public class ProfileController {

    @Autowired
    private ProfileFeign profileFeign;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 获取个人信息
     *
     * @param username {@code String} 用户名
     * @return {@link ResponseResult}
     */
    @GetMapping(value = "/info/{username}")
//    @SentinelResource(value = "info", fallback = "infoFallback", fallbackClass = ProfileControllerFallback.class)
    public ResponseResult<AdminDTO> info(@PathVariable String username) {
        ResponseResult<User> responseResult = profileFeign.info(username);
        System.out.println("ProfileController>>/info>>>"+responseResult);
        User user = responseResult.getData();
        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(user,adminDTO);
        return new ResponseResult<AdminDTO>(ResponseResult.CodeStatus.OK, "获取个人信息",adminDTO);
    }

    /**
     * 更新个人信息
     *
     * @param profileParam {@link ProfileParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "/update")
    public ResponseResult<Void> update(@RequestBody ProfileParam profileParam) {
        ResponseResult<Integer> responseResult = profileFeign.update(profileParam);
        // 成功
        if (responseResult.getData()>0) {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "更新个人信息成功"/*,responseResult.getData()*/);
        }
        // 失败
        else {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.FAIL, "更新个人信息失败"/*,0*/);
        }
    }
    /**
     * 修改密码
     * @param passwordParam {@link PasswordParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "/modify/password")
    public ResponseResult<Void> modifyPassword(@RequestBody PasswordParam passwordParam) {
        ResponseResult<User> responseResult = profileFeign.modifyPassword(passwordParam);
        // 旧密码正确
        if (passwordEncoder.matches(passwordParam.getOldPassword(),responseResult.getData().getPassword())) {
            if (responseResult.getData()!=null) {
                return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "修改密码成功"/*,responseResult.getData()*/);
            }
        }

        // 旧密码错误
        else {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.FAIL, "旧密码不匹配,请重试");
        }

        return new ResponseResult<Void>(ResponseResult.CodeStatus.FAIL, "修改密码失败");
    }

    /**
     * 修改头像
     * @param iconParam {@link IconParam}
     * @return {@link ResponseResult}
     */
    @PostMapping(value = "/modify/icon")
    public ResponseResult<Void> modifyIcon(@RequestBody IconParam iconParam) {
        ResponseResult<Integer> responseResult = profileFeign.modifyIcon(iconParam);
        // 成功
        if (responseResult.getData() > 0) {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.OK, "更新头像成功"/*,responseResult.getData()*/);
        }
        // 失败
        else {
            return new ResponseResult<Void>(ResponseResult.CodeStatus.FAIL, "更新头像失败");
        }
    }
}
