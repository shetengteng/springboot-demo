package com.stt.web;

import com.stt.bean.ResultBean;
import com.stt.entity.StudentInfo;
import com.stt.serivce.StudentInfoService;
import com.stt.util.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by stt on 2017/10/4.
 */
@RestController
@RequestMapping(value="/student",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class StudentController
{
	@Autowired
	private StudentInfoService service;

	@PostMapping("/add")
	public ResultBean<String> addStudentInfo(@RequestBody StudentInfo info) {
		return new ResultBean<String>(service.save(info));
	}

	@DeleteMapping("/{id}")
	public ResultBean<Boolean> deleteStudentInfo(@PathVariable String id) {
		return new ResultBean<Boolean>(service.delete(id));
	}

	@PutMapping("/update")
	public ResultBean<StudentInfo> updateStudentInfo(@RequestBody StudentInfo info) {
		CheckUtil.notEmptyOrNull(info.getId(),"更新 studentInfo 实例时 id 为空");
		return new ResultBean<StudentInfo>(service.update(info));
	}

	@GetMapping("/{id}")
	public ResultBean<StudentInfo> getStudentById(@PathVariable String id){
		return new ResultBean<StudentInfo>(service.findById(id));
	}

	@GetMapping("/name/{name}")
	public ResultBean<List<StudentInfo>> getStudentByName(@PathVariable String name) {
		return new ResultBean<>(service.findByName2(name));
	}

}
