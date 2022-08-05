package com.sparta.w4assignment.post;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostRequestDto {
	private final String postTitle;
	private final String postContent;
}