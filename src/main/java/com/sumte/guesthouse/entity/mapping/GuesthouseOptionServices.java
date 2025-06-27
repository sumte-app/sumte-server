package com.sumte.guesthouse.entity.mapping;

import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.entity.OptionServices;
import com.sumte.jpa.BaseTimeEntity;

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
public class GuesthouseOptionServices extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guesthouse_id")
	private Guesthouse guesthouse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_services_id")
	private OptionServices optionServices;
}
