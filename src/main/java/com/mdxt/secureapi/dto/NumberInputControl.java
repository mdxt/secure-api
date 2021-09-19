package com.mdxt.secureapi.dto;

import lombok.Getter;

@Getter
public class NumberInputControl extends AbstractFormControl{
	Long min;
	Long max;
	String dataType; 
	public NumberInputControl(Long min, Long max) {
		this.min = min;
		this.max = max;
		this.controlType = "input";
		this.dataType = "number";
	}
	
}
