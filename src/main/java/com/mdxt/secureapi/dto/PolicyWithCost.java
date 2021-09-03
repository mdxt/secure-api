package com.mdxt.secureapi.dto;

import com.mdxt.secureapi.entity.LifeInsurancePolicy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PolicyWithCost {
	LifeInsurancePolicy policy;
	Double totalCost;
}
