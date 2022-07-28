package com.sparta.w3assignment.service;

import com.sparta.w3assignment.doamin.Postit;
import com.sparta.w3assignment.doamin.PostitRepository;
import com.sparta.w3assignment.doamin.PostitRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostitService {

	private final PostitRepository postitRepository;

	@Transactional //DB에 꼬옥 반영해.주 ~
	public Long update(Long id, PostitRequestDto requestDto) {
		Postit postit = postitRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
		);
		postit.update(requestDto);
		return postit.getId();
	}
}
