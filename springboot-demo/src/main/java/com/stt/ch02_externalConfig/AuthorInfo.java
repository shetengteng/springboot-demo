package com.stt.ch02_externalConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 9/25/2017.
 */
@RestController
@SpringBootApplication
public class AuthorInfo {

    @Autowired
    private AuthorSettings settings;

    @RequestMapping("/authorInfo")
    public String getAuthorInfo() {

        return "author info :" + settings.getName() + " : age : " + settings.getAge();

    }

    public static void main(String[] args) {
        SpringApplication.run(AuthorInfo.class);
    }


}
