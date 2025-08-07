package com.sumte.guesthouse.dto;

import java.util.List;

import com.sumte.guesthouse.entity.AdType;
import com.sumte.room.dto.RoomResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuesthouseDetailDTO {
	private Long id;
	private String name;
	private String addressRegion;
	private String addressDetail;
	private String information;
	private AdType advertisement;
	private List<String> optionServices;
	private List<String> targetAudience;
	private List<RoomResponseDTO.GetRoomResponse> rooms;
	private List<String> imageUrls;    // 모든 이미지 URL 리스트
}
