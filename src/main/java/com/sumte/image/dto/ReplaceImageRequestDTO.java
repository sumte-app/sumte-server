package com.sumte.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ReplaceImageRequestDTO",
	description = "이미지 전체 교체 요청 DTO (URL만 포함)")
public class ReplaceImageRequestDTO {

	@Schema(description = "S3 URL", example = "https://sumte-file.s3.ap-northeast-2.amazonaws.com/sumte.png")
	@NotBlank
	private String url;
}
