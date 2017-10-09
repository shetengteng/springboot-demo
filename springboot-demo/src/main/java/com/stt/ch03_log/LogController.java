package com.stt.ch03_log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 9/25/2017.
 */
@RestController

@SpringBootApplication

public class LogController {
    private final Logger log = LoggerFactory.getLogger(LogController.class);

    @RequestMapping("/log")
    public String log() {

        // 默认debug不显示
        log.debug("this is debug message");
        log.info("this is info message");
        log.warn("this is warn message");
        log.error("this is error message");
        log.info("this is {} message 2","info");


        // Spring Boot为Logback提供了默认的配置文件,base.xml，
        // 另外Spring Boot 提供了两个输出端的
        // 配置文件console-appender.xml和file-appender.xml，base.xml引用了这两个配置文件。

        return "ok";
    }

    public static void main(String[] args) {
        SpringApplication.run(LogController.class);
    }

}
