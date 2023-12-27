package com.ravismishra99.AuthenticationApp.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ravismishra99.AuthenticationApp.dao.AuthenticationRequest;
import com.ravismishra99.AuthenticationApp.dao.RegisterRequest;
import com.ravismishra99.AuthenticationApp.service.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthenticationService service;
	
	@Autowired
	private AuthenticationService userService;

	@GetMapping("/")
	public ResponseEntity<String> home()
	{
		return ResponseEntity.ok("Welcome to Home Page");
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {
		return ResponseEntity.ok(userService.registerUser(registerRequest));
	}

	@PostMapping("/login")
	public ResponseEntity<String> authentiction(@RequestBody AuthenticationRequest authenticationRequest) {
		return ResponseEntity.ok(userService.authenticateUser(authenticationRequest));
	}
	
	@PostMapping("/cartPage")
	public ResponseEntity<String> cartPage() {
		return ResponseEntity.ok("Welcome to Cart Page");
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response)
	{ 
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null)
		{
			SecurityContextHolder.getContext().setAuthentication(null);
			
		}
		return ResponseEntity.ok("Logout Page");
	}
	
	  @PostMapping("/refresh-token")
	  public void refreshToken(
	      HttpServletRequest request,
	      HttpServletResponse response
	  ) throws IOException {
	    service.refreshToken(request, response);
	  }

}