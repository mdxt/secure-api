package com.mdxt.secureapi.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BasePolicy {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String insurer;
	private String name;
	
	//store as static files or in database?
	//http://www.africau.edu/images/default/sample.pdf
	private String documentPath;
	
	private String[] additionalFeatures;
	
}
