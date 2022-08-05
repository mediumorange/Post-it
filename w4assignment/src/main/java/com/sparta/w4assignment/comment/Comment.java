package com.sparta.w4assignment.comment;

import com.sparta.w4assignment.post.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity(name = "t_comment")
public class Comment extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "comment_key")
	private Long commentKey;

	@Column(name = "user_key", nullable = false)
	private Long userKey;
	@Column(name = "post_key", nullable = false)
	private Long postKey;

	@Column(name = "comment_content", nullable = false)
	private String commentContent;

	public Comment(CommentRequestDto requestDto, Long userKey) {
		this.userKey = userKey;
		this.postKey = requestDto.getPostKey();
		this.commentContent = requestDto.getCommentContent();
	}

	public void update(CommentRequestDto requestDto) {
		this.commentContent = requestDto.getCommentContent();
	}
}