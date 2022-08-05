package com.sparta.w4assignment.controller;

import com.sparta.w4assignment.post.Post;
import com.sparta.w4assignment.post.PostRepository;
import com.sparta.w4assignment.post.PostRequestDto;
import com.sparta.w4assignment.service.PostService;
import com.sparta.w4assignment.service.UserService;
import com.sparta.w4assignment.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {
	private final PostRepository postRepository;
	private final PostService postService;
	private final UserService userService;


	@GetMapping("/posts")
	public List<Post> getPosts() {
		return postRepository.findAllByOrderByModifiedAtDesc();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/posts/post")
	public Post createPost(@RequestBody PostRequestDto requestDto) {
		UserResponseDto userInfo = userService.getMyInfo();
		Long userKey = userInfo.getUserKey();
		return postService.createPost(requestDto, userKey);
	}

	@GetMapping("/posts/{postKey}")
	public Post getDetailPost(@PathVariable Long postKey) {
		return postRepository.findById(postKey).orElse(null);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PatchMapping("/posts/{postKey}/{myUserKey}")
	public Long updatePost(@PathVariable Long postKey, @PathVariable Long myUserKey, @RequestBody PostRequestDto requestDto) {
		postService.updatePost(postKey, myUserKey, requestDto);
		return postKey;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/posts/{postKey}/{myUserKey}")
	public Long deletePost(@PathVariable Long postKey, @PathVariable Long myUserKey) {
		postService.deletePost(postKey, myUserKey);
		return postKey;
	}
}