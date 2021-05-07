package com.xworkz.bulk_mail_app.service;

import com.xworkz.bulk_mail_app.dto.LoginDTO;

public interface LoginService {

	public boolean generateOTP();
	
	public String genarateRandomOTP();

	public boolean validateAndLogin(LoginDTO dto);

}
