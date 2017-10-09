package com.stt;

import com.stt.bean.ResultBean;
import com.stt.entity.Person;
import com.stt.mapper.PersonMapper;
import com.stt.serivce.PersonService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by stt on 2017/9/30.
 */
public class MyBatisBaseTest extends  BaseTest{

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	private PersonService personService;

	@Test
	public void test_add() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);
		Person p = new Person();
		p.setAge(33);
		p.setName("stt");
		p.setBirth(new Date());
		p.setRegisterTime(new Date());
		p.setSalary(0.1);
		mapper.add(p);

		System.out.println("person-id :" + p.getId());

		sqlSession.commit();

		sqlSession.close();
		System.out.println("test_add finished");
	}

	@Test
	public void test_get() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		PersonMapper mapper = sqlSession.getMapper(PersonMapper.class);

		List<Person> list = mapper.getAllPerson();
		for (Person p :
				list) {
			System.out.print(p);
		}

		sqlSession.commit();

		sqlSession.close();
		System.out.print("test_add finished");
	}

	@Test
	public void test_get2() {
		List<Person> list = personService.getAllPerson();
		for (Person p :
				list) {
			System.out.print(p);
		}
		System.out.print("test_add finished");

		ResultBean bean = new ResultBean();

	}

	@Test
	public void test_update() {
		Person p = new Person();
		p.setSalary(99);
		p.setId(2L);
		p.setName("ss");
		System.out.println(personService.update(p));

	}

}
