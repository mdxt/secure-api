package com.mdxt.secureapi.entity;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class DentalPolicy extends BasePolicy{
	private Integer minNumberCovered;
	private Integer maxNumberCovered;
	private Double multiplierNumberCovered;
	
}
