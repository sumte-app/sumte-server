package com.sumte.image;

import com.sumte.jpa.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Image extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OwnerType ownerType;

	private Long ownerId; // guesthouseId 또는 reviewId 등

	private String url;
	private String imageType; // (optional) 썸네일, 본문 등
	private Integer sortOrder;
}
