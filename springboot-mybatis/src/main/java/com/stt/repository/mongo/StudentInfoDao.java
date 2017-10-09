package com.stt.repository.mongo;

import com.stt.entity.StudentInfo;

import java.util.List;

/**
 * Created by stt on 2017/10/4.
 */
// 使用原生api进行操作
public interface StudentInfoDao {

	// 如果使用自定义的方式获取，则会覆盖springdata中同名方法
	public StudentInfo findById(String id);

	public List<StudentInfo> findByNameOrSecret(String name,String secret);

	public List<StudentInfo> findPageList(String name,Integer pageIndex,Integer pageSize);

	public int update(StudentInfo info,String id);


}
