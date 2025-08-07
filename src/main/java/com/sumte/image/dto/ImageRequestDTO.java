package com.sumte.image.dto;

import com.sumte.image.entity.OwnerType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ImageRequestDTO", description = "이미지 메타데이터 등록 요청 DTO")
public class ImageRequestDTO {

	@Schema(description = "이미지 소유자 타입", example = "GUESTHOUSE")
	@NotNull
	private OwnerType ownerType;

	@Schema(description = "소유자 ID (예: 게스트하우스 ID, 룸 ID, 리뷰 ID 등)", example = "1")
	@NotNull
	private Long ownerId;

	@Schema(description = "S3에 업로드된 이미지 URL", example = "https://sumte-file.s3.ap-northeast-2.amazonaws.com/sumte.png")
	@NotBlank
	private String url;

	// @Schema(description = "정렬 순서 (0부터 시작)", example = "1")
	// private Integer sortOrder;
}
