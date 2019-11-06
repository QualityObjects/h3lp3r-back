package com.qualityobjects.oss.h3lp3r.domain.enums;

import lombok.Getter;

public enum Lang {
	ES("Spanish"), EN("English");

	@Getter
	private final String description;
	
	private Lang(String description) {
		this.description = description;
	}

}
