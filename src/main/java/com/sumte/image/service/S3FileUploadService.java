package com.sumte.image.service;

import java.net.URL;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3FileUploadService {

	private final AmazonS3 amazonS3;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Transactional
	public String generatePresignedUrl(String fileName, String contentType) {
		// 만료 시간 설정
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime() + (1000 * 60 * 5); // 5분
		expiration.setTime(expTimeMillis);

		// Presigned URL 요청 생성
		GeneratePresignedUrlRequest generatePresignedUrlRequest =
			new GeneratePresignedUrlRequest(bucket, fileName)
				.withMethod(HttpMethod.PUT)
				.withExpiration(expiration)
				.withContentType(contentType);

		// Presigned URL 생성
		URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}
}
