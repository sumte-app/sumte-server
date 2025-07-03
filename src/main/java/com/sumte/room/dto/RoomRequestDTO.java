package com.sumte.room.dto;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class RoomRequestDTO {
	@Getter
	public static class Register {
		@NotBlank(message = "이름을 입력해주세요")
		String name;

		String content;

		@NotNull(message = "가격을 입력해주세요")
		Long price;

		@NotNull(message = "체크인 시간을 입력해주세요")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		LocalTime checkin;

		@NotNull(message = "체크아웃 시간을 입력해주세요")
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		LocalTime checkout;

		@NotNull(message = "기준 인원을 입력해주세요")
		Long standartCount;

		@NotNull(message = "최대 인원을 입력해주세요")
		Long totalCount;

		String imageUrl;

	}

	@Getter
	public static class Update {
		String name;
		String content;
		Long price;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		LocalTime checkin;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
		LocalTime checkout;
		Long standartCount;
		Long totalCount;
		String imageUrl;
	}

}
