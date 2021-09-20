package com.mdxt.secureapi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.mdxt.secureapi.enums.ExaminationTypeEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class LifeInsurancePolicy extends BasePolicy{
	private ExaminationTypeEnum examinationType;
}
