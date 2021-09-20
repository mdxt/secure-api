package com.mdxt.secureapi;

import javax.annotation.Resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class SecureApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureApiApplication.class, args);
	}

}
