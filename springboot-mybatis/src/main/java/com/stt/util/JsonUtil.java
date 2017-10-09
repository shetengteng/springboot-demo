package com.stt.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by stt on 2017/9/30.
 * json 转换工具类
 */
public class JsonUtil {

	// 定义jackson对象 mapper
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 将对象转换成json字符串。
	 *
	 * @param data
	 * @return
	 */
	public static String bean2Json(Object data) throws JsonProcessingException {
		return MAPPER.writeValueAsString(data);
	}

	/**
	 * 将对象转为json字符串的byte数组
	 *
	 * @param data
	 * @return
	 * @throws JsonProcessingException
	 */
	public static byte[] bean2JsonBytes(Object data) throws JsonProcessingException {
		return MAPPER.writeValueAsBytes(data);
	}


	/**
	 * 将json结果集转化为对象
	 *
	 * @param jsonData json数据
	 * @param beanType 对象中的object类型
	 * @return
	 */
	public static <T> T json2Bean(String jsonData, Class<T> beanType) throws IOException {
		if (StringUtils.isEmpty(jsonData)) {
			return null;
		}
		T t = MAPPER.readValue(jsonData, beanType);
		return t;
	}

	public static <T> T json2Bean(byte[] json, Class<T> beanType) throws IOException {
		if (json == null || json.length == 0) {
			return null;
		}
		return MAPPER.readValue(json, beanType);
	}


	/**
	 * 将json数据转换成pojo对象list
	 *
	 * @param jsonData
	 * @param beanType
	 * @return
	 */
	public static <T> List<T> json2List(String jsonData, Class<T> beanType) throws IOException {
		JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
		List<T> list = MAPPER.readValue(jsonData, javaType);
		return list;
	}

}