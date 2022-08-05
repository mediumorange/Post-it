package com.sparta.w4assignment.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@RequiredArgsConstructor
public class UserRequestDto {
	private final String userId;
	private final String userName;
	private final String userPassword;
	public boolean admin = false;
	private final String adminToken = "";

	public UsernamePasswordAuthenticationToken toAuthentication() {
		return new UsernamePasswordAuthenticationToken(userId, userPassword);
	}
}