package com.stt.serivce;

import com.stt.entity.StudentInfo;
import com.stt.exception.CheckException;
import com.stt.repository.mongo.StudentInfoRepository;
import com.stt.util.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by stt on 2017/10/3.
 */
@Service
public class StudentInfoService {

	@Autowired
	private StudentInfoRepository repository;

	public StudentInfo findByStudentName(String name) {
		StudentInfo info = repository.findByStudentName(name);
		return info;
	}

	// 这里报错，有异常
	public StudentInfo findByName(String name) {
//		StudentInfo info = repository.findByName(name);
//		return info;
		return null;
	}

	// 使用自定义的方法
	public List<StudentInfo> findByName2(String name) {
		List<StudentInfo> result = repository.findByNameOrSecret(name, null);
		return result;
	}

	public StudentInfo insertInfo(StudentInfo info) {
		// 在该方法，如果info中没有给id赋值，那么会自动生成一个id的值，并返回
		return repository.insert(info);
	}

	public StudentInfo findById(String id) {
		// 使用自定义名称的方式
		return repository.findById(id);
		// 使用springdata自带的api
		//return repository.findOne(id);
	}

	public String save(StudentInfo info) {
		StudentInfo result = repository.insert(info);
		return result.getId();
	}

	public Boolean delete(String id) {
		repository.delete(id);
		return true;
	}

	///////////////////////////////////////////////////////

	public StudentInfo update(StudentInfo info) {
		CheckUtil.check(repository.exists(info.getId()), "id 对应的信息不存在", "id value", info.getId());
		// 修改需要使用自定义的修改方式
		if (repository.update(info, info.getId()) == 1) {
			// 更新完成之后返回
			return repository.findOne(info.getId());
		} else {
			throw new CheckException("更新异常失败");
		}
	}


}
