package com.qualityobjects.oss.h3lp3r.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.qualityobjects.oss.h3lp3r.domain.dto.OpInput;
import com.qualityobjects.oss.h3lp3r.domain.dto.OpResponse;
import com.qualityobjects.oss.h3lp3r.domain.dto.RandomName;
import com.qualityobjects.oss.h3lp3r.domain.entiity.FirstName;
import com.qualityobjects.oss.h3lp3r.domain.entiity.LastName;
import com.qualityobjects.oss.h3lp3r.domain.enums.Lang;
import com.qualityobjects.oss.h3lp3r.domain.enums.Operation;
import com.qualityobjects.oss.h3lp3r.exception.InvalidInputDataException;
import com.qualityobjects.oss.h3lp3r.exception.QOException;
import com.qualityobjects.oss.h3lp3r.repository.FirstNameRepository;
import com.qualityobjects.oss.h3lp3r.repository.LastNameRepository;

@Service
public class RandomGeneratorService {

	@SuppressWarnings("unused") 
	private static final Logger LOG = LogManager.getLogger(RandomGeneratorService.class);

	@Autowired
	private FirstNameRepository firstNameRepository;

	@Autowired
	private LastNameRepository lastNameRepository;
	
	public static final String MIN_LIMIT_INPUT_KEY = "min";
	public static final String MAX_LIMIT_INPUT_KEY = "max";
	public static final String TOTAL_INPUT_KEY = "total";
	public static final String GENDER_INPUT_KEY = "gender";
	public static final String LANG_INPUT_KEY = "lang";
	public static final Integer MAX_TOTAL_NAMES = 100;
	
	
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
		List<RandomName> result = new ArrayList<>();
		int total = Integer.valueOf( input.getParams().getOrDefault(TOTAL_INPUT_KEY, "1"));
		if (total > MAX_TOTAL_NAMES) {
			total = MAX_TOTAL_NAMES;
		}
		Set<Integer> included = new HashSet<>();
		List<FirstName> firstNames = this.findFirstNames(input.getParams().get(GENDER_INPUT_KEY));
		List<LastName> lastNames = this.findLastNames(input.getParams().get(GENDER_INPUT_KEY));
		
		while (result.size() < total) {
			int idxFn = (int)this.randomNumberInt(0.0, firstNames.size() - 1.0);
			int idxLn = (int)this.randomNumberInt(0.0, lastNames.size() - 1.0);
			FirstName fn = firstNames.get(idxFn);
			LastName ln = lastNames.get(idxLn);
			Integer hashCode = fn.getFirstName().hashCode() + ln.getLastName().hashCode();
			if (included.contains(hashCode)) {
				// Full name already exist.
				// NOTE: We can find some false-positive but in this case is not relevant
				continue;
			}
			included.add(hashCode);
			
			RandomName rn = new RandomName();
			rn.setGender(fn.getGender());
			rn.setFirstname(fn.getFirstName());
			rn.setLastName(ln.getLastName());
			result.add(rn);
		}
		
		resp.setResult(result);
		return resp;
	}
	
	@Cacheable(cacheNames = {"names"})
	public List<FirstName> findFirstNames(@Nullable String langStr) {
		if (langStr == null) {
			return firstNameRepository.findAll();
		} else {
			return firstNameRepository.findByLang(Lang.valueOf(langStr));
		}
	}

	@Cacheable(cacheNames = {"names"})
	public List<LastName> findLastNames(@Nullable String langStr) {
		if (langStr == null) {
			return lastNameRepository.findAll();
		} else {
			return lastNameRepository.findByLang(Lang.valueOf(langStr));
		}
	}


}
