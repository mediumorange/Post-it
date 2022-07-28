package com.sparta.w3assignment.doamin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostitRepository extends JpaRepository<Postit, Long> {
	List<Postit> findAllByOrderByModifiedAtDesc();
}