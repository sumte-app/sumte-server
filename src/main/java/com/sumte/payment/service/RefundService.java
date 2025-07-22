package com.sumte.payment.service;

import com.sumte.payment.dto.RefundRequestDTO;

public interface RefundService {
    void requestRefund(RefundRequestDTO.CreateRefundDTO dto);
    void approveRefund(Long refundId);
}
