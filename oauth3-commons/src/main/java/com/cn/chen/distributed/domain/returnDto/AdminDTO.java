package com.cn.chen.distributed.domain.returnDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 管理员信息
 * <p>
 * Description:
 * </p>
 *
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-08-28 01:01:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO implements Serializable {
    private Long id;

    private String username;

    /**
     * 头像
     */
    private String icon;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 备注信息
     */
    private String note;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 最后登录时间
     */
    private Date logintime;

    /**
     * 帐号启用状态：0->禁用；1->启用
     */
    private Integer status;
}
