package com.stt.serivce;

import com.stt.entity.Person;

import java.util.List;

/**
 * Created by stt on 2017/9/30.
 */
public interface PersonService {

	public Long add(Person person);

	public Boolean delete(Long id);

	public Long update(Person person);

	public Person getPersonById(Long id);

	public Person getPersonByIdUseDefaultKeyGenerator(Long id);

	public List<Person> getAllPerson();


}
