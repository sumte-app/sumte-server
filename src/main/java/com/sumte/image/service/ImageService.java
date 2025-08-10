package com.sumte.image.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.sumte.apiPayload.code.error.ImageErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.guesthouse.repository.GuesthouseRepository;
import com.sumte.image.dto.ImageRequestDTO;
import com.sumte.image.entity.Image;
import com.sumte.image.entity.OwnerType;
import com.sumte.image.repository.ImageRepository;
import com.sumte.review.repository.ReviewRepository;
import com.sumte.room.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final ImageRepository imageRepository;
	private final GuesthouseRepository guesthouseRepository;
	private final RoomRepository roomRepository;
	private final ReviewRepository reviewRepository;
	private final AmazonS3 amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	@Value("${cloud.aws.s3.url-prefix}")
	private String s3UrlPrefix;

	@Transactional
	public List<Image> saveAllImages(List<ImageRequestDTO> dtos) {
		// 1) 같은 ownerType+ownerId 그룹핑
		Map<Pair<OwnerType, Long>, List<ImageRequestDTO>> groups = dtos.stream()
			.collect(Collectors.groupingBy(
				dto -> Pair.of(dto.getOwnerType(), dto.getOwnerId())
			));

		List<Image> toSave = new ArrayList<>();
		for (var entry : groups.entrySet()) {
			OwnerType ownerType = entry.getKey().getFirst();
			Long ownerId = entry.getKey().getSecond();

			List<ImageRequestDTO> list = entry.getValue();

			// 2) 동일 owner에 이미지 이미 등록되어 있는지 검사
			boolean existsImages = imageRepository.existsByOwnerTypeAndOwnerId(ownerType, ownerId);
			if (existsImages) {
				throw new SumteException(ImageErrorCode.IMAGE_ALREADY_EXISTS);
			}

			// 3) 실제 엔티티 존재 여부 검증
			validateOwnerExists(ownerType, ownerId);

			// 4) 그룹별로 DB에서 현재 최대 sortOrder 조회 → +1 부터
			int nextOrder = imageRepository
				.findMaxSortOrder(ownerType, ownerId)
				.orElse(0) + 1;

			// 5) 그룹 내 DTO 순서대로 sortOrder 할당
			for (ImageRequestDTO dto : list) {
				toSave.add(Image.builder()
					.ownerType(ownerType)
					.ownerId(ownerId)
					.url(dto.getUrl())
					.sortOrder(nextOrder++)
					.build()
				);
			}
		}
		return imageRepository.saveAll(toSave);
	}

	/**
	 * 전체 교체: 기존 이미지 DB 레코드와 S3 오브젝트를 전부 삭제하고,
	 * 전달된 URL 순서대로 새 이미지를 1부터 순차 저장합니다.
	 */
	@Transactional
	public List<Image> replaceImages(OwnerType ownerType, Long ownerId, List<ImageRequestDTO> imageRequestDTOS) {
		// 1) 소유자 존재 검증
		validateOwnerExists(ownerType, ownerId);

		// 2) 기존 DB 레코드 조회
		List<Image> existing = imageRepository
			.findByOwnerTypeAndOwnerIdOrderBySortOrderAsc(ownerType, ownerId);

		if (!existing.isEmpty()) {
			// 3) S3 객체 키 수집 & 삭제
			List<DeleteObjectsRequest.KeyVersion> keys = existing.stream()
				.map(Image::getUrl)
				// 3-1) full URL 에서 prefix 제거 → object key만 남김
				.filter(url -> url.startsWith(s3UrlPrefix + "/"))
				.map(url -> url.substring((s3UrlPrefix + "/").length()))
				.map(DeleteObjectsRequest.KeyVersion::new)
				.collect(Collectors.toList());

			if (!keys.isEmpty()) {
				DeleteObjectsRequest delReq = new DeleteObjectsRequest(bucketName)
					.withKeys(keys);
				amazonS3Client.deleteObjects(delReq);
			}
			// 4) DB 레코드 삭제
			imageRepository.deleteAllInBatch(existing);
		}

		// 5) 새 DTO 리스트 순서대로 1부터 부여
		List<Image> toSave = new ArrayList<>(imageRequestDTOS.size());
		for (int i = 0; i < imageRequestDTOS.size(); i++) {
			ImageRequestDTO dto = imageRequestDTOS.get(i);
			toSave.add(Image.builder()
				.ownerType(ownerType)
				.ownerId(ownerId)
				.url(dto.getUrl())
				.sortOrder(i + 1)
				.build()
			);
		}

		// 6) 저장 후 반환
		return imageRepository.saveAll(toSave);
	}

	private void validateOwnerExists(OwnerType ownerType, Long ownerId) {
		switch (ownerType) {
			case GUESTHOUSE -> {
				if (!guesthouseRepository.existsById(ownerId)) {
					throw new SumteException(ImageErrorCode.GUESTHOUSE_NOT_FOUND);
				}
			}
			case ROOM -> {
				if (!roomRepository.existsById(ownerId)) {
					throw new SumteException(ImageErrorCode.ROOM_NOT_FOUND);
				}
			}
			case REVIEW -> {
				if (!reviewRepository.existsById(ownerId)) {
					throw new SumteException(ImageErrorCode.REVIEW_NOT_FOUND);
				}
			}
			default -> throw new SumteException(ImageErrorCode.INVALID_OWNER_TYPE);
		}
	}

	public List<Image> getImagesByOwner(OwnerType ownerType, Long ownerId) {
		return imageRepository.findByOwnerTypeAndOwnerIdOrderBySortOrderAsc(ownerType, ownerId);
	}
}
