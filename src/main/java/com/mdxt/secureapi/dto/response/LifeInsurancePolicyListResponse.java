package com.mdxt.secureapi.dto.response;

import com.mdxt.secureapi.enums.ExaminationTypeEnum;

import lombok.Data;

@Data
public class LifeInsurancePolicyListResponse extends BasePolicyListResponse{
	private ExaminationTypeEnum examinationType;
}
