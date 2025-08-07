package com.sumte.guesthouse.entity;

import java.util.ArrayList;
import java.util.List;

import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.jpa.BaseTimeEntity;
import com.sumte.room.entity.Room;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Guesthouse extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String addressRegion;
	private String addressDetail;

	@Enumerated(EnumType.STRING)
	private AdType advertisement;

	private String information;

	@OneToMany(mappedBy = "guesthouse", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Room> rooms = new ArrayList<>();

	public static Guesthouse createByRegisterDTO(GuesthouseRequestDTO.Register dto) {
		Guesthouse guesthouse = new Guesthouse();
		guesthouse.name = dto.getName();
		guesthouse.addressRegion = dto.getAddressRegion();
		guesthouse.addressDetail = dto.getAddressDetail();
		guesthouse.information = dto.getInformation();
		guesthouse.advertisement = AdType.NON_AD;
		return guesthouse;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddressRegion(String addressRegion) {
		this.addressRegion = addressRegion;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public void activateAd() {
		this.advertisement = AdType.AD;
	}

	public void deactivateAd() {
		this.advertisement = AdType.NON_AD;
	}

}
