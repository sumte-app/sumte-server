package com.sumte.image.dto;

import com.sumte.image.entity.OwnerType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ImageResponseDTO", description = "이미지 메타데이터 조회/등록 응답 DTO")
public class ImageResponseDTO {

	@Schema(description = "이미지 고유 ID", example = "1")
	private Long id;

	@Schema(description = "이미지 URL 또는 object key", example = "https://sumte-file.s3.ap-northeast-2.amazonaws.com/sumte.png")
	private String url;

	@Schema(description = "정렬 순서 (1부터 시작)", example = "1")
	private Integer sortOrder;

	@Schema(description = "이미지 소유자 타입", example = "GUESTHOUSE")
	private OwnerType ownerType;

	@Schema(description = "소유자 ID (게스트하우스/룸/리뷰 ID)", example = "1")
	private Long ownerId;
}