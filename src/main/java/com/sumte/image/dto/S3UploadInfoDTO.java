package com.sumte.image.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class S3UploadInfoDTO {
	private String originalName;   // 클라이언트가 보낸 원본 파일명
	private String imageUrl;       // S3에 저장될 이미지 URL
	private String presignedUrl;   // 해당 키로 PUT 요청할 URL
}
