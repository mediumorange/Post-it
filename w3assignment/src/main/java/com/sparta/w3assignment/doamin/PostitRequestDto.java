package com.sparta.w3assignment.doamin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostitRequestDto {
	private final String username;
	private final String password;
	private final String title;
	private final String memo;
}