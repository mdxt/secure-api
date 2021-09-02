package com.mdxt.secureapi.security.jwt;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
class AuthRequest{
	private String username;
	private String password;
}

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;
	
	private JwtConfig jwtConfig;
			
	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig) {
		this.authenticationManager = authenticationManager;
		this.jwtConfig = jwtConfig;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			AuthRequest authRequest = new ObjectMapper().readValue(request.getInputStream(), AuthRequest.class);
			
			System.out.println("attempting login with username-"+authRequest.getUsername()+", password-"+authRequest.getPassword());
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					authRequest.getUsername(), authRequest.getPassword());
			
			return authenticationManager.authenticate(authentication);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.attemptAuthentication(request, response);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String token = Jwts.builder()
			.setSubject(authResult.getName())
			.claim("authorities", authResult.getAuthorities())
			.setIssuedAt(new Date())
			.setExpiration(jwtConfig.getTokenExpirationAfter())
			.signWith(jwtConfig.getSecretKey())
			.compact();
		
		response.addHeader("Authorization", jwtConfig.getTokenPrefix()+token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		System.err.print("Authentication failed with error "+failed);
		response.sendError(HttpServletResponse.SC_ACCEPTED, "Incorrect username or password");
	}
	
	

}
