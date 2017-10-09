package com.stt;

import com.stt.entity.StudentInfo;
import com.stt.serivce.StudentInfoService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by stt on 2017/10/3.
 */
public class MongoBaseTest extends  BaseTest{

	@Autowired
	private StudentInfoService service;

	@Test
	public void init() {
		System.out.println("#############测试建立连接");
	}

	@Test
	public void testInsertInfo() {
		StudentInfo info = new StudentInfo();
		info.setAge(22);
		info.setStudentName("stt");
//		info.setId("00-1");
		info.setSecret("秘密");
		StudentInfo result = service.insertInfo(info);
		// 增加成功后会返回一个id，如果设定了id会覆盖原先的_id
		System.out.println(result);
	}

	@Test
	public void testFindByName() {
		StudentInfo info = service.findByStudentName("stt");
		System.out.println(info);
	}

	@Test
	public void testFindByName2() {
		StudentInfo info = service.findByName("stt");
		System.out.println(info);
	}


}
