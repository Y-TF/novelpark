package com.novelpark.presentation;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.novelpark.application.auth.AuthService;
import com.novelpark.presentation.dto.request.auth.SignupRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthController {

	private final AuthService authService;

	@PostMapping(value = "/signup", consumes = {
		MediaType.APPLICATION_JSON_VALUE,
		MediaType.MULTIPART_FORM_DATA_VALUE
	})
	public ResponseEntity<Void> signup(@Valid @RequestPart SignupRequest request, @RequestPart MultipartFile image) {
		authService.signup(request, image);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
