package com.sparta.w3assignment.doamin;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Postit extends Timestamped {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;

	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private String memo;

	public Postit(PostitRequestDto requestDto) {
		this.username = requestDto.getUsername();
		this.password = requestDto.getPassword();
		this.title = requestDto.getTitle();
		this.memo = requestDto.getMemo();
	}

	public void update(PostitRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.memo = requestDto.getMemo();
	}
}