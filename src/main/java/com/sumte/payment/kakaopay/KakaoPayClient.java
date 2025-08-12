package com.sumte.payment.kakaopay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumte.payment.dto.KakaoPayApproveRequestDTO;
import com.sumte.payment.dto.KakaoPayApproveResponseDTO;
import com.sumte.payment.dto.KakaoPayReadyRequestDTO;
import com.sumte.payment.dto.KakaoPayReadyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoPayClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${kakao.pay.secret-key}")
    private String adminKey;

    public KakaoPayReadyResponseDTO requestPayment(KakaoPayReadyRequestDTO request) {
        return webClient.post()
                .uri("https://open-api.kakaopay.com/online/v1/payment/ready")
                .header("Authorization", "SECRET_KEY " + adminKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KakaoPayReadyResponseDTO.class)
                .block();
    }

    public KakaoPayApproveResponseDTO approvePayment(KakaoPayApproveRequestDTO request) {
        return webClient.post()
                .uri("https://open-api.kakaopay.com/online/v1/payment/approve")
                .header("Authorization", "SECRET_KEY " + adminKey)
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(KakaoPayApproveResponseDTO.class)
                .block();
    }
}
