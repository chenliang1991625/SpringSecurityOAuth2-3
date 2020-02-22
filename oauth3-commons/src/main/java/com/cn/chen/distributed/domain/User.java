package com.cn.chen.distributed.domain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
/**
 * 系统用户
 * <p>
 * Description:
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-07-26 09:39:06
 */
@Data
@Table(name = "t_user")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    /*
    * 手机
    */
    @Column(name = "mobile")
    private String mobile;
    /**
     * 头像
     */
    @Column(name = "icon")
    private String icon;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 全称
     */
    @Column(name = "fullname")
    private String fullname;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 备注信息
     */
    @Column(name = "note")
    private String note;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "createtime")
    private Date createtime;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "logintime")
    private Date logintime;

    /**
     * 帐号启用状态：0->禁用；1->启用
     */
    @Column(name = "status")
    private Integer status;
}
