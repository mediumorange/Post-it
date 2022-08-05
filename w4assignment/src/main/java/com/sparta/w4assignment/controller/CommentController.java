package com.sparta.w4assignment.controller;

import com.sparta.w4assignment.comment.Comment;
import com.sparta.w4assignment.comment.CommentRepository;
import com.sparta.w4assignment.comment.CommentRequestDto;
import com.sparta.w4assignment.security.UserDetailsImpl;
import com.sparta.w4assignment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {
	private final CommentRepository commentRepository;
	private final CommentService commentService;


	@GetMapping("/comments")
	public List<Comment> getComments() {
		return commentRepository.findAllByOrderByModifiedAtDesc();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/comments/comment")
	public Comment createComment(@RequestBody CommentRequestDto requestDto,
						   @AuthenticationPrincipal UserDetailsImpl userDetails) {
		Long userKey = userDetails.getUser().getUserKey();
		return commentService.createComment(requestDto, userKey);
	}

	@GetMapping("/comments/{commentKey}")
	public Comment getDetailComment(@PathVariable Long commentKey) {
		return commentRepository.findById(commentKey).orElse(null);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PatchMapping("/comments/{commentKey}")
	public Long updateComment(@PathVariable Long commentKey, @RequestBody CommentRequestDto requestDto) {
		commentService.update(commentKey, requestDto);
		return commentKey;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/comments/{commentKey}")
	public Long deleteComment(@PathVariable Long commentKey) {
		commentRepository.deleteById(commentKey);
		return commentKey;
	}
}