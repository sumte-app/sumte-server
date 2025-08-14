package com.sumte.email.sender;

public interface EmailSender {
	void send(String to, String subject, String plainTextBody);
}
