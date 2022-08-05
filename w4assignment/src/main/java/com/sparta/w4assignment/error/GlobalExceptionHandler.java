package com.sparta.w4assignment.error;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex){
		log.error("handleCustomException : {}",ex.getErrorCode());
		return ErrorResponse.toResponseEntity(ex.getErrorCode());
	}

//	@ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
//	public ResponseEntity<ErrorResponse> handleException(Exception ex){
//		log.error("handleException : {}", INTERNAL_SERVER_ERROR);
//		return ErrorResponse.toResponseEntity(INTERNAL_SERVER_ERROR);
//	}
}