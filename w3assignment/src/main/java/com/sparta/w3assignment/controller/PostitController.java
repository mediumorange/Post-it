package com.sparta.w3assignment.controller;

import com.sparta.w3assignment.doamin.Postit;
import com.sparta.w3assignment.doamin.PostitRepository;
import com.sparta.w3assignment.doamin.PostitRequestDto;
import com.sparta.w3assignment.service.PostitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class PostitController {
	private final PostitRepository postitRepository;
	private final PostitService postitService;


	@PostMapping("/api/postit")
	public Postit createPostit(@RequestBody PostitRequestDto requestDto) {
		Postit postit = new Postit(requestDto);
		return postitRepository.save(postit);
	}

	@GetMapping("/api/postit")
	public List<Postit> getPostits() {
		return postitRepository.findAllByOrderByModifiedAtDesc();
	}

	@GetMapping("/api/postit/{id}")
	public Postit detailPostit(@PathVariable Long id) {
		return postitRepository.findById(id).orElse(null);
	}

	@GetMapping("api/postit/{id}/{password}")
	public Boolean  checkPassword(@PathVariable Long id, @PathVariable String password) {
		Postit a = postitRepository.findById(id).orElse(null);
		return Objects.equals(Objects.requireNonNull(a).getPassword(), password);
	}

	@PatchMapping("/api/postit/{id}")
	public Long updatePostit(@PathVariable Long id, @RequestBody PostitRequestDto requestDto) {
		postitService.update(id, requestDto);
		return id;
	}

	@DeleteMapping("/api/postit/{id}")
	public Long deletePostit(@PathVariable Long id) {
		postitRepository.deleteById(id);
		return id;
	}
}