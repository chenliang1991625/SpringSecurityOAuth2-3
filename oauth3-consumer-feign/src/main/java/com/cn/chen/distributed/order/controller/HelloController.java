package com.cn.chen.distributed.order.controller;
import com.cn.chen.distributed.domain.User;
import com.cn.chen.distributed.domain.returnDto.UserDTO;
import com.cn.chen.distributed.feign.HelloService;
import com.cn.chen.distributed.order.service.MyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloController {
    @Autowired
    MyService myService;
    @Autowired
    HelloService helloService;
    @RequestMapping("/hello")
    public String hi(){
        //这里直接写的是服务名： springcloud-eureka-serviceprovider  。在ribbon中它会根据服务名来选择具体的服务实例，根据服务实例在请求的时候会用具体的url替换掉服务名
        return helloService.hi();
    }
    @GetMapping(value = "/info/{username}")
    public User info(@PathVariable String username){
        UserDTO userDTO = helloService.info(username);
        User user = new User();
        try {
            BeanUtils.copyProperties(userDTO,user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
//        return myService.get(username);
    }
}
