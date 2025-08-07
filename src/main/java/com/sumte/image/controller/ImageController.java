package com.sumte.image.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.image.dto.ImageRequestDTO;
import com.sumte.image.dto.ImageResponseDTO;
import com.sumte.image.dto.ReplaceImageRequestDTO;
import com.sumte.image.entity.OwnerType;
import com.sumte.image.service.ImageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
@Tag(name = "이미지 API", description = "이미지 메타데이터 저장·조회·교체 API 및 S3 PresignedUrl 발급 API")
public class ImageController {
	private final ImageService imageService;

	@Operation(summary = "이미지 일괄 저장",
		description = """
			- 여러 이미지 메타데이터를 한 번에 저장합니다.
			- 이미지가 등록되는 파트에 대한 정보 OwnerType(GUESTHOUSE, ROOM 등)과
			OwnerId(해당 파트의 ID)를 함께 전달해야 합니다.
			- 요청 리스트 순서대로 서버에서 sortOrder(이미지 순서)가 1부터 자동 부여됩니다.
			""")
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		description = "등록할 이미지 리스트를 전달합니다. 서버가 순서를 1부터 자동 부여합니다.",
		required = true,
		content = @Content(
			mediaType = "application/json",
			array = @ArraySchema(
				schema = @Schema(implementation = ImageRequestDTO.class)
			),
			examples = {
				@ExampleObject(
					name = "Image Batch Upload Example",
					value = """
						[
						  {
							"ownerType": "GUESTHOUSE",
							"ownerId": 1,
							"url": "https://sumte-file.s3.ap-northeast-2.amazonaws.com/sumte1.png"
						  },
						  {
							"ownerType": "GUESTHOUSE",
							"ownerId": 1,
							"url": "https://sumte-file.s3.ap-northeast-2.amazonaws.com/sumte2.png"
						  },
						  {
							"ownerType": "GUESTHOUSE",
							"ownerId": 1,
							"url": "https://sumte-file.s3.ap-northeast-2.amazonaws.com/sumte3.png"
						  }
						]
						"""
				)
			}
		)
	)
	@PostMapping
	public ResponseEntity<List<ImageResponseDTO>> saveImagesBatch(

		@Valid @RequestBody List<ImageRequestDTO> imageRequestDTOS) {

		var savedImageList = imageService.saveAllImages(imageRequestDTOS);
		var imageResponseDTOS = savedImageList.stream()
			.map(img -> new ImageResponseDTO(
				img.getId(), img.getUrl(), img.getSortOrder(), img.getOwnerType(), img.getOwnerId()))
			.collect(Collectors.toList());

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(imageResponseDTOS);
	}

	@Operation(
		summary = "이미지 전체 교체",
		description = """
			  주어진 ownerType/ownerId 에 등록된 모든 이미지를 삭제하고,
			  요청 리스트 순서대로 새 이미지를 1부터 순차 저장합니다.
			  - S3 객체도 함께 삭제됩니다.
			""")
	@PutMapping("/{ownerType}/{ownerId}")
	public ResponseEntity<List<ImageResponseDTO>> replaceImages(
		@PathVariable OwnerType ownerType,
		@PathVariable Long ownerId,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(
			description = "전체 교체할 이미지 리스트를 전달합니다.",
			required = true,
			content = @Content(
				mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = ReplaceImageRequestDTO.class)),
				examples = {
					@ExampleObject(
						name = "Replace Example",
						value = """
							[
							  { "url":"https://sumte-file.s3.ap-northeast-2.amazonaws.com/sumte1.png" },
							  { "url":"https://sumte-file.s3.ap-northeast-2.amazonaws.com/sumte2.png" },
							  { "url":"https://sumte-file.s3.ap-northeast-2.amazonaws.com/sumte3.png" }
							]
							"""
					)
				}
			)
		)
		@Valid @RequestBody List<ReplaceImageRequestDTO> replaceImageDtos
	) {
		List<ImageRequestDTO> requests = replaceImageDtos.stream()
			.map(r -> new ImageRequestDTO(ownerType, ownerId, r.getUrl()))
			.collect(Collectors.toList());

		var replacedImages = imageService.replaceImages(ownerType, ownerId, requests);
		var imageResponseDTOS = replacedImages.stream()
			.map(img -> new ImageResponseDTO(
				img.getId(),
				img.getUrl(),
				img.getSortOrder(),
				img.getOwnerType(),
				img.getOwnerId()
			))
			.collect(Collectors.toList());

		return ResponseEntity.ok(imageResponseDTOS);
	}

	@Operation(summary = "이미지 목록 조회", description = "주어진 ownerType, ownerId 에 매핑된 이미지 리스트를 정렬순으로 조회합니다.")
	@GetMapping
	public ResponseEntity<List<ImageResponseDTO>> getImages(
		@Parameter(
			name = "ownerType",
			description = "이미지 소유자 타입",
			example = "ROOM",
			required = true,
			in = ParameterIn.QUERY
		) @RequestParam OwnerType ownerType,
		@Parameter(
			name = "ownerId",
			description = "소유자 ID",
			example = "1",
			required = true,
			in = ParameterIn.QUERY
		)
		@RequestParam Long ownerId) {
		var imageList = imageService.getImagesByOwner(ownerType, ownerId);
		var imageResponseDTOS = imageList.stream()
			.map(img -> new ImageResponseDTO(
				img.getId(), img.getUrl(), img.getSortOrder(), img.getOwnerType(), img.getOwnerId()))
			.collect(Collectors.toList());
		return ResponseEntity.ok(imageResponseDTOS);
	}
}
