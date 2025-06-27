package com.sumte.guesthouse.entity;

import java.util.ArrayList;
import java.util.List;

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

	private String imageUrl;

	@OneToMany(mappedBy = "guesthouse", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Room> rooms = new ArrayList<>();
}
