package com.sparta.w4assignment.service;

import com.sparta.w4assignment.error.CustomException;
import com.sparta.w4assignment.error.ErrorCode;
import com.sparta.w4assignment.jwt.*;
import com.sparta.w4assignment.security.SecurityUtil;
import com.sparta.w4assignment.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final TokenProvider tokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	@Autowired
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
					   AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.tokenProvider = tokenProvider;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	public void creatUser(@Value("${ADMIN_TOKEN}") UserRequestDto requestDto) {
		String userId = requestDto.getUserId();

		// 회원 ID 중복 확인
		Optional<User> found = userRepository.findByUserId(userId);
		if (found.isPresent()) {
			throw new CustomException(ErrorCode.ID_DUPLICATION);
		}
		// 사용자 ROLE 확인
		Authority userRole = Authority.ROLE_USER;
		if (requestDto.isAdmin()) {
			if (!requestDto.getAdminToken().equals("${ADMIN_TOKEN}")) {
				throw new CustomException(ErrorCode.ADMIN_TOKEN_MISMATCHED);
			}
			userRole = Authority.ROLE_ADMIN;
		}
		String userPassword = passwordEncoder.encode(requestDto.getUserPassword());
		String userName = requestDto.getUserName();
		User user = new User(userId, userName ,userPassword, userRole);
		userRepository.save(user);
	}

	@Transactional
	public TokenDto login(UserRequestDto requestDto) {

		// 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
		UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();

		// 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
		//    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		// 3. 인증 정보를 기반으로 JWT 토큰 생성
		TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

		// 4. RefreshToken 저장
//		RefreshToken refreshToken = new RefreshToken();
//			refreshToken.setData(authentication.getName(), tokenDto.getRefreshToken());
		RefreshToken refreshToken = RefreshToken.builder()
				.key(authentication.getName())
				.value(tokenDto.getRefreshToken())
				.build();

		refreshTokenRepository.save(refreshToken);

		// 5. 토큰 발급
		return tokenDto;
	}

	@Transactional
	public TokenDto reissue(TokenRequestDto tokenRequestDto) {
		// 1. Refresh Token 검증
		if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
			throw new CustomException(ErrorCode.WRONG_REFRESH_TOKEN);
		}

		// 2. Access Token 에서 userId 가져오기
		Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

		// 3. 저장소에서 userId 를 기반으로 Refresh Token 값 가져옴
		RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
				.orElseThrow(() -> new CustomException(ErrorCode.LOGGED_OUT_USER));

		// 4. Refresh Token 일치하는지 검사
		if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
			throw new CustomException(ErrorCode.MISMATCHED_TOKEN);
		}

		// 5. 새로운 토큰 생성
		TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

		// 6. 저장소 정보 업데이트
//		refreshToken.updateData(authentication.getName(), tokenDto.getRefreshToken());
		RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());

		refreshTokenRepository.save(newRefreshToken);

		// 토큰 발급
		return tokenDto;
	}

	@Transactional
	public Long update(Long userKey, UserRequestDto requestDto) {
		User user = userRepository.findById(userKey).orElseThrow(
				() -> new CustomException(ErrorCode.NOT_FOUND_USERID)
		);
		user.update(requestDto);
		return user.getUserKey();
	}

	@Transactional(readOnly = true)
	public UserResponseDto getUserInfo(Long userKey) {
		return userRepository.findById(userKey)
				.map(UserResponseDto::of)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
	}

	// 현재 SecurityContext 에 있는 유저 정보 가져오기
	@Transactional(readOnly = true)
	public UserResponseDto getMyInfo() {
		return userRepository.findByUserId(SecurityUtil.getCurrentUserId())
				.map(UserResponseDto::of)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ME));
	}
}