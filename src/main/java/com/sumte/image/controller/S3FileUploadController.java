package com.sumte.image.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.image.dto.S3UploadInfoDTO;
import com.sumte.image.entity.OwnerType;
import com.sumte.image.service.S3FileUploadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
		description = """
			- 여러 개의 파일명에 대해 Presigned URL을 일괄 생성하여 반환합니다. "
			- 이미지가 등록되는 파트에 대한 정보 OwnerType(GUESTHOUSE, ROOM, REVIEW)과 OwnerId(해당 파트의 ID)를 함께 전달해야 합니다.
			- presignedUrl로 파일 업로드를 진행할 수 있습니다.
			- imageUrl로 이미지 저장 API의 url 키로 매핑시키면 됩니다. (iamgeUrl은 S3에 저장된 이미지의 URL입니다.)
			"""
	)
	@GetMapping("/presigned-urls")
	public ResponseEntity<List<S3UploadInfoDTO>> generatePresignedUrls(
		@RequestParam(name = "fileNames", defaultValue = "sumte1, ouchlogo") List<String> fileNames,
		@Parameter(
			name = "ownerType",
			description = "이미지 소유자 타입",
			example = "ROOM",
			required = true,
			in = ParameterIn.QUERY
		) @RequestParam(name = "ownerType") OwnerType ownerType,
		@Parameter(
			name = "ownerId",
			description = "소유자 ID",
			example = "1",
			required = true,
			in = ParameterIn.QUERY
		) @RequestParam(name = "ownerId") Long ownerId
	) {
		List<S3UploadInfoDTO> s3UploadInfoDTO = s3FileUploadService.generatePresignedUrl(
			fileNames, ownerType, ownerId
		);
		return ResponseEntity.ok(s3UploadInfoDTO);
	}
}
