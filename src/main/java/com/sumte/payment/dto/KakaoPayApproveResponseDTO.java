package com.sumte.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayApproveResponseDTO {
    private String aid;
    private String tid;
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;

    @Getter
    public static class Amount {
        private int total;
        private int tax_free;
        private int vat;
    }
}
