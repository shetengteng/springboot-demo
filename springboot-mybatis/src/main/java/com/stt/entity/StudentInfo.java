package com.stt.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.stt.common.DateJsonDeserializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by stt on 2017/10/3.
 * mongodb测试使用
 */

//复合索引的声明，建复合索引可以有效地提高多字段的查询效率。
//@CompoundIndexes({
//		@CompoundIndex(name = "id_name_idx", def = "{'id': 1, 'name': -1}",unique = true,dropDups = true)
//})
// def 表示符合索引的字段，unique表示唯一索引，dropDups建立索引时去除重复索引项

@Document(collection = "student")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentInfo {

	@Id // 声明id并赋值 会替代原mongo中的_id字段
	private String id;

	@Field("name")
	@Indexed // 表示建立索引,单值索引
	private String studentName;

	private Integer age;

	@Transient // 表示该字段不存储在数据库中
	private String secret;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonDeserialize(using = DateJsonDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;

	public StudentInfo() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String name) {
		this.studentName = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "StudentInfo{" +
				"id='" + id + '\'' +
				", studentName='" + studentName + '\'' +
				", age=" + age +
				", secret='" + secret + '\'' +
				'}';
	}
}
