package com.stt.repository.mongo;

import com.stt.entity.StudentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by stt on 2017/10/3.
 */
// 使用springdata自带的api进行操作
public interface StudentInfoRepository extends MongoRepository<StudentInfo,String>,StudentInfoDao{

//	（1）save ：我们在新增文档时，如果有一个相同_ID的文档时，会覆盖原来的。saveOrUpdate
//	（2）insert：我们在新增文档时，如果有一个相同的_ID时，就会新增失败。

	// 继承使用 MongoRepository 含有findall，以及分页的方法

	public StudentInfo findByStudentName(String studentName);
	// 注意：由于StudentInfo中没有name属性，
	// 虽然注解Field上是表明了name，但依然要按照bean中的名称,否则使用会有异常
	//public StudentInfo findByName(String name);

	// 使用 id获取 ,也可以使用自带的方法 findOne
	public StudentInfo findById(String id);

}
