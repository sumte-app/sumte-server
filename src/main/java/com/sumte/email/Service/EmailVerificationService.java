package com.sumte.email.Service;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.EmailErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.email.EmailVerificationProperties;
import com.sumte.email.dto.response.EmailSendResponse;
import com.sumte.email.dto.response.EmailVerifyResponse;
import com.sumte.email.sender.EmailSender;
import com.sumte.email.sender.VerificationCodeGenerator;
import com.sumte.email.store.EmailVerificationStore;
import com.sumte.email.store.VerificationEntry;

/**
 * 📌 이메일 인증 서비스
 *
 * - /email/send : 인증번호 생성 & 저장 & 발송
 * - /email/verify : 인증번호 검증 (성공 시 일회성으로 폐기)
 *
 * ⚠️ 실패 시에는 ApiException(EmailErrorCode.*)을 던져서
 *    GlobalExceptionHandler가 Email4xx 형식의 본문을 내려주게 한다.
 *    (이렇게 해야 Swagger/프론트에서 COMMON400이 아니라 Email4xx가 보임)
 */
@Service
public class EmailVerificationService {

	private static final Logger log = LoggerFactory.getLogger(EmailVerificationService.class);

	private final EmailVerificationStore store;
	private final VerificationCodeGenerator generator;
	private final EmailSender sender;
	private final EmailVerificationProperties props;

	public EmailVerificationService(EmailVerificationStore store,
		VerificationCodeGenerator generator,
		EmailSender sender,
		EmailVerificationProperties props) {
		this.store = store;
		this.generator = generator;
		this.sender = sender;
		this.props = props;
	}

	public EmailSendResponse sendCode(String email) {
		Instant now = Instant.now();
		var existing = store.get(email);

		// 재전송 쿨다운 적용 (프론트에서 처리안할경우 굳이..)
		if (existing != null) {
			long elapsed = Duration.between(existing.lastSentAt(), now).getSeconds();
			long cooldown = props.getResendCooldownSeconds();
			if (elapsed < cooldown) {
				long remaining = cooldown - elapsed;
				log.debug("Resend cooldown: email={}, remainingSeconds={}", email, remaining);
				return new EmailSendResponse(false, "재전송 쿨다운 중입니다.", remaining);
			}
		}

		// 코드 생성 & 저장
		String code = generator.numeric(props.getCodeLength());
		Instant expiresAt = now.plusSeconds(props.getTtlSeconds());
		store.put(email, new VerificationEntry(code, expiresAt, now));

		// 메일 양식
		String subject = "[숨터] 이메일 인증번호";
		String body = """
			숨터 이메일 인증번호는 [%s] 입니다.
			유효시간: %d분
			(잘못 수신한 경우 이 메일을 무시하세요.)
			""".formatted(code, props.getTtlSeconds() / 60);

		try {
			sender.send(email, subject, body);
		} catch (MailException e) {
			// 메일 전송 실패는 EmailErrorCode.MAIL_SENDING_FAILED 로 통일
			log.warn("Mail sending failed: email={}, err={}", email, e.getMessage());
			throw new SumteException(EmailErrorCode.MAIL_SENDING_FAILED);
		}

		log.debug("Email verification code generated: email={}, code={}, expiresAt={}", email, code, expiresAt);
		return new EmailSendResponse(true, "인증번호가 발송되었습니다.", 0);
	}

	public EmailVerifyResponse verifyCode(String email, String code) {
		Instant now = Instant.now();
		var entry = store.get(email);

		// 요청되지 않았거나, 성공을 이미 한 경우
		if (entry == null) {
			throw new SumteException(EmailErrorCode.VERIFICATION_CODE_NOT_FOUND);
		}

		// 만료되는경우
		if (entry.isExpired(now)) {
			store.remove(email);
			throw new SumteException(EmailErrorCode.VERIFICATION_CODE_NOT_FOUND);
		}

		// 일치하지 않는경우
		if (!entry.code().equals(code)) {
			throw new SumteException(EmailErrorCode.VERIFICATION_CODE_MISMATCH);
		}

		// 성공 후 바로 삭제
		store.remove(email);
		return new EmailVerifyResponse(true, "인증이 완료되었습니다.");
	}
}