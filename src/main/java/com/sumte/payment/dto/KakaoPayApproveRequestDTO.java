package com.sumte.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayApproveRequestDTO {
    private String cid;
    private String tid;
    private String partner_order_id;
    private String partner_user_id;
    private String pg_token;
}
