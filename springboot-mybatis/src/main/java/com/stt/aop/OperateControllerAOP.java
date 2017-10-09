package com.stt.aop;

import com.stt.bean.ResultBean;
import com.stt.exception.CheckException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by stt on 2017/9/30.
 * 操作controller层的aop
 * 添加日志和异常捕获处理,AOP的默认配置属性，其中spring.aop.auto属性默认是开启的，
 * 只要引入了AOP依赖后，默认已经增加了@EnableAspectJAutoProxy。
 */
@Aspect
@Component
@Order(-1) //调整加载顺序
public class OperateControllerAOP {
	private static final Logger log = LoggerFactory.getLogger(OperateControllerAOP.class);

	/**
	 * 定义切点，返回值是ResultBean类型的会被代理
	 */
	@Pointcut("execution(public com.*.bean.ResultBean *(..))")
	public void targetMethod() {
	}

	/**
	 * 打印日志
	 *
	 * @param pjp
	 * @return
	 */
	@Around("targetMethod()")
	public Object handleControllerMethod(ProceedingJoinPoint pjp) {
		Long startTime = System.currentTimeMillis();
		ResultBean<?> result;
		try {
			result = (ResultBean<?>) pjp.proceed();
			log.info(pjp.getSignature() + " use time : " + (System.currentTimeMillis() - startTime) +"ms");
		} catch (Throwable e) {
			result = handleException(pjp, e);
		}
		return result;
	}

	/**
	 * 处理异常信息
	 *
	 * @param pjp
	 * @param e
	 * @return
	 */
	private ResultBean<?> handleException(ProceedingJoinPoint pjp, Throwable e) {
		ResultBean<?> result = new ResultBean();
		if(e instanceof CheckException) {
			result.setMsg(e.getLocalizedMessage());
			result.setCode(ResultBean.FAIL);
//		}else if(e instanceof UnloginException) {
//			result.setMsg("Unlogin");
//			result.setCode(ResultBean.NO_LOGIN);
		}else {
			// 需要记录方法的各个参数值
			log.error(pjp.getSignature() + "error:{}",e);
			result.setMsg(e.getMessage());
			result.setCode(ResultBean.FAIL);
			// 通过邮箱发送异常或者短信通知
			// 注意尽量减少空判断，都放在这里处理，如果有空判断，需要单独抛出异常处理
		}
		return result;
	}

}
