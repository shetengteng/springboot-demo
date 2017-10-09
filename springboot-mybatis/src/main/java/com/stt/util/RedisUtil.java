package com.stt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by stt on 2017/10/5.
 */
@Component
public class RedisUtil {

	private static RedisTemplate redisTemplate;

	// 静态变量不能使用autowired直接注入
	@Autowired
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		RedisUtil.redisTemplate = redisTemplate;
	}

	public static RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	// 通用执行方法包装
	public static <T> T execute(RedisCallback<T> action) {
		return (T) redisTemplate.execute(action);
	}

	// 用于含有事务的方法的包装
	public static <T> T executeSession(SessionCallback<T> session) {
		return (T) redisTemplate.execute(session);
	}

	// get操作
	public static Object get(final String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public static <V> void set(String key, V value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * 使用 setnx 命令，如果已经存在，则返回0 表示插入失败，如果不存在，则set操作并返回 1
	 *
	 * @param key
	 * @param value
	 * @return 设置成功，返回1 设置失败，返回0
	 */
	public static <V> Boolean setnx(String key, V value) {
		return redisTemplate.opsForValue().setIfAbsent(key, value);
	}

	/**
	 * 用于使用redis生成自定义递增主键
	 *
	 * @param key
	 * @param expireSeconds
	 * @return
	 */
	public static Long incr(String key, Integer expireSeconds) {
		// 涉及到事务，需要使用sessionCallback进行操作
		return (Long) redisTemplate.execute(new SessionCallback<Object>() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				List<Object> result = null;
				do {
					operations.watch(key);
					operations.multi();
					operations.opsForValue().increment(key, 1L);
					operations.expire(key, expireSeconds, TimeUnit.SECONDS);
					result = operations.exec();
					// 注意：redis返回的是基本数据类型，如果此处是Long，则会抛出异常
				} while (result == null);
				return result.get(0);
			}
		});
	}

	/*****list操作*****/

	/**
	 * 压栈
	 * @param key
	 * @param values
	 * @return
	 */
	public static Long lpush(String key,Object ... values) {

		return redisTemplate.opsForList().leftPushAll(key, values);
	}

	/**
	 * 出栈
	 * @param key
	 * @return
	 */
	public static Object lpop(String key) {

		return redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 入队
	 *
	 * @param key
	 * @param values
	 * @return
	 */
	public static Long rpush(String key, Object ... values) {

		return redisTemplate.opsForList().rightPush(key, values);
	}

	/**
	 * 出队
	 *
	 * @param key
	 * @return
	 */
	public static Object rpop(String key) {

		return redisTemplate.opsForList().rightPop(key);
	}

	/**
	 * list的长度
	 * @param key
	 * @return
	 */
	public static Long llen(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 范围检索
	 * 如果indexEnd的值是-1或者大于列表的长度，则显示全部
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Object> lrange(String key, int start, int end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 移除key中的为value的n个元素
	 *
	 * @param key
	 * @param n 为0表示删除全部为value值的元素
	 * @param value
	 */
	public static void lrem(String key, long n, Object value) {
		redisTemplate.opsForList().remove(key, n, value);
	}

	/**
	 * 检索
	 * 以顶部为起始下标
	 * @param key
	 * @param index
	 * @return
	 */
	public static Object lindex(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 置值
	 *
	 * @param key
	 * @param index
	 * @param value
	 */
	public static void lset(String key, long index, Object value) {
		redisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * 裁剪
	 *
	 * @param key
	 * @param start
	 * @param end
	 */
	public static void ltrim(String key, long start, int end) {
		redisTemplate.opsForList().trim(key, start, end);
	}

	/***hash 操作**/
	public static long sadd(String key,Object ... values) {
		return redisTemplate.opsForSet().add(key,values);
	}

	/**
	 * 判断value是否属于该set集合
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean sismember(String key,Object value) {
		return redisTemplate.opsForSet().isMember(key,value);
	}

	/**
	 * 返回所有值的set集合
	 * @param key
	 * @return
	 */
	public static Set smembers(String key) {
		Set members = redisTemplate.opsForSet().members(key);
		return members;
	}

	/**
	 * 删除键操作
	 * @param keys
	 * @return
	 */
	public static Long del(String... keys) {
		return execute(connection -> {
			byte[][] params= new byte[keys.length][];
			for(int i = 0;i<keys.length;i++) {
				params[i] = keys[i].getBytes();
			}
			return connection.del(params);
		});
	}

	/**
	 * 获取当前redis服务器的时间 ms
	 * @return
	 */
	public static Long getTime() {
		return	execute(connection -> {
			return connection.time();
		});
	}



}
