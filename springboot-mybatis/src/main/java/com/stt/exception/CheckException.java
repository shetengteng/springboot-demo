package com.stt.exception;

/**
 * Created by stt on 2017/9/30.
 * 自定义异常
 */

public class CheckException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CheckException() {
		super();
	}

	public CheckException(String msg) {
		super(msg);
	}

	public CheckException(Throwable cause) {
		super(cause);
	}

	public CheckException(String msg,Throwable cause) {
		super(msg,cause);
	}

	public CheckException(String msg, Throwable cause,
							   boolean enableSuppression,
							   boolean writableStackTrace) {
		super(msg, cause, enableSuppression, writableStackTrace);
	}


}
