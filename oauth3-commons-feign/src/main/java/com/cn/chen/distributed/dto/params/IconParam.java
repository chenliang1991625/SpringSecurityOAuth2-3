package com.cn.chen.distributed.dto.params;
import lombok.Data;
import java.io.Serializable;
/**
 * 修改头像参数
 * <p>
 * Description:
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-08-26 23:44:42
 */
@Data
public class IconParam implements Serializable {
    /**
     * 用户名
     */
    private String username;
    /**
     * 头像地址
     */
    private String path;
}
