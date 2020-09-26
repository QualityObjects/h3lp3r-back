package com.qualityobjects.oss.h3lp3r.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.ajbrown.namemachine.Gender;
import org.ajbrown.namemachine.Name;
import org.ajbrown.namemachine.NameGenerator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.domain.dto.RandomName;
import com.qualityobjects.oss.h3lp3r.domain.enums.Lang;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;
import com.qualityobjects.oss.h3lp3r.exception.InvalidInputDataException;
import com.qualityobjects.oss.h3lp3r.exception.QOException;

@Service
public class RandomGeneratorService {

	@SuppressWarnings("unused") 
	private static final Logger LOG = LoggerFactory.getLogger(RandomGeneratorService.class);

	public static final String MIN_LIMIT_INPUT_KEY = "min";
	public static final String MAX_LIMIT_INPUT_KEY = "max";
	public static final String TOTAL_INPUT_KEY = "total";
	public static final String GENDER_INPUT_KEY = "gender";
	public static final String LANG_INPUT_KEY = "lang";
	public static final Integer MAX_TOTAL_NAMES = 1000;
	
	private NameGenerator nameGenerator = new NameGenerator();

	public OpResponse randomNumber(OpInput input) throws QOException {
		OpResponse resp = new OpResponse();
		resp.setInput(input);
		
		String minValueStr = input.getParams().get(MIN_LIMIT_INPUT_KEY);
		String maxValueStr = input.getParams().get(MAX_LIMIT_INPUT_KEY);
		double min = ObjectUtils.isEmpty(minValueStr) ? 0.0 : Double.valueOf(minValueStr);
		double max = ObjectUtils.isEmpty(maxValueStr) ? min + 1.0 : Double.valueOf(maxValueStr);

		if (input.getAction() == Operation.RANDOM_NUM_INT) {
			resp.setResult(randomNumberInt(min, max));
		} else if (input.getAction() == Operation.RANDOM_NUM_DEC) {
			resp.setResult(randomNumberDec(min, max));
		} else {
			throw new InvalidInputDataException(String.format("Operation not supported: '%s'", input.getAction()));
		}
		return resp;
	}
	
	public long randomNumberInt(double min, double  max) throws NumberFormatException {
		long result = (long)(Math.floor(Math.random()*(max-min+1))+min);
	    return result;
	}

	public double randomNumberDec(double min, double  max) throws NumberFormatException {
	    double result = Math.random()*(max-min)+min;
	    return result;
	}

	public OpResponse randomNames(OpInput input) throws QOException {
		OpResponse resp = new OpResponse();
		resp.setInput(input);
		
		int total = Integer.valueOf( input.getParams().getOrDefault(TOTAL_INPUT_KEY, "1"));
		if (total > MAX_TOTAL_NAMES) {
			total = MAX_TOTAL_NAMES;
		}
		Gender gender = input.getParams().containsKey(GENDER_INPUT_KEY) ? Gender.valueOf(input.getParams().get(GENDER_INPUT_KEY)) : null;

		List<RandomName> result = nameGenerator.generateNames( total, gender ).parallelStream().map(name -> {
			return RandomName.builder() //
			.gender(name.getGender()) //
			.firstName(name.getFirstName()) //
			.lastName(name.getLastName()) //
			.build();
		}).collect(Collectors.toList());

		resp.setResult(result);
		return resp;
	}


}
