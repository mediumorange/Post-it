package com.sparta.w4assignment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RestController
public class MainController {
	ModelAndView modelAndView = new ModelAndView();

	@GetMapping("/main")
	public ModelAndView main() {
		modelAndView.setViewName("main");
		return modelAndView;
	}
}