package com.sumte.guesthouse.service;

import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.guesthouse.dto.GuesthouseDetailDTO;
import com.sumte.guesthouse.dto.GuesthousePreviewDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;
import com.sumte.guesthouse.entity.AdType;
import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.repository.GuesthouseOptionServicesRepository;
import com.sumte.guesthouse.repository.GuesthouseRepository;
import com.sumte.guesthouse.repository.GuesthouseRepositoryCustom;
import com.sumte.guesthouse.repository.GuesthouseTargetAudienceRepository;
import com.sumte.image.entity.Image;
import com.sumte.image.entity.OwnerType;
import com.sumte.image.repository.ImageRepository;
import com.sumte.review.repository.ReviewRepository;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.entity.Room;
import com.sumte.room.repository.RoomRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuesthouseQueryServiceImpl implements GuesthouseQueryService {

	private final GuesthouseRepository guesthouseRepository;
	private final RoomRepository roomRepository;
	private final ReviewRepository reviewRepository;
	private final GuesthouseTargetAudienceRepository guesthouseTargetAudienceRepository;
	private final GuesthouseOptionServicesRepository guesthouseOptionServicesRepository;
	private final GuesthouseRepositoryCustom guesthouseRepositoryCustom;
	private final ImageRepository imageRepository;

	@Override
	@Transactional
	public GuesthouseDetailDTO getHouseById(Long guesthouseId) {
		// 1) 기본 정보 로드
		Guesthouse gh = guesthouseRepository.findById(guesthouseId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.NOT_EXIST));

		// 2) 옵션·타깃
		List<String> targetAudiences = guesthouseTargetAudienceRepository
			.findTargetAudienceNamesByGuesthouseId(guesthouseId);
		List<String> optionServices = guesthouseOptionServicesRepository
			.findTargetAudienceNamesByGuesthouseId(guesthouseId);

		// 3) **게스트하우스가 가진 모든 이미지** 조회 & URL 리스트 변환
		List<String> ghImageUrls = imageRepository
			.findByOwnerTypeAndOwnerIdOrderBySortOrderAsc(OwnerType.GUESTHOUSE, guesthouseId)
			.stream()
			.map(Image::getUrl)
			.toList();

		// 4) 각 Room 정보 + 첫 번째 이미지
		List<Room> rooms = gh.getRooms();
		List<Long> roomIds = rooms.stream().map(Room::getId).toList();

		// 4-a) 객실 이미지 일괄 조회
		List<Image> roomImages = imageRepository
			.findByOwnerTypeAndOwnerIdInOrderByOwnerIdAscSortOrderAsc(OwnerType.ROOM, roomIds);

		// 4-b) 방 ID 별 첫 장 URL
		Map<Long, String> firstImageByRoom = roomImages.stream()
			.collect(Collectors.groupingBy(
				Image::getOwnerId,
				LinkedHashMap::new,
				Collectors.mapping(Image::getUrl, Collectors.toList())
			))
			.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> e.getValue().get(0)
			));

		// 4-c) RoomResponseDTO 리스트 생성
		List<RoomResponseDTO.GetRoomResponse> roomDtos = rooms.stream()
			.map(room -> RoomResponseDTO.GetRoomResponse.builder()
				.id(room.getId())
				.name(room.getName())
				.content(room.getContents())
				.price(room.getPrice())
				.checkin(room.getCheckin())
				.checkout(room.getCheckout())
				.standardCount(room.getStandardCount())
				.totalCount(room.getTotalCount())
				.imageUrl(firstImageByRoom.get(room.getId()))   // 각 방 첫 장 이미지
				.build())
			.toList();

		// 5) 최종 DTO 조립
		return GuesthouseDetailDTO.builder()
			.id(gh.getId())
			.name(gh.getName())
			.addressRegion(gh.getAddressRegion())
			.addressDetail(gh.getAddressDetail())
			.information(gh.getInformation())
			.advertisement(gh.getAdvertisement())
			.optionServices(optionServices)
			.targetAudience(targetAudiences)
			.imageUrls(ghImageUrls)      // 모든 게스트하우스 이미지
			.rooms(roomDtos)             // 각 방 + 첫 장 이미지
			.build();
	}

	@Override
	@Transactional
	public Slice<GuesthouseResponseDTO.HomeSummary> getGuesthousesForHome(Pageable pageable) {
		// 1) 페이징 혹은 슬라이스 조회
		Slice<Guesthouse> slice = guesthouseRepository.findAllOrderedForHome(pageable);
		List<Guesthouse> ghList = slice.getContent();
		if (ghList.isEmpty()) {
			return new SliceImpl<>(List.of(), pageable, false);
		}

		// 2) ID 리스트 & 이미지 조회
		List<Long> ghIds = ghList.stream().map(Guesthouse::getId).toList();
		List<Image> images = imageRepository
			.findByOwnerTypeAndOwnerIdInOrderByOwnerIdAscSortOrderAsc(
				OwnerType.GUESTHOUSE, ghIds
			);
		Map<Long, String> firstImageByGh = images.stream()
			.collect(Collectors.groupingBy(
				Image::getOwnerId,
				LinkedHashMap::new,
				Collectors.mapping(Image::getUrl, Collectors.toList())
			))
			.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> e.getValue().get(0)
			));

		// 3) DTO 변환
		List<GuesthouseResponseDTO.HomeSummary> summaries = ghList.stream()
			.map(gh -> GuesthouseResponseDTO.HomeSummary.builder()
				.guestHouseId(gh.getId())
				.name(gh.getName())
				.addressRegion(gh.getAddressRegion())
				.imageUrl(firstImageByGh.getOrDefault(gh.getId(), null))
				.averageScore(
					Optional.ofNullable(
						reviewRepository.findAverageScoreByGuesthouseId(gh.getId())
					).orElse(0.0)
				)
				.reviewCount(
					reviewRepository.countByGuesthouseId(gh.getId())
				)
				.checkInTime(
					Optional.ofNullable(
						roomRepository.findEarliestCheckinByGuesthouseId(gh.getId())
					).orElse("00:00")
				)
				.minPrice(
					Optional.ofNullable(
						roomRepository.findMinPriceByGuesthouseId(gh.getId())
					).orElse(0L)
				)
				.isAd(gh.getAdvertisement() == AdType.AD)
				.build()
			)
			.toList();

		// 4) SliceImpl 생성해 반환
		return new SliceImpl<>(
			summaries,
			pageable,
			slice.hasNext()
		);
	}

	@Override
	@Transactional
	public Page<GuesthousePreviewDTO> getFilteredGuesthouse(GuesthouseSearchRequestDTO dto, Pageable pageable) {
		// 1) 필터링된 게스트하우스 페이징 조회
		Page<Guesthouse> page = guesthouseRepositoryCustom.searchFiltered(dto, pageable);
		List<Guesthouse> ghList = page.getContent();
		if (ghList.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, 0);
		}

		// 2) 조회된 게스트하우스 ID들
		List<Long> ghIds = ghList.stream()
			.map(Guesthouse::getId)
			.toList();

		// 3) 이미지 일괄 조회 (N+1 방지)
		List<Image> images = imageRepository
			.findByOwnerTypeAndOwnerIdInOrderByOwnerIdAscSortOrderAsc(
				OwnerType.GUESTHOUSE, ghIds
			);

		// 4) ownerId 별로 첫 번째 URL만 뽑아서 맵으로 저장
		Map<Long, String> firstImageByGh = images.stream()
			.collect(Collectors.groupingBy(
				Image::getOwnerId,
				LinkedHashMap::new,                         // key 순서 보장(Optional)
				Collectors.mapping(Image::getUrl, Collectors.toList())
			))
			.entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				e -> e.getValue().get(0)                  // 리스트의 첫 번째 URL
			));

		// 5) DTO 변환
		List<GuesthousePreviewDTO> dtos = ghList.stream()
			.map(gh -> GuesthousePreviewDTO.builder()
				.id(gh.getId())
				.name(gh.getName())
				.averageScore(
					Optional.ofNullable(
						reviewRepository.findAverageScoreByGuesthouseId(gh.getId())
					).orElse(0.0)
				)
				.reviewCount(
					reviewRepository.countByGuesthouseId(gh.getId())
				)
				.lowerPrice(
					Optional.ofNullable(
						roomRepository.findMinPriceByGuesthouseId(gh.getId())
					).orElse(0L)
				)
				.addressRegion(gh.getAddressRegion())
				.checkinTime(
					LocalTime.parse(Optional.ofNullable(
						roomRepository.findEarliestCheckinByGuesthouseId(gh.getId())
					).orElse("00:00"))
				)
				.imageUrl(
					firstImageByGh.getOrDefault(gh.getId(), null)
				)
				.build()
			)
			.toList();

		// 6) PageImpl 으로 감싸서 반환
		return new PageImpl<>(dtos, pageable, page.getTotalElements());
	}
}