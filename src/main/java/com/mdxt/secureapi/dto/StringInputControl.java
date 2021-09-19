package com.mdxt.secureapi.dto;

public class StringInputControl extends AbstractFormControl{
	String regexp;
	String dataType;
	
	public StringInputControl(String regexp, String dataType) {
		this.regexp = regexp;
		this.dataType = dataType;
	}
	
}
