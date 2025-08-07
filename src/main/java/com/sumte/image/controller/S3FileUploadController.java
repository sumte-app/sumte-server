package com.sumte.image.controller;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
@Tag(name = "이미지 API", description = "이미지 메타데이터 저장·조회·교체 API 및 S3 PresignedUrl 발급 API")
public class S3FileUploadController {

	private final S3FileUploadService s3FileUploadService;

	/*
	@Operation(summary = "PresignedUrl 발급", description = "이미지, 파일 업로드를 위한 PresignedUrl을 발급합니다.")
	@GetMapping("/presigned-url")
	public ResponseEntity<String> generatePresignedUrl(@RequestParam String fileName,
		@RequestParam String contentType) {
		String presignedUrl = s3FileUploadService.generatePresignedUrl(fileName, contentType);
		return ResponseEntity.ok(presignedUrl);
	}
	 */

	@Operation(
		summary = "Presigned URLs 일괄 발급",
		description = "여러 개의 파일명에 대해 Presigned URL을 일괄 생성하여 반환합니다."
	)
	@GetMapping("/presigned-urls")
	public ResponseEntity<Map<String, String>> generatePresignedUrls(
		@RequestParam(name = "fileNames", defaultValue = "sumte1, ouchlogo") List<String> fileNames
	) {
		Map<String, String> presignedUrls = fileNames.stream()
			.collect(Collectors.toMap(
				Function.identity(),
				key -> s3FileUploadService.generatePresignedUrl(key)
			));
		return ResponseEntity.ok(presignedUrls);
	}
}
