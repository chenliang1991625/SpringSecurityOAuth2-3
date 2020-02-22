package com.cn.chen.test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootApplication
public class UAAServerTest {
    public static void main(String[] args) {
        SpringApplication.run(UAAServerTest.class, args);
    }
    @org.junit.Test
    public void test(){
        String encode = new BCryptPasswordEncoder().encode("123");
        System.out.println(encode);
    }
}
