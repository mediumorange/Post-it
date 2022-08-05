package com.sparta.w3assignment.exception;

import lombok.Getter;

@Getter
public class CharacterCountException extends RuntimeException {
	private ErrorCode errorCode;
	public CharacterCountException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
}
