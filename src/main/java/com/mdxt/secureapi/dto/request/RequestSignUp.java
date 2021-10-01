package com.mdxt.secureapi.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RequestSignUp {
	@Email
	@NotBlank
	String email;
	
	@NotBlank
	String password;
}
