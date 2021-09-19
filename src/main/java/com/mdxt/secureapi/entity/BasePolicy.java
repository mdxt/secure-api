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
	
	//the total amount covered has an upper and lower limit
	//for calculating the cost, the multiplier is applied to the cover value
	private Long minCoverValue;
	private Long maxCoverValue;
	
	@JsonIgnore
	private Double multiplierCoverValue;
	
	//policy cover for x number of people, min x==1
//	private Integer maxNumberCovered;
//	private Double multiplierNumberCOvered;
	
	//age till which the policy applies
	private Integer minCoverTillAge;
	private Integer maxCoverTillAge;
	private Double multiplierCoverTillAge;
	
	private String[] additionalFeatures;
	
}
