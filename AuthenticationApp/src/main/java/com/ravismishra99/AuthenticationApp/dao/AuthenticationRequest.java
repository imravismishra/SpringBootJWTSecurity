package com.ravismishra99.AuthenticationApp.dao;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {

	@NotBlank(message = "Enter valid email")
	private String email;
	@Min(3)
	@Max(10)
	//@Pattern(regexp = "{a-zA-Z0-9}", message = "Enter valid password")
	private String password;
}