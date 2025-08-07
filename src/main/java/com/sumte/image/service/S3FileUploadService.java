package com.sumte.image.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.sumte.image.dto.S3UploadInfoDTO;
import com.sumte.image.entity.OwnerType;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3FileUploadService {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.url-prefix}")
	private String s3Endpoint;

	@Transactional
	public List<S3UploadInfoDTO> generatePresignedUrl(List<String> originalFileNames,
		OwnerType ownerType,
		Long ownerId /*, String contentType*/) {

		// 만료 시간 설정
		Date expiration = new Date(System.currentTimeMillis() + 5 * 60 * 1000);

		// yyyy/MM/dd 형태의 날짜 경로
		String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		return originalFileNames.stream()
			.map(original -> {
				// 1) 확장자 분리
				String ext = "";
				int idx = original.lastIndexOf('.');
				if (idx >= 0) {
					ext = original.substring(idx);
				}

				// 2) UUID 조합 및 전체 키 생성
				String uuid = UUID.randomUUID().toString();

				String key = "images/" + ownerType + "/"
					+ ownerType + "_" + ownerId + "_" + datePath + "_" + uuid + ext;

				// Presigned URL 요청 생성
				GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucket, key)
					.withMethod(HttpMethod.PUT)
					.withExpiration(expiration);

				// 이미지 객체 URL 생성
				String fullUrl = s3Endpoint + "/" + key;

				// Presigned URL 생성
				String presignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();

				return new S3UploadInfoDTO(original, fullUrl, presignedUrl);

			})
			.collect(Collectors.toList());
	}
}
