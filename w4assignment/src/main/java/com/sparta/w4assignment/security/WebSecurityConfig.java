package com.sparta.w4assignment.security;

import com.sparta.w4assignment.jwt.JwtAccessDeniedHandler;
import com.sparta.w4assignment.jwt.JwtAuthenticationEntryPoint;
import com.sparta.w4assignment.jwt.JwtSecurityConfig;
import com.sparta.w4assignment.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final TokenProvider tokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}

	// h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		// h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
		return (web) -> web.ignoring()
				.antMatchers("/h2-console/**");
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// CSRF 설정 Disable
		http.csrf().disable();

		http
				.authorizeHttpRequests((authz) -> authz
						// static 허용
						.antMatchers("/images/**").permitAll()
						.antMatchers("/css/**").permitAll()
						.antMatchers("/js/**").permitAll()
						// users API 허용
						.antMatchers("/users/**").permitAll()
						// posts API 허용
						.antMatchers("/posts/**").permitAll()
						// comments API 허용
						.antMatchers("/comments/**").permitAll()
						// 특정 허용
						.antMatchers("/main").permitAll()
						// 어떤 요청이든 인증
						.anyRequest().authenticated()

				)
				.logout()
				.logoutUrl("/users/logout")
				.logoutSuccessUrl("/users/login")
				.deleteCookies("accessToken", "refreshToken")

				// exception handling 할 때 우리가 만든 클래스를 추가
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.accessDeniedHandler(jwtAccessDeniedHandler)

				// h2-console 을 위한 설정을 추가
				.and()
				.headers()
				.frameOptions()
				.sameOrigin()

				// 시큐리티는 기본적으로 세션을 사용
				// 세션 설정을 Stateless 로 설정
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				// JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
				.and()
				.apply(new JwtSecurityConfig(tokenProvider));

		return http.build();
	}
}