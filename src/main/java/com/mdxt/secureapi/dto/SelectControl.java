package com.mdxt.secureapi.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class SelectControl extends AbstractFormControl{
	List<String> permittedValues;
	
	public SelectControl(List<String> permittedValues) {
		this.permittedValues = permittedValues;
		this.controlType = "select";
	}
}
