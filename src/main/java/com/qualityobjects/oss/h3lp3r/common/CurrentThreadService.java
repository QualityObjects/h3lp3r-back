package com.qualityobjects.oss.h3lp3r.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CurrentThreadService {

	private ThreadLocal<Map<String, Object>> threadData = new ThreadLocal<>() {
	    @Override protected Map<String, Object> initialValue() {
	        return new HashMap<>();
	    }
	};
	
	private Map<String, Object> getThreadData() {
		return this.threadData.get();
	}

	public <T> T get(String key, Class<T> klass) {
		return klass.cast(getThreadData().get(key));
	}

	public Object get(String key) {
		return getThreadData().get(key);
	}


	public void set(String key, Object value) {
		getThreadData().put(key, value);
	}


}
