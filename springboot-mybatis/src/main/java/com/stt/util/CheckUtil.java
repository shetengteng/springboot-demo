package com.stt.util;

import com.stt.exception.CheckException;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

/**
 * Created by stt on 2017/9/30.
 * 判断条件，不通过返回相应的异常
 */
public class CheckUtil {


	public static void check(boolean condition, String msg, Object... params) {
		if (!condition) {
			fail(msg, params);
		}
	}

	public static void notEmptyOrNull(Object obj, String msg, Object... params) {
		if (StringUtils.isEmpty(obj)) {
			fail(msg, params);
		}
	}

	/**
	 * 对于错误消息的处理
	 * @param errors
	 */
	public static void checkErrors(Errors errors) {
		if (errors.hasErrors()) {
			// 取得第一个就抛出
			FieldError error = errors.getFieldErrors().get(0);
			fail(error.getDefaultMessage()+" 字段："+error.getField());
		}
	}

	private static void fail(String msg, Object... params) {
		StringBuilder sb = new StringBuilder(msg);
		if (params != null) {
			for (int i = 0; i < params.length; i++)
				sb.append(":").append(params[i] == null ? "null" : params[i].toString());
		}
		throw new CheckException(sb.toString());
	}
}

