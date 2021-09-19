package com.mdxt.secureapi.dto;

import lombok.Getter;
import lombok.Setter;

class Validator {
	String type;
}

@Getter
@Setter
public abstract class AbstractFormControl {
	String name;
	String controlType;
}

