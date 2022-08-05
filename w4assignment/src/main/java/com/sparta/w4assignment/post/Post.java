package com.sparta.w4assignment.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity(name = "t_post")
public class Post extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "post_key")
	private Long postKey;

	@Column(name = "user_key", nullable = false)
	private Long userKey;
	@Column(name = "post_title", nullable = false)
	private String postTitle;
	@Column(name = "post_content", nullable = false)
	private String postContent;

	public Post(PostRequestDto requestDto, Long userKey) {
		this.userKey = userKey;
		this.postTitle = requestDto.getPostTitle();
		this.postContent = requestDto.getPostContent();
	}

	public void update(PostRequestDto requestDto) {
		this.postTitle = requestDto.getPostTitle();
		this.postContent = requestDto.getPostContent();
	}
}