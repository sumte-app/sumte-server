package com.sumte.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.payment.entity.PaymentTerms;

public interface PaymentTermsRepository extends JpaRepository<PaymentTerms, Long> {
	void deleteByPaymentId(Long paymentId);
}
