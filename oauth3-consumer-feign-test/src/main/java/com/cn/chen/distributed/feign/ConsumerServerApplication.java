package com.cn.chen.distributed.feign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
/**
 * @author Administrator
 * @version 1.0
 **/
//@SpringBootApplication不要扫描到tk.mybatis.mapper.common.Mapper;
@SpringBootApplication(scanBasePackages = {"com.cn.chen.distributed"})
@EnableDiscoveryClient
@EnableEurekaClient
@EnableFeignClients
public class ConsumerServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerServerApplication.class, args);
    }
}
