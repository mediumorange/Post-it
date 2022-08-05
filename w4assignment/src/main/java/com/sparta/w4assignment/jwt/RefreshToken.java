package com.sparta.w4assignment.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import java.time.Duration;

//@Service
//@RequiredArgsConstructor
//public class RefreshToken {
//	private RedisTemplate<String, String> redisTemplate;
//
//	public void setData(String key, String value){
//		ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
//		valueOperations.set(key,value);
//	}
//	public String getValue(String key) {
//		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//		return valueOperations.get(key);
//	}
//
//	public void updateData(String key, String value) {
//		redisTemplate.delete(key);
//		ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
//		valueOperations.set(key,value);
//	}
//}
@Getter
@NoArgsConstructor
@RedisHash("refresh_token")
public class RefreshToken {
	@Id
	private String key;

	@Indexed
	private String value;

	@Builder
	public RefreshToken(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public RefreshToken updateValue(String token) {
		this.value = token;
		return this;
	}
}