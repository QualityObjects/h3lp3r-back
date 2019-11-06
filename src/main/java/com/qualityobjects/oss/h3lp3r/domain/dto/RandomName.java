package com.qualityobjects.oss.h3lp3r.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qualityobjects.oss.h3lp3r.domain.enums.Gender;
import com.qualityobjects.oss.h3lp3r.domain.enums.Lang;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class RandomName {

	@JsonProperty("first_name")
	private String firstname;
	@JsonProperty("last_name")
	private String lastName;
	@JsonProperty("last_name2")
	private String lastName2;
	
	private Gender gender;
	private Lang lang;
}
