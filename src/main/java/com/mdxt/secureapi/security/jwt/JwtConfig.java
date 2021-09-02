package com.mdxt.secureapi.security.jwt;

import java.sql.Date;
import java.time.LocalDate;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.NoArgsConstructor;

//use this class to get Jwt properties from config file
//@ConfigurationProperties(prefix = "application.jwt") //relevant properties in application.properties will be prefixed with application.jwt  
@Getter
@Configuration
@NoArgsConstructor
public class JwtConfig {
	private SecretKey secretKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor("securesecuresecuresecuresecuresecuresecuresecuresecure".getBytes());//secretKeyFor(SignatureAlgorithm.HS256);
	private String tokenPrefix = "Bearer ";
	private Date tokenExpirationAfter = java.sql.Date.valueOf(LocalDate.now().plusWeeks(2));
}
