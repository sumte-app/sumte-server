package com.sumte.image.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.image.service.S3FileUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
@Tag(name = "S3 PresignedUrl API", description = "S3 PresignedUrl 발급 API")
public class S3FileUploadController {

	private final S3FileUploadService s3FileUploadService;

	//@AllUser
	@Operation(summary = "PresignedUrl 발급", description = "이미지, 파일 업로드를 위한 PresignedUrl을 발급합니다.")
	@GetMapping("/presigned-url")
	public ResponseEntity<String> generatePresignedUrl(@RequestParam String fileName,
		@RequestParam String contentType) {
		String presignedUrl = s3FileUploadService.generatePresignedUrl(fileName, contentType);
		return ResponseEntity.ok(presignedUrl);
	}
}
