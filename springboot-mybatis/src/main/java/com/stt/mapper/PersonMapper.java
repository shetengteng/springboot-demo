package com.stt.mapper;

import com.stt.entity.Person;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

// 在mybatis中可以不用添加Repository注解，因为mybatis配置了扫描的包
@Repository

/**@CacheConfig: 类级别的注解：
 * 如果我们在此注解中定义cacheNames，
 * 则此类中的所有方法上 @Cacheable的cacheNames默认都是此值。
 * 当然@Cacheable也可以重定义cacheNames的值
 **/
@CacheConfig(cacheNames = "person")
public interface PersonMapper {

	/**
	 * 每次执行都会执行方法，无论缓存里是否有值，同时使用新的返回值的替换缓存中的值。
	 * 这里不同于@Cacheable：@Cacheable如果缓存没有值，从则执行方法并缓存数据，
	 * 如果缓存有值，则从缓存中获取值
	 */
	//@CachePut(cacheNames = "person01", key = "#person.id")
	public void add(Person person);

	@CacheEvict(cacheNames = "person01", key = "'p' + #id", allEntries = false)
	public void delete(Long id);
	/**
	 * 更新操作时，需要移除对应的person缓存
	 * allEntries = true: 清空缓存里的所有值
	 * allEntries = false: 默认值，此时只删除key对应的值
	 */
	@CacheEvict(cacheNames = "person01", key = "'p' + #person.id", allEntries = false)
	public void update(Person person);

	/**
	 *  cacheNames 设置缓存的值
	 *  key：指定缓存的key，这是指参数id值。 key可以使用spEl表达式
	 *  如果设置sync=true，
	 *  如果缓存中没有数据，多个线程同时访问这个方法，则只有一个方法会执行到方法，其它方法需要等待
	 *  如果缓存中已经有数据，则多个线程可以同时从缓存中获取数据
	 *  注意：定义key生成的类，和key的不能同时存在
	 *  可以使使用value定义超时时间，获取预定义的键值对，改变值
	 * @param
	 */
//	@Cacheable(cacheNames = "person01", key = "'p' + #id", sync = true)
	// 设置自定义的组名称，含有指定的超时时间
	@Cacheable(cacheNames = "userDefind", keyGenerator = "defaultKeyGenerator")
	public Person getPersonById(Person person);

	// 可以同时使用注解和xml的方式
	@Select("SELECT * FROM tbl_person;")
	public List<Person> getAllPerson();

}