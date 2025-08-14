package com.sumte.email.sender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * spring.mail.host 가 설정돼 있으면 자동 사용되는 SMTP 발송기
 */
@Component
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
public class SmtpEmailSender implements EmailSender {

	private final JavaMailSender mailSender;

	@Value("${app.email-verification.from:no-reply@sumte.com}")
	private String from;

	public SmtpEmailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void send(@NonNull String to, @NonNull String subject, @NonNull String plainTextBody) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		msg.setText(plainTextBody);
		mailSender.send(msg);
	}
}