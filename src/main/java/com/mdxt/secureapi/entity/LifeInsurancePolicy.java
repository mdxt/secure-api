package com.mdxt.secureapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mdxt.secureapi.enums.ExaminationTypeEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class LifeInsurancePolicy extends BasePolicy{
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
	
	@JsonIgnore
	private Double multiplierCoverTillAge;
	
	private ExaminationTypeEnum examinationType;
}
