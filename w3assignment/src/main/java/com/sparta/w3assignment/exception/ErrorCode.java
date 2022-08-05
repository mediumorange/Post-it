package com.sparta.w3assignment.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	NOT_FOUND(404, "COMMON-ERR-500", "PAGE NOT FOUND"),
	INTER_SERVER_ERROR(500, "MEMBER-ERR-400", "EMAIL DUPLICATED"),
	EXCEEDED_CHARACTER_COUNT(500, "ERR-400?", "CHARACTER COUNT IS EXCEEDED"),
	;

	private int status;
	private String errorCode;
	private String message;
}