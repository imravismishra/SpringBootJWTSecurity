package com.ravismishra99.AuthenticationApp.service;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ravismishra99.AuthenticationApp.configuration.JwtService;
import com.ravismishra99.AuthenticationApp.dao.AuthenticationRequest;
import com.ravismishra99.AuthenticationApp.dao.RegisterRequest;
import com.ravismishra99.AuthenticationApp.entity.User;
import com.ravismishra99.AuthenticationApp.respository.TokenRepository;
import com.ravismishra99.AuthenticationApp.respository.UserRepository;
import com.ravismishra99.AuthenticationApp.token.Token;
import com.ravismishra99.AuthenticationApp.token.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenRepository tokenRepository;

	public String registerUser(RegisterRequest registerRequest) {

		User user = User.builder().name(registerRequest.getName()).email(registerRequest.getEmail())
				.password(passwordEncoder.encode(registerRequest.getPassword())).mobile(registerRequest.getMobile())
				.role(registerRequest.getRole()).build();

		userRepository.save(user);

		String jwtToken = jwtService.generateToken(user);
		SaveToken(user, jwtToken);
		    
		return jwtToken;
	}

	public String authenticateUser(AuthenticationRequest authRequest) {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

		User user = userRepository.findByEmail(authRequest.getEmail()).orElse(new User());
		System.out.println(user);
		String jwtToken = jwtService.generateToken(user);
//		var refreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user);
		SaveToken(user, jwtToken);
		    

		return jwtToken;
	}

	private void SaveToken(User user, String jwtToken) {
		Token token = Token.builder()
		        .user(user)
		        .token(jwtToken)
		        .type(TokenType.BEARER)
		        .expired(false)
		        .revoked(false)
		        .build();
		    tokenRepository.save(token);
	}
	
	 private void revokeAllUserTokens(User user) {
		    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		    if (validUserTokens.isEmpty())
		      return;
		    validUserTokens.forEach(token -> {
		      token.setExpired(true);
		      token.setRevoked(true);
		    });
		    tokenRepository.saveAll(validUserTokens);
		  }

		  public void refreshToken(
		          HttpServletRequest request,
		          HttpServletResponse response
		  ) throws IOException {
		    final String authHeader = request.getHeader("Authorization");
		    final String refreshToken;
		    final String userEmail;
		    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
		      return;
		    }
		    refreshToken = authHeader.substring(7);
		    userEmail = jwtService.extractUsername(refreshToken);
		    if (userEmail != null) {
		      var user = this.userRepository.findByEmail(userEmail)
		              .orElseThrow();
		      if (jwtService.isTokenValid(refreshToken, user)) {
		        var accessToken = jwtService.generateToken(user);
		        revokeAllUserTokens(user);
		       SaveToken(user, refreshToken);
				/*
				 * var authResponse = AuthenticationResponse.builder() .accessToken(accessToken)
				 * .refreshToken(refreshToken) .build(); new
				 * ObjectMapper().writeValue(response.getOutputStream(), authResponse);
				 */
		      }
		    }
		  }
}