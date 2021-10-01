package com.mdxt.secureapi.entity;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class DentalPolicy extends BasePolicy{
	//the total amount covered has an upper and lower limit
	//for calculating the cost, the multiplier is applied to the cover value
	private Long[] coverValues; 
	
	@JsonIgnore
	private Double multiplierCoverValue;
	
	//years till which the policy applies
	private Integer minCoverPeriod;
	private Integer maxCoverPeriod;
	
	@JsonIgnore
	private Double multiplierCoverPeriod;
	
	private Integer minNumberCovered;
	private Integer maxNumberCovered;
	
	@JsonIgnore
	private Double multiplierNumberCovered;
	
	private Long deductible;
}
