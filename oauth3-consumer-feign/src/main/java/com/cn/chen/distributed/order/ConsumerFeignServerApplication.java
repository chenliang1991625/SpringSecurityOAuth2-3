package com.cn.chen.distributed.order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import tk.mybatis.spring.annotation.MapperScan;
/**
 * @author Administrator
 * @version 1.0
 **/
//@SpringBootApplication不要扫描到tk.mybatis.mapper.common.Mapper;
/*@SpringBootApplication(scanBasePackages = {"com.cn.chen.distributed.order.config","com.cn.chen.distributed.order.controller","com.cn.chen.distributed.order.filter"
,"com.cn.chen.distributed.order.service","com.cn.chen.distributed.config","com.cn.chen.distributed.feign.fallback","com.cn.chen.distributed.interceptor",
"com.cn.chen.distributed.service","com.cn.chen.distributed.dao"})*/
@SpringBootApplication(scanBasePackages ={"com.cn.chen.distributed"})
@EnableDiscoveryClient
@EnableEurekaClient
@MapperScan(basePackages = "com.cn.chen.distributed.dao")
@EnableFeignClients(basePackages = {"com.cn.chen.distributed.feign"})
@EnableAuthorizationServer
//@Import(FeignRequestConfiguration.class)
public class ConsumerFeignServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerFeignServerApplication.class, args);
    }
}
