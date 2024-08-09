package com.study.mysite;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND,reason="entity not found")
public class DataNonFoundException extends RuntimeException{
	private static final long serialVersionUTD=1L;
	public DataNonFoundException(String message) {
		super(message);
	}
}
