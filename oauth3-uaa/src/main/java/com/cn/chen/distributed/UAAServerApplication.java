package com.cn.chen.distributed;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import tk.mybatis.spring.annotation.MapperScan;
/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootApplication(scanBasePackages = {"com.cn.chen.distributed"})
@EnableDiscoveryClient
@EnableHystrix
//@EnableFeignClients(basePackages = {"com.cn.chen.distributed.feign"})
@EnableAuthorizationServer
//@MapperScan(basePackages = {"com.cn.chen.distributed.dao"})
public class UAAServerApplication{
    public static void main(String[] args) {
        SpringApplication.run(UAAServerApplication.class, args);
    }
}
