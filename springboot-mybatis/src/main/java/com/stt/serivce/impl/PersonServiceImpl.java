package com.stt.serivce.impl;

import com.stt.entity.Person;
import com.stt.mapper.PersonMapper;
import com.stt.serivce.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by stt on 2017/9/30.
 */
@Service
// 可以指定事务管理器对象
@Transactional
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonMapper personMapper;

	@Override
	public Long add(Person person) {
		personMapper.add(person);
		return person.getId();
	}

	@Override
	public Boolean delete(Long id) {
		personMapper.delete(id);
		return true;
	}

	@Override
	public Long update(Person person) {
		// 检查person的id是否为空，关键点处理
		personMapper.update(person);
		return person.getId();
	}

	@Override
	public Person getPersonById(Long id) {
		System.out.println("########getPersonById:"+id);
		Person person = new Person();
		person.setId(id);
		return personMapper.getPersonById(person);
	}

	/**
	 * 使用自定义的key生成策略
	 * @param id
	 * @return
	 */
	@Override
	public Person getPersonByIdUseDefaultKeyGenerator(Long id) {
		Person person = new Person();
		person.setId(id);
		return personMapper.getPersonById(person);
	}

	@Override
	public List<Person> getAllPerson() {
		return personMapper.getAllPerson();
	}
}
