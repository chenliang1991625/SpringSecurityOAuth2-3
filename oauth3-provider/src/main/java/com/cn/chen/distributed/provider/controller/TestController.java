package com.cn.chen.distributed.provider.controller;
import com.cn.chen.distributed.domain.User;
import com.cn.chen.distributed.domain.returnDto.UserDTO;
import com.cn.chen.distributed.provider.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    String hi(){
        return "hi TestController";
    }
    @GetMapping(value = "/info/{username}")
    public UserDTO info(@PathVariable String username){
        User user = userService.get(username);
        UserDTO userDTO = new UserDTO();
        /*userDTO.setFullname(user.getFullname());
        userDTO.setIcon(user.getIcon());
        userDTO.setMobile(user.getMobile());
        userDTO.setNickname(user.getNickname());
        userDTO.setPassword(user.getPassword());
        userDTO.setUsername(user.getUsername());*/
        BeanUtils.copyProperties(user,userDTO);
        return  userDTO;
    }
}
