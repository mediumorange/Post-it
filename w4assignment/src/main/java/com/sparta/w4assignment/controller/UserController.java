package com.sparta.w4assignment.controller;

import com.sparta.w4assignment.jwt.TokenDto;
import com.sparta.w4assignment.jwt.TokenRequestDto;
import com.sparta.w4assignment.user.User;
import com.sparta.w4assignment.service.UserService;
import com.sparta.w4assignment.user.UserRepository;
import com.sparta.w4assignment.user.UserRequestDto;
import com.sparta.w4assignment.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RequiredArgsConstructor
@RestController
public class UserController {
	private final UserRepository userRepository;
	private final UserService userService;

	@GetMapping("/users/signup")
	public ModelAndView signup() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("signup");
		return modelAndView;
	}
	@GetMapping("/users/login")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	@PostMapping("/users/signup")
	public String creatUser(@RequestBody UserRequestDto requestDto) {
		userService.creatUser(requestDto);
		return "회원가입 성공";
	}

	@PostMapping("/users/login")
	public ResponseEntity<TokenDto> login(@RequestBody UserRequestDto requestDto) {
		TokenDto tokenDto = userService.login(requestDto);
		MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
		header.add("Access-Token", tokenDto.getAccessToken());
		header.add("Refresh-Token", tokenDto.getRefreshToken());
		header.add("Authorization", "Bearer " + tokenDto.getAccessToken());
		return new ResponseEntity<>(header, HttpStatus.OK);

//		TokenDto tokenDto = userService.login(requestDto);
//		return ResponseEntity.ok(tokenDto);
	}

	@GetMapping("/users/me")
	public ModelAndView getMyUserInfo() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main");
		UserResponseDto userInfo = userService.getMyInfo();
		String userId = userInfo.getUserId();
		Long userKey = userInfo.getUserKey();
		String userName = userInfo.getUserName();

		modelAndView.addObject("userId", userId);
		modelAndView.addObject("userKey", userKey);
		modelAndView.addObject("userName", userName);
		return modelAndView;
	}

	@GetMapping("/users/{key}")
	public ModelAndView getUserInfo(@PathVariable Long key) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main");
		UserResponseDto userInfo = userService.getUserInfo(key);
		String userId = userInfo.getUserId();
		Long userKey = userInfo.getUserKey();
		String userName = userInfo.getUserName();

		modelAndView.addObject("userId", userId);
		modelAndView.addObject("userKey", userKey);
		modelAndView.addObject("userName", userName);
		return modelAndView;
	}

	@PostMapping("/users/reissue")
	public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
		return ResponseEntity.ok(userService.reissue(tokenRequestDto));
	}

	@PatchMapping("/update/{userKey}")
	public String updateUser(@PathVariable Long userKey, @RequestBody UserRequestDto requestDto) {
		userService.update(userKey, requestDto);
		return "a";
	}
}