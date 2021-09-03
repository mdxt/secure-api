package com.mdxt.secureapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class LifeInsurancePolicy extends BasePolicy{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
