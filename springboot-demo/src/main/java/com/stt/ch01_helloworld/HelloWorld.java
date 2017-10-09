package com.stt.ch01_helloworld;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 9/25/2017.
 */
//该注解是组合注解，包含了ResponseBody和Controller这2个注解
@RestController

// 这里使用自带的启动器，进行启动
// 核心注解，用于开启自动配置
// 可以排除数据源的自动配置

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HelloWorld {


    @RequestMapping("/hello")
    public String index() {

        String msg = "hello spring boot ss";
        return msg;
    }

    public static void main(String[] args) {

        // 默认的启动方式：如果在resource中有banner.txt则使用自定义的banner文字或者图案文字
        //SpringApplication.run(HelloWorld.class);

        // 去除启动banner
        SpringApplication app = new SpringApplication(HelloWorld.class);
        // 关闭显示
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

}
