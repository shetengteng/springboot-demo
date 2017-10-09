package com.stt.repository.mongo;

import com.stt.entity.StudentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by stt on 2017/10/4.
 */
// 使用原生api进行操作,注意是和springdata组合使用
@Repository
public class StudentInfoRepositoryImpl extends BaseDao<StudentInfo> implements StudentInfoDao{

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public StudentInfo findById(String id) {
		StudentInfo info = mongoTemplate.findById(id, StudentInfo.class);
		info.setSecret("使用自定义的方式获取");
		return info;
	}

	@Override
	public List<StudentInfo> findByNameOrSecret(String name,String secret) {

		Query query = new Query();
		// 模糊查询，使用正则
		query.addCriteria(Criteria.where("name").regex(name));
		if(!StringUtils.isEmpty(secret)) {
			// 精确查询
			query.addCriteria(Criteria.where("secret").is(secret));
		}
		List<StudentInfo> result = mongoTemplate.find(query,StudentInfo.class);
		return result;
	}

	@Override
	public List<StudentInfo> findPageList(String name,Integer pageIndex,Integer pageSize) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name));
		query.limit(pageIndex);
		query.skip(pageSize);
		query.with(new Sort(Sort.Direction.DESC,"createTime"));
		return mongoTemplate.find(query,StudentInfo.class);
	}

	@Override
	public int update(StudentInfo info,String id){
		try {
			return super.update(info,id,"id");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


}
