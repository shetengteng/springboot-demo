package com.stt.web;

import com.stt.bean.ResultBean;
import com.stt.entity.Person;
import com.stt.serivce.PersonService;
import com.stt.util.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by stt on 2017/9/30.
 * 增删改查示例
 */
@RestController
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonController {

	@Autowired
	private PersonService personService;

	@GetMapping(value="/{id}")
	public ResultBean<Person> getPersonById(@PathVariable Long id) {
		return new ResultBean<Person>(personService.getPersonById(id));
	}

	@GetMapping(value="/cache2/{id}")
	public ResultBean<Person> getPersonById2(@PathVariable Long id) {
		return new ResultBean<Person>(personService.getPersonByIdUseDefaultKeyGenerator(id));
	}

	@DeleteMapping("/{id}")
	public ResultBean<Boolean> deletePersonById(@PathVariable Long id) {
		return new ResultBean<Boolean>(personService.delete(id));
	}

	@PostMapping("/add")
	// 注意：发送参数的Content-Type 是 application/json 的情况下
	// 且有@RequestBody，person对象才有值 ，否则入参是form表单的形式，即form-data情况下要去除 RequestBody 才有效果
	public ResultBean<Long> addPerson(@RequestBody Person person) {
		return new ResultBean<Long>(personService.add(person));
	}

	@GetMapping("/list")
	public ResultBean<List<Person>> getPersonList() {
		return new ResultBean<List<Person>>(personService.getAllPerson());
	}

	/**
	 * put 修改操作
	 * @param
	 * @return
	 */
	// 方式1：注意：在ajax提交中，content-Type必须设置为：x-www-form-urlencoded
	// 'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
	// 同时，在入参的使用不能使用@RequestBoy注解，否则会返回415错误

	// 方式2：content-Type必须设置为：application/json 同时添加注解RequestBody
	// 发送json类型的入参person才有值

	@PutMapping("/update")
	public ResultBean<Long> updatePerson(@Valid @RequestBody Person person, Errors bindingResult) {

		// 从bindingResult中获取错误消息
		// 注意：在Valid注解声明的对象和BindingResult对象之间不能有其他对象，同时BindingResult是Error的子类，可以使用Error代替
//		if(bindingResult.hasErrors()) {
//			for (FieldError fieldError: bindingResult.getFieldErrors()) {
//				System.out.println(fieldError.getField() +":"+ fieldError.getDefaultMessage());
//			}
//			// 可以跳转到指定的错误页面
//		}
		CheckUtil.checkErrors(bindingResult);
		CheckUtil.notEmptyOrNull(person.getId(),"person 的 id 为空");
		return new ResultBean<Long>(personService.update(person));
	}

}
