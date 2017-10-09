package com.stt.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stt on 2017/10/5.
	含有缓存机制的redis操作
 */
@Configuration
// 开启缓存
@EnableCaching
public class RedisConfig extends  CachingConfigurerSupport {


	// 自己读取配置，进行JedisConnectionFactory 的初始化，要不使用框架的自动注入方式
//	@Bean
//	public JedisConnectionFactory redisConnectionFactory() {
//		JedisConnectionFactory factory = new JedisConnectionFactory();
//		factory.setHostName(host);
//		factory.setPort(port);
//		factory.setTimeout(timeout); //设置连接超时时间
//		return factory;
//	}

	// 注意这里的factory是自动注入的

	/**
	 * SpringBoot提供了对Redis的自动配置功能，
	 * 在RedisAutoConfiguration中默认为我们配置了
	 * JedisConnectionFactory（客户端连接）、
	 * RedisTemplate以及StringRedisTemplate（数据操作模板），
	 * 其中StringRedisTemplate模板只针对键值对都是字符型的数据进行操作，
	 * 本示例采用RedisTemplate作为数据操作模板，
	 * @param factory
	 * @return
	 */
	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {

		StringRedisTemplate template = new StringRedisTemplate(factory);

		//使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		// 设置序列化工具，这样ReportBean不需要实现Serializable接口
		template.setValueSerializer(jackson2JsonRedisSerializer);
		// 使用StringRedisSerializer来序列化和反序列化redis的key值
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();

		// 设置事务的管理
		template.setEnableTransactionSupport(true);
		return template;
	}

	//自定义缓存key生成策略
	@Bean(name = "defaultKeyGenerator")
	public KeyGenerator defaultKeyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName()).append(":");
				sb.append(method.getName()).append(":");
				for (Object obj : params) {
					sb.append(obj.toString()).append("-");
				}
				// 最后一个 - 删除
				if(params.length != 0) {
					sb.deleteCharAt(sb.lastIndexOf("-"));
				}
				return sb.toString();
			}
		};
	}


	// 缓存存储结构是string的key-value型
	@Bean
	public CacheManager cacheManager(RedisTemplate redisTemplate) {
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
		//设置缓存过期默认时间
		rcm.setDefaultExpiration(60);//秒 86400
		Map<String, Long> expires = new HashMap<>();
		// 使用自定义的超时时间
		expires.put("secondBy10",10L);
		expires.put("secondBy20",20L);
		expires.put("userDefind",30L); // 自定义，初始值30
		rcm.setExpires(expires);
		rcm.setCacheNames(Arrays.asList("secondBy10","secondBy20","userDefind"));
		return rcm;
	}
}
