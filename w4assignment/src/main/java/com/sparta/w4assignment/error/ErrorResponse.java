package com.sparta.w4assignment.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
	private final LocalDateTime timestamp = LocalDateTime.now();
	private int status;
	private String error;
	private String code;
	private String message;

	public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
		return ResponseEntity
				.status(errorCode.getStatus())
				.body(ErrorResponse.builder()
						.status(errorCode.getStatus())
						.error(errorCode.name())
						.code(errorCode.getErrorCode())
						.message(errorCode.getMessage())
						.build()
				);
	}
}