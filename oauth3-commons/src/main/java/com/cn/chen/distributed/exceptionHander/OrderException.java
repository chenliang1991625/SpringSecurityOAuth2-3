package com.cn.chen.distributed.exceptionHander;
import com.cn.chen.distributed.domain.returnDto.OrderStatus;
/**
 * consumer服务全局业务异常
 * <p>
 * Description:
 * </p>
 * @author Lusifer
 * @version v1.0.0
 * @date 2019-10-24 01:21:25
 */
public class OrderException extends RuntimeException {
    private static final long serialVersionUID = 3034121940056795549L;
    private Integer code;
    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public OrderException() {
    }
    public OrderException(OrderStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }
}
