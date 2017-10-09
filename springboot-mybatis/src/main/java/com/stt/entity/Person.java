package com.stt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.stt.common.DateJsonDeserializer;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by stt on 2017/9/29.
 */
// 用于标记忽略的未知的字段
@JsonIgnoreProperties(ignoreUnknown = true)
//将该标记放在属性上，如果该属性为NULL则不参与序列化
//@JsonInclude(JsonInclude.Include.NON_NULL)
// 这2个注解是在使用jsonUtil的使用时有效果

public class Person implements Serializable {

	private Long id;

//	@NotEmpty(message = "人的姓名不能为空！") // 定义默认错误消息
	// 使用文件配置的方式获取错误消息
	// 注意：错误消息的文件的名称一定要是 ValidationMessages.properties
	@NotEmpty(message = "{person.name.notEmpty}")
	// message 使用 {} 代表错误内容，从 resources 目录下的 ValidationMessages.properties 文件中读取
	@Pattern(regexp = "[a-zA-Z0-9_]{5,10}", message = "{person.name.illegal}")
	private String name;

	@Min(0)
	private int age;

	@Past // 表示必须是过去的时间
	// 前台传递给后台转换使用 (需要配置annotation-drive标签，但是springboot内部自动添加了)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	// 在前台传递json的情况下，替代上个注解，注意：发送参数的Content-Type 是 application/json
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") // 后台传递给前台转换使用
	private Date birth;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date registerTime;

	private double salary;

	public Person() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", birth=" + birth +
				", registerTime=" + registerTime +
				", salary=" + salary +
				'}';
	}
}

