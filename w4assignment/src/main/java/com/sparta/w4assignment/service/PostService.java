package com.sparta.w4assignment.service;

import com.sparta.w4assignment.error.CustomException;
import com.sparta.w4assignment.error.ErrorCode;
import com.sparta.w4assignment.post.Post;
import com.sparta.w4assignment.post.PostRepository;
import com.sparta.w4assignment.post.PostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class PostService {

	private final PostRepository postRepository;

	public Post createPost(PostRequestDto requestDto, Long userKey) {
		Post post = new Post(requestDto, userKey);
		postRepository.save(post);
		return post;
	}

	@Transactional //DB에 꼬옥 반영해.주 ~
	public Long updatePost(Long postKey, Long myUserKey, PostRequestDto requestDto) {
		Post post = postRepository.findById(postKey).orElseThrow(
				() -> new CustomException(ErrorCode.NOT_FOUND_POST)
		);
		if(Objects.equals(myUserKey, post.getUserKey())) {
			post.update(requestDto);

		} else {
			throw new CustomException(ErrorCode.NOT_FOUND);
		}
		return post.getPostKey();
	}

	@Transactional
	public Long deletePost(Long postKey, Long myUserKey) {
		Post post = postRepository.findById(postKey).orElseThrow(
				() -> new CustomException(ErrorCode.NOT_FOUND_POST)
		);
		if(Objects.equals(myUserKey, post.getUserKey())) {
			postRepository.deleteById(postKey);

		} else {
			throw new CustomException(ErrorCode.NOT_FOUND);
		}
		return post.getPostKey();
	}
}