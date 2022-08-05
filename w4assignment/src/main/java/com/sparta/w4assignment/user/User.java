package com.sparta.w4assignment.user;

import com.sparta.w4assignment.post.PostRequestDto;
import com.sparta.w4assignment.post.Timestamped;
import com.sparta.w4assignment.user.UserRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity(name = "t_user")
public class User extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_key")
	private Long userKey;

	@Column(name = "user_id", nullable = false, unique = true)
	private String userId;
	@Column(name = "user_name", nullable = false)
	private String userName;
	@Column(name = "user_password", nullable = false)
	private String userPassword;

	@Column(name = "user_role", nullable = false)
	@Enumerated(EnumType.STRING)
	private Authority userRole;

	public User(String userId, String userName, String userPassword, Authority userRole) {
		this.userId = userId;
		this.userName = userName;
		this.userPassword = userPassword;
		this.userRole = userRole;
	}

	public void update(UserRequestDto requestDto) {
		this.userName = requestDto.getUserName();
		this.userPassword = requestDto.getUserPassword();
	}
}