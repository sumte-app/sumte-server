package com.sumte.email.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.email.Service.EmailVerificationService;
import com.sumte.email.dto.request.EmailSendRequest;
import com.sumte.email.dto.request.EmailVerifyRequest;
import com.sumte.email.dto.response.EmailSendResponse;
import com.sumte.email.dto.response.EmailVerifyResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "이메일 인증", description = "이메일 인증번호 발송 및 검증 API")
@RestController
@RequestMapping("/email")
public class EmailController {

	private final EmailVerificationService service;

	public EmailController(EmailVerificationService service) {
		this.service = service;
	}

	@Operation(
		summary = "이메일 인증번호 발송",
		description = """
			사용자가 입력한 이메일 주소로 인증번호를 발송합니다.  
			- 이메일 형식 검증이 이루어지고,
			- 아래 쿨타임 필드가 보이실텐데 이는 재전송까지 남은 시간을 볼 수 있는 필드로 넣었습니다.
			- 쿨다임의 경우 10초로 지정하였고, 쿨타임에 재전송을 하는 경우 에러가 나니 확인해주시면 될 것 같습니다.
			"""
	)
	@PostMapping("/send")
	public ResponseEntity<EmailSendResponse> send(@Valid @RequestBody EmailSendRequest req) {
		return ResponseEntity.ok(service.sendCode(req.email()));
	}

	@Operation(
		summary = "이메일 인증번호 검증",
		description = """
			사용자가 입력한 이메일과 인증번호가 일치하는지 확인합니다.  
			- 요청 시 바디에 이메일도 꼭 포함되도록 했습니다! 
			- 인증번호가 만료되었거나 요청되지 않은 경우 에러가 발생하고 
			- 검증 성공 시 바로 인증완료 처리되니 확인해주시면 될 것 같습니다!
			"""
	)
	@PostMapping("/verify")
	public ResponseEntity<EmailVerifyResponse> verify(@Valid @RequestBody EmailVerifyRequest req) {
		return ResponseEntity.ok(service.verifyCode(req.email(), req.code()));
	}
}