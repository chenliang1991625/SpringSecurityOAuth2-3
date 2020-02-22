package com.cn.chen.distributed.domain.returnDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.io.Serializable;
/**
 * 用户信息
 */
//前端页面展示的数据模型
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class UserDTO implements Serializable {
    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
//   密码
    private String password;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 姓名
     */
    private String fullname;
//    头像
    private String icon;
//     别名
    private String nickname;
}
