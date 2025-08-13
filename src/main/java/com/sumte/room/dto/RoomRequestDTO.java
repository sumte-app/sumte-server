package com.sumte.room.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class RoomRequestDTO {
	@Getter
	@Schema(name = "RegisterRoomRequest", description = "방 등록 요청 DTO")
	public static class RegisterRoom {

		@NotBlank(message = "이름을 입력해주세요")
		@Schema(description = "방 이름", example = "오션뷰 더블룸")
		String name;

		@Schema(description = "방 설명", example = "바다 전망, 킹사이즈 침대, 무료 Wi-Fi")
		String content;

		@NotNull(message = "가격을 입력해주세요")
		@Schema(description = "1박 가격(원)", example = "120000")
		Long price;

		@NotNull(message = "체크인 시간을 입력해주세요")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		@Schema(description = "체크인 시간", type = "string", example = "15:00:00")
		LocalTime checkin;

		@NotNull(message = "체크아웃 시간을 입력해주세요")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		@Schema(description = "체크아웃 시간", type = "string", example = "11:00:00")
		LocalTime checkout;

		@NotNull(message = "기준 인원을 입력해주세요")
		@Schema(description = "기준 인원", example = "2")
		Long standardCount;

		@NotNull(message = "최대 인원을 입력해주세요")
		@Schema(description = "최대 인원", example = "4")
		Long totalCount;
	}

	@Getter
	@Schema(name = "UpdateRoomRequest", description = "방 수정 요청 DTO")
	public static class UpdateRoom {

		@Schema(description = "방 이름", example = "마운틴뷰 트윈룸")
		String name;

		@Schema(description = "방 설명", example = "산 전망, 트윈 침대 2개, 무료 Wi-Fi")
		String content;

		@Schema(description = "1박 가격(원)", example = "90000")
		Long price;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		@Schema(description = "체크인 시간", type = "string", example = "14:00:00")
		LocalTime checkin;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		@Schema(description = "체크아웃 시간", type = "string", example = "10:00:00")
		LocalTime checkout;

		@Schema(description = "기준 인원", example = "2")
		Long standardCount;

		@Schema(description = "최대 인원", example = "3")
		Long totalCount;
	}
}
