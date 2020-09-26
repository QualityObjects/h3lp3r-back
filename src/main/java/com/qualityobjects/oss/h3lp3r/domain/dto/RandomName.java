package com.qualityobjects.oss.h3lp3r.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.qualityobjects.oss.h3lp3r.domain.enums.Lang;

import org.ajbrown.namemachine.Gender;

import lombok.Data;

@JsonInclude(value = Include.NON_NULL)
@Data
public class RandomName {

	private String firstName;
	private String lastName;
	private String lastName2;
	
	private Gender gender;
	private Lang lang;
}
