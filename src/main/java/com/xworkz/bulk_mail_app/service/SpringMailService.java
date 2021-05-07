package com.xworkz.bulk_mail_app.service;

import org.springframework.mail.javamail.MimeMessagePreparator;

public interface SpringMailService {

	public boolean validateAndSendMailByMailId(MimeMessagePreparator messagePreparator);
}
