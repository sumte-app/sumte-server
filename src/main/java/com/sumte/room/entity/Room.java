package com.sumte.room.entity;

import java.time.LocalTime;

import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.jpa.BaseTimeEntity;
import com.sumte.room.dto.RoomRequestDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Room extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guesthouse_id")
	private Guesthouse guesthouse;

	private String name;
	private String contents;
	private Long price;
	private LocalTime checkin;
	private LocalTime checkout;
	private Long standardCount;
	private Long totalCount;
	private String imageUrl;

	public static Room createRoomEntity(RoomRequestDTO.RegisterRoom dto) {
		Room room = new Room();
		room.name = dto.getName();
		room.contents = dto.getContent();
		room.price = dto.getPrice();
		room.checkin = dto.getCheckin();
		room.checkout = dto.getCheckout();
		room.standardCount = dto.getStandartCount();
		room.totalCount = dto.getTotalCount();
		room.imageUrl = dto.getImageUrl();
		return room;
	}

	public void setGuesthouse(Guesthouse guesthouse) {
		this.guesthouse = guesthouse;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public void setCheckin(LocalTime checkin) {
		this.checkin = checkin;
	}

	public void setCheckout(LocalTime checkout) {
		this.checkout = checkout;
	}

	public void setStandardCount(Long standardCount) {
		this.standardCount = standardCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
