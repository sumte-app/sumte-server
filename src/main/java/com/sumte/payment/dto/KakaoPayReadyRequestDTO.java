package com.sumte.payment.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
@Builder
public class KakaoPayReadyRequestDTO {
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String item_name;
    private String quantity;
    private String total_amount;
    private String tax_free_amount;
    private String approval_url;
    private String cancel_url;
    private String fail_url;
}
