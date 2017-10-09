package com.stt.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.IOException;
import java.util.Date;

/**
 * Created by stt on 2017/10/2.
 * 用于传递的json中参数的日期的反序列化
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String dateStr = p.getValueAsString();
		DateTime dateTime = DateTime.parse(dateStr,
						DateTimeFormat.forPattern("yyyy-MM-dd" + (dateStr.length() == 10 ? "" : " HH:mm:ss")));
		return dateTime.toDate();
	}
}
