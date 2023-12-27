package com.ravismishra99.AuthenticationApp.dao;

import com.ravismishra99.AuthenticationApp.entity.Role;
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
public class RegisterRequest {

	private int id;
	@NotBlank(message = "Enter valid User Name")
	private String name;
	@NotBlank(message = "Enter valid email")
	private String email;
	@NotBlank(message = "Enter valid mobile")
	private String mobile;
	@Min(3)
	@Max(10)
	//@Pattern(regexp = "{a-zA-Z0-9}", message = "Enter valid password")
	private String password;
	@NotBlank(message = "Enter valid role ( USER/ADMIN)")
	private Role role;
}