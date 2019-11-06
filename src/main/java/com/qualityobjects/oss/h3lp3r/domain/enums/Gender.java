package com.qualityobjects.oss.h3lp3r.domain.enums;

import lombok.Getter;

public enum Gender {
	M("Male"), F("Female"), N("Neutral");

	@Getter
	private final String description;
	
	private Gender(String description) {
		this.description = description;
	}

}
