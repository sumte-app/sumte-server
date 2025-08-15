package com.sumte.payment.controller;

import com.sumte.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "결제 리다이렉트 API", description = "PG사 결제 후 앱으로 리다이렉트 처리")
public class KakaoRedirectController {

    @Value("${kakao.pay.android.scheme}")
    private String scheme;

    @Value("${kakao.pay.android.host}")
    private String host;

    @GetMapping("/pay/success")
    @Operation(
            summary = "결제 성공 리다이렉트",
            description = "카카오페이 결제 성공 후 앱으로 리다이렉트합니다.<br>" +
                    "Query Parameter로 paymentId, pg_token이 전달됩니다."
    )
    public ResponseEntity<ApiResponse<String>> success(
            @RequestParam Long paymentId,
            @RequestParam("pg_token") String pgToken) {

        log.info("[KAKAO SUCCESS] paymentId={}, pg_token={}", paymentId, pgToken);

        String deepLink = UriComponentsBuilder.newInstance()
                .scheme("myapp").host("pay").path("/success")
                .queryParam("paymentId", paymentId)
                .queryParam("pg_token", pgToken)
                .build(true).toUriString();

        log.info("DeepLink constructed: {}", deepLink);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(deepLink));
        log.info("Responding 302 with Location: {}", headers.getLocation());
        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .body(ApiResponse.success(deepLink));
    }

    @GetMapping("/pay/cancel")
    @Operation(
            summary = "결제 취소 리다이렉트",
            description = "카카오페이 결제 취소 후 앱으로 리다이렉트합니다.<br>" +
                    "Query Parameter로 paymentId가 전달됩니다."
    )
    public ResponseEntity<ApiResponse<String>> cancel(@RequestParam Long paymentId) {
        String deepLink = UriComponentsBuilder.newInstance()
                .scheme(scheme).host(host).path("/cancel")
                .queryParam("paymentId", paymentId)
                .build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(deepLink));
        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .body(ApiResponse.success(deepLink));
    }

    @GetMapping("/pay/fail")
    @Operation(
            summary = "결제 실패 리다이렉트",
            description = "카카오페이 결제 실패 후 앱으로 리다이렉트합니다.<br>" +
                    "Query Parameter로 paymentId, code, message가 전달됩니다."
    )
    public ResponseEntity<ApiResponse<String>> fail(
            @RequestParam Long paymentId,
            @RequestParam(required=false) String code,
            @RequestParam(required=false) String message) {

        String deepLink = UriComponentsBuilder.newInstance()
                .scheme(scheme).host(host).path("/fail")
                .queryParam("paymentId", paymentId)
                .queryParam("code", code)
                .queryParam("message", message)
                .build().toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(deepLink));
        return ResponseEntity.status(HttpStatus.FOUND)
                .headers(headers)
                .body(ApiResponse.success(deepLink));
    }
}