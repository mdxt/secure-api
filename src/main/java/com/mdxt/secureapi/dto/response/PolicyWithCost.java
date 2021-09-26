package com.mdxt.secureapi.dto.response;

import com.mdxt.secureapi.entity.BasePolicy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PolicyWithCost {
	private BasePolicy policy;
	private Double cost;
}
