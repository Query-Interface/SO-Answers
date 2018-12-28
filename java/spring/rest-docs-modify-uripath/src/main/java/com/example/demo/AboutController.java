package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AboutController {

	@GetMapping("/about")
    public ResponseEntity<String> listGithubRepositories() {
        return ResponseEntity.status(HttpStatus.OK).body("About 1.0");
	}
}
