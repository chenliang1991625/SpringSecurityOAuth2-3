package com.cn.chen.distributed.feign;
import com.cn.chen.distributed.domain.returnDto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * 通过FeignClient配置负载均衡，指定了服务提供者的名字
 */
@FeignClient(value = "provider-service",path = "test")
public interface HelloService {
    //这里指定调用的rest URL
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String hi();
    @GetMapping(value = "/info/{username}")
    public UserDTO info(@PathVariable String username);
}
