package com.mdxt.secureapi.dto.request;

import lombok.Data;

@Data
public class RequestCreateDentalAndVision extends RequestCreateDentalPolicy{
	private Boolean coversEyeglassFrames; 
}
