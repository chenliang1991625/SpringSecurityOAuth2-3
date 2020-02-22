package com.cn.chen.distributed.domain;
import lombok.Data;

import java.io.Serializable;
/**
 * @author Administrator
 * @version 1.0
 **/
@Data
//前端页面展示的数据模型
public class PermissionDto implements Serializable {
    private String id;
    private String code;
    private String description;
    private String url;
}
