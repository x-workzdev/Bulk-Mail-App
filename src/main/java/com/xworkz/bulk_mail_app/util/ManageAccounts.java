package com.xworkz.bulk_mail_app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.xworkz.bulk_mail_app.service.Account;

@Component
public class ManageAccounts {
	
	@Value("${auth}")
	private String auth;
	@Value("${replyTo}")
	private String replyTo;
	@Value("${fromName}")
	private String fromName;
	@Value("${http}")
	private String http;

	@Value("${auth1}")
	private String auth1;
	@Value("${replyTo1}")
	private String replyTo1;
	@Value("${fromName}")
	private String fromName1;
	@Value("${http1}")
	private String http1;
	
	@Value("${auth2}")
	private String auth2;
	@Value("${replyTo2}")
	private String replyTo2;
	@Value("${fromName2}")
	private String fromName2;
	@Value("${http2}")
	private String http2;
	
	private Account accountId = Account.Default;
	private Account accountId1 = Account.Secondary;
	
	static Logger logger = LoggerFactory.getLogger(EncryptionHelper.class);
	
	public String[] selectUser(Integer mailid) {
		String[] user = new String[4];
		if (mailid == accountId.getId()) {
			user[0] = auth1;
			user[1] = replyTo1;
			user[2] = fromName1;
			user[3] = http1;
		}else if (mailid == accountId1.getId()) {
			user[0] = auth2;
			user[1] = replyTo2;
			user[2] = fromName2;
			user[3] = http2;
		}
		else {
			user[0] = auth;
			user[1] = replyTo;
			user[2] = fromName;
			user[3] = http;
		}
		return user;
	}
}