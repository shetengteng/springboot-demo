package com.stt.ch02_externalConfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 9/25/2017.
 */


@Component
// 添加额外配置，如果没有locations则默认从application.properties中进行获取
// @ConfigurationProperties(prefix = "author",locations = {"classpath:/config/author.properties"})
// 注意：在1.5版本以上，取消了locations属性配置，需要添加PropertySource的注解额外配置
@ConfigurationProperties(prefix = "author")
@PropertySource("classpath:/config/author.properties")
public class AuthorSettings {

    // 这里省去了注解@Value("") 原因是添加了前缀，直接匹配前缀.属性名称获取
    private String name;
    private Long age;

    public AuthorSettings() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
}
