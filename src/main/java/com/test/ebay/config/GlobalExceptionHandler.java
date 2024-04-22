package com.test.ebay.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.test.ebay.entity.Response;

import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest web) {
		Response rs = new Response(400, "An error has occurred. Please contact system administrator");
		log.error("An error has occurred:", ex);
		return handleExceptionInternal(ex, rs, new HttpHeaders(), HttpStatus.BAD_REQUEST, web);
	}
}