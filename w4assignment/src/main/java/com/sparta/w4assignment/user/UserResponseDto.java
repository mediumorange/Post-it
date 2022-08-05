package com.sparta.w4assignment.user;

import lombok.*;

@Getter
@Builder
public class UserResponseDto {
	private final Long userKey;
	private final String userId;
	private final String userName;


	public static UserResponseDto of(User user) {
		return UserResponseDto.builder()
				.userKey(user.getUserKey())
				.userId(user.getUserId())
				.userName(user.getUserName())
				.build();
	}
}