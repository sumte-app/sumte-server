	package com.sumte.payment.entity;

	import com.sumte.jpa.BaseTimeEntity;
	import com.sumte.reservation.entity.Reservation;

	import jakarta.persistence.Entity;
	import jakarta.persistence.EnumType;
	import jakarta.persistence.Enumerated;
	import jakarta.persistence.FetchType;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.JoinColumn;
	import jakarta.persistence.ManyToOne;
	import lombok.AllArgsConstructor;
	import lombok.Builder;
	import lombok.Getter;
	import lombok.NoArgsConstructor;

	@Entity
	@Getter
	@NoArgsConstructor
	@Builder
	@AllArgsConstructor
	public class Payment extends BaseTimeEntity {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "reservation_id")
		private Reservation reservation;

		private Long paidPrice;

		@Enumerated(EnumType.STRING)
		private PaymentStatus paymentStatus;

		@Enumerated(EnumType.STRING)
		private PaymentMethod paymentMethod;

		public void markAsPaid() {
			this.paymentStatus = PaymentStatus.PAID;
		}

		public void markAsFailed() {
			this.paymentStatus = PaymentStatus.FAILED;
		}
	}
