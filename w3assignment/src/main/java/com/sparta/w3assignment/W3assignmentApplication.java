package com.sparta.w3assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class W3assignmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(W3assignmentApplication.class, args);
	}

}