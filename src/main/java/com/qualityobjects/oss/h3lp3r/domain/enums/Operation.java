package com.qualityobjects.oss.h3lp3r.domain.enums;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;

public enum Operation {
	SHA1("SHA-1"), SHA256("SHA-256"), SHA384("SHA-256"), SHA512("SHA-512"),	MD5("MD5"), // 
	RANDOM_NUM_DEC("RANDOM_NUM_DEC"), RANDOM_NUM_INT("RANDOM_NUM_INT"), RANDOM_NAMES("RANDOM_NAMES"), // 
	ENC_BASE64("ENC_BASE64"), DEC_BASE64("DEC_BASE64"), ORACLE_SAYS("ORACLE_SAYS"), ORACLE_RESPONSE("ORACLE_RESPONSE");

	@Getter
	private final String code;
	
	private Operation(String code) {
		this.code = code;
	}
	
	@JsonCreator
	public static Operation of(String name) {
		Optional<Operation> val = Arrays.stream(values()).filter(wdt -> wdt.name().equalsIgnoreCase(name)).findAny();
		return val.isEmpty() ? null : val.get();
	}
	
	public final static Converter<String, Operation> CONVERTER = new Converter<String, Operation>() {
		@Override
		public Operation convert(String source) {
			return Operation.of(source);
		}
	};
	
}
