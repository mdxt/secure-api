package com.mdxt.secureapi.security.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtTokenVerifier extends OncePerRequestFilter{
	
	private JwtConfig jwtConfig;

	public JwtTokenVerifier(JwtConfig jwtConfig) {
		super();
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//this method is closely linked to successfulAuthentication method in JwtUsernameAndPasswordAuthenticationFilter 
		
		String authorizationHeader = request.getHeader("Authorization");
		
		if(Strings.isBlank(authorizationHeader) || 
				!authorizationHeader.startsWith("Bearer ")) {
			//request does not involve authorization 
			//pass on the next filter in the chain
			filterChain.doFilter(request, response);
			return;
		}
		
		//header should contain Bearer (token)
		String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");
		
		
		String username;
		Set<SimpleGrantedAuthority> sga;
		
		try {
			Jws<Claims> claimsJws = Jwts.parserBuilder()
					.setSigningKey(jwtConfig.getSecretKey())
					.build()
					.parseClaimsJws(token);
			
			Claims body = claimsJws.getBody();
			 
			username = body.getSubject();
			 
			List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
			sga = authorities.stream()
							.map(m -> new SimpleGrantedAuthority(m.get("authority")))
							.collect(Collectors.toSet());
		} catch(JwtException e) {
			System.err.println("Token is invalid or expired "+token);
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid or expired");
			return;
		}
		
		
//		try {
			
			 Authentication authentication = 
					 new UsernamePasswordAuthenticationToken(username,
							 								null,
							 								sga);
			 
			 SecurityContextHolder.getContext().setAuthentication(authentication);
//		} catch(JwtException e) {
//			System.err.println("Authentication failed for token "+token);
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed for token");
//			return;
//		}
			 
		 filterChain.doFilter(request, response);
	}

}
