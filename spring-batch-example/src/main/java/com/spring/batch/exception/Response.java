package com.spring.batch.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * @author Shahzad Hussain
 */
@Getter
@Setter
@NoArgsConstructor
public class Response {

	private int code;
	private String message;
	private HttpStatus status;
	private Object data;
	private Long totalCount;

	public Response(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
		this.code = status.value();
	}

	public Response(String message, Object responseObject, HttpStatus status) {
		this.message = message;
		this.status = status;
		this.code = status.value();
		this.data = responseObject;
	}

	public Response(String message, Object data, Long totalCount, HttpStatus status) {
		this.code = status.value();
		this.message = message;
		this.status = status;
		this.data = data;
		this.totalCount = totalCount;
	}
}