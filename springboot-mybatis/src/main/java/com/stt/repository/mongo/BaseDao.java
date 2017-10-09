package com.stt.repository.mongo;

import com.mongodb.WriteResult;
import com.stt.entity.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;

/**
 * Created by stt on 2017/10/4.
 */
public abstract class BaseDao<T> {

	@Autowired
	private MongoTemplate mongoTemplate;

	private Class typeClass = null;

	public BaseDao() {
		Class clazz = this.getClass();
		Type type = clazz.getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) type;
		Type[] types = pt.getActualTypeArguments();
		typeClass = (Class) types[0];
	}

	/**
	 * 更新操作，对于没有的属性则不进行更新操作
	 *
	 * @param model
	 * @param id
	 * @return
	 * @throws IllegalAccessException
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 */
	public int update(T model, Object id, String idFieldName) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
		// 这里有idFieldName表示该字段要在update中忽略，不能被修改
		Update update = this.getUpdateParam(model, idFieldName);
		Query query = new Query();
		query.addCriteria(Criteria.where(idFieldName).is(id));
		WriteResult r = mongoTemplate.updateFirst(query, update, StudentInfo.class);
		return r.getN();
	}

	private Update getUpdateParam(T model, String idFieldName) throws InvocationTargetException, IllegalAccessException, IntrospectionException {

		// 新info中的不为空的部分进行替换操作
		Update update = new Update();
		Field[] fields = typeClass.getDeclaredFields();

		for (Field f : fields) {
			f.setAccessible(true);
			String propertyName = f.getName();
			// 修改的主键要被忽略
			if (propertyName.equals(idFieldName)) {
				continue;
			}
			// 该方法在createTime字段变成了isCreateTime，有问题，异常,没有该方法的时候就抛出异常，会尝试isxxx
			PropertyDescriptor pd = new PropertyDescriptor(f.getName(),typeClass);
			// 获取get方法，并获取到值，对该值进行判断是否为空
			Method readMethod = pd.getReadMethod();

			//方式2
//			String methodStr = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
//			Method readMethod = null;
//			try {
//				readMethod = typeClass.getMethod(methodStr);
//			} catch (NoSuchMethodException e) {
//				continue;
//			}

			if (readMethod == null) {
				continue;
			}
			Object value = readMethod.invoke(model);
			if (value != null) {
				// 如果注解field中有值，则获取数据库中的字段名
				if (f.isAnnotationPresent(org.springframework.data.mongodb.core.mapping.Field.class)) {
					String fieldName = f.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class).value();
					if (!StringUtils.isEmpty(fieldName)) {
						update.set(fieldName, value);
						continue;
					}
				}
				update.set(f.getName(), value);
			}
		}
		return update;
	}

}
