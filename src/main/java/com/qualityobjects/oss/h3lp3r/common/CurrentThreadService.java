package com.qualityobjects.oss.h3lp3r.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CurrentThreadService {

    private final InheritableThreadLocal<Map<String, Object>> threadData = new InheritableThreadLocal<>();

    public Map<String, Object> getData() {
        Map<String, Object> data = this.threadData.get();
        if (data == null) {
            data = new HashMap<>();
            this.threadData.set(data);
        }
        return data;
    }

    public void clean() {
        this.threadData.remove();
    }

    public <T> T get(String key, Class<T> klass) {
        return klass.cast(getData().get(key));
    }

    public Object get(String key) {
        return getData().get(key);
    }


    public void set(String key, Object value) {
        getData().put(key, value);
    }



}
