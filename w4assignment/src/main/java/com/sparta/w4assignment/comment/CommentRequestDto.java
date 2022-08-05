package com.sparta.w4assignment.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequestDto {
	private final Long userKey;
	private final Long postKey;

	private final String CommentContent;
}