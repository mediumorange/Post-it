package com.sparta.w4assignment.service;

import com.sparta.w4assignment.error.CustomException;
import com.sparta.w4assignment.error.ErrorCode;
import com.sparta.w4assignment.comment.Comment;
import com.sparta.w4assignment.comment.CommentRepository;
import com.sparta.w4assignment.comment.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final CommentRepository commentRepository;

	public Comment createComment(CommentRequestDto requestDto, Long userKey) {
		Comment comment = new Comment(requestDto, userKey);
		commentRepository.save(comment);
		return comment;
	}

	@Transactional //DB에 꼬옥 반영해.주 ~
	public Long update(Long commentKey, CommentRequestDto requestDto) {
		Comment comment = commentRepository.findById(commentKey).orElseThrow(
				() -> new CustomException(ErrorCode.NOT_FOUND_COMMENT)
		);
		comment.update(requestDto);
		return comment.getCommentKey();
	}
}