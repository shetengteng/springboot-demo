package com.stt;

import com.stt.entity.Person;
import com.stt.util.JsonUtil;
import com.stt.util.RedisUtil;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * Created by stt on 2017/10/6.
 */
public class RedisBaseTest extends BaseTest {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void init() {

	}

	@Test
	public void testExecute() {

		// 使用通用的方式执行redis的操作
		Object execute = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				connection.set("testExecute".getBytes(),"resultValue".getBytes());
				return true;
			}
		});

		System.out.println(execute);

	}

	@Test
	public void testExecute2() {
//		Object execute = RedisUtil.execute(new RedisCallback<Boolean>() {
//			@Override
//			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//				connection.set("testExecute".getBytes(),"resultValue".getBytes());
//				return true;
//			}
//		});

		Object execute = RedisUtil.execute(connection -> {
			connection.set("java8".getBytes(),"java8Value".getBytes());
			return true;
		});

		System.out.println(execute);
	}

	@Test
	public void testAdd() throws IOException {

		Person p = new Person();
		p.setName("stt");
		p.setId(1L);
		p.setAge(22);
		p.setSalary(100);
		p.setBirth(new Date());

		redisTemplate.opsForValue().set("person01",p);

		// String类型操作
		Person p2 = (Person) redisTemplate.opsForValue().get("person01");
		System.out.println(p2);
	}

	@Test
	public void testAdd2() {
		Person p = new Person();
		p.setName("stt2");
		p.setId(2L);
		p.setAge(22);
		p.setSalary(100);
		p.setBirth(new Date());

		RedisUtil.set("person02",p);

		// String类型操作
		Person p2 = (Person)RedisUtil.get("person02");
		System.out.println(p2);
	}

	@Test
	public void testSetnx() {
		Person p = new Person();
		p.setName("stt2");
		p.setId(2L);
		p.setAge(22);
		p.setSalary(100);
		p.setBirth(new Date());

		System.out.println(RedisUtil.setnx("person03",p));
	}

	@Test
	public void testJson() throws IOException {
		Person p = new Person();
		p.setName("stt");
		p.setId(1L);
		p.setAge(22);
		p.setSalary(100);
		p.setBirth(new Date());
		String json = JsonUtil.bean2Json(p);
		System.out.println(json);
		Person p2 = JsonUtil.json2Bean(json,Person.class);
		System.out.println(p2);
	}

	@Test
	public void testIncr() {

		System.out.println("zhujian:"+ RedisUtil.incr("zhujian",60));

	}

	@Test
	public void testdel() {
		Long del = RedisUtil.del("zhujian", "stt2", "java8", "person02");
		System.out.println(del);
	}

	@Test
	public void testTime() {

		DateTime dateTime = new DateTime(RedisUtil.getTime());
		System.out.println(dateTime.toString("yyyy-MM-dd HH:mm:ss"));
	}

	@Test
	public void testList() {
		Person p = new Person();
		p.setName("stt");
		p.setId(1L);
		p.setAge(22);
		p.setSalary(100);
		p.setBirth(new Date());

		Long lpush = RedisUtil.lpush("listTest", p, 3, 5, "ss");
		System.out.println(lpush);

		List<Object> listTest = RedisUtil.lrange("listTest", 0, -1);
		for (Object obj :
				listTest) {
			System.out.println(obj.toString());
		}
	}

	@Test
	public void testSet() {
		Person p = new Person();
		p.setName("stt");
		p.setId(1L);
		p.setAge(22);
		p.setSalary(100);
		p.setBirth(new Date());
		long testAdd = RedisUtil.sadd("testAdd", p, "1", "3", p);
		System.out.println(testAdd);

		Set testAdd1 = RedisUtil.smembers("testAdd");
		for (Object value: testAdd1
			 ) {
			System.out.println(value);
		}
	}

}
