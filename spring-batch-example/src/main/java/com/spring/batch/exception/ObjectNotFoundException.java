package com.spring.batch.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author Shahzad Hussain
 */
@Getter
public class ObjectNotFoundException extends Exception {

	private static final long serialVersionUID = 460396670844622215L;

	private int code;

	private HttpStatus httpStatus;

	private Object responseObject;

	public ObjectNotFoundException(String exceptionMessage) {
		super(exceptionMessage);
	}

	public ObjectNotFoundException(String exceptionMessage, HttpStatus httpStatus) {
		super(exceptionMessage);
		this.code = httpStatus.value();
		this.httpStatus = httpStatus;
	}

	public ObjectNotFoundException(String exceptionMessage, Object responseObject, HttpStatus httpStatus) {
		super(exceptionMessage);
		this.code = httpStatus.value();
		this.httpStatus = httpStatus;
		this.responseObject = responseObject;
	}

}
