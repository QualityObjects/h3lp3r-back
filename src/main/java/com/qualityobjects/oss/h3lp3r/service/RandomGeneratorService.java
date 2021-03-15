package com.qualityobjects.oss.h3lp3r.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.naming.OperationNotSupportedException;

import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.domain.dto.RandomName;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;
import com.qualityobjects.oss.h3lp3r.domain.enums.OracleResponse;
import com.qualityobjects.oss.h3lp3r.domain.enums.OracleType;
import com.qualityobjects.oss.h3lp3r.exception.InvalidInputDataException;
import com.qualityobjects.oss.h3lp3r.exception.QOException;

import org.ajbrown.namemachine.Gender;
import org.ajbrown.namemachine.NameGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class RandomGeneratorService {

	@SuppressWarnings("unused") 
	private static final Logger LOG = LoggerFactory.getLogger(RandomGeneratorService.class);

	public static final String MIN_LIMIT_INPUT_KEY = "min";
	public static final String MAX_LIMIT_INPUT_KEY = "max";
	public static final String TOTAL_INPUT_KEY = "total";
	public static final String GENDER_INPUT_KEY = "gender";
	public static final String LANG_INPUT_KEY = "lang";
	public static final String ORACLE_QUESTION_KEY = "question";
	public static final String ORACLE_TYPE_KEY = "type";
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

	public OpResponse oracleSays(OpInput input) throws QOException {
		OpResponse resp = new OpResponse();

		String question = input.getParams().get(ORACLE_QUESTION_KEY);
		input.setAction(question == null ? Operation.ORACLE_SAYS : Operation.ORACLE_RESPONSE);
		resp.setInput(input);

		OracleType oracleType = OracleType.valueOf(input.getParams().getOrDefault(ORACLE_TYPE_KEY, OracleType.YES_NO.name()));
		OracleResponse result;

		double number;
		if (input.getAction() == Operation.ORACLE_SAYS) {
			number = randomNumberDec(0.0, 3.0);	
		} else { // input.getAction() == Operation.ORACLE_ANSWERS
			String normalizedQuestion = this.normalizeQuestion(question);
			number = ((Math.abs(normalizedQuestion.hashCode()) % 1000.0) / 1000.0) * 3.0;
		}
		switch (oracleType) {
			case YES_NO:
			result = number > 1.5 ? OracleResponse.NO : OracleResponse.YES;
			break;
			case YES_NO_MAYBE:
			result = number >= 2.0 ? OracleResponse.NO : (number < 1.0 ? OracleResponse.YES : OracleResponse.MAYBE);				
			break;			
			default:
				throw new InvalidInputDataException("Operation not supported: " + oracleType.name());
		}
		resp.setResult(result);
		return resp;
	}

	private String normalizeQuestion(String question) {
		return question.replaceAll("[-:;_ \\(\\)\\[\\],.\t\n]", "").toLowerCase();
	}


}
