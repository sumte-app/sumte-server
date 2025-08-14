package com.sumte.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoPayReadyResponseDTO {
    private String tid;
    private String next_redirect_app_url;
    private String next_redirect_pc_url;
    private String android_app_scheme;
    private String created_at;
}
