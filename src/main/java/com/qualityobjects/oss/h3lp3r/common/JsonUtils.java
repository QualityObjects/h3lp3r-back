package com.qualityobjects.oss.h3lp3r.common;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {

	private static final Logger LOG = LogManager.getLogger(JsonUtils.class);


    final private static ObjectMapper MAPPER = new ObjectMapper();
    final private static ObjectMapper MAPPER_PRETTY;

    static {

        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        MAPPER_PRETTY = MAPPER.copy();
        MAPPER_PRETTY.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
	public static Map<String, Object> toMap(JsonNode jsonNode) {
		
		Map<String, Object> result = MAPPER.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {
		});
		return result;
	}



    /**
     * Create a JSON string from a object compatible or annotated with Jackson,
     * i.e: <code>
     * {"f1":2345,"f2":"Test de valor"}
     * </code>
     * @param obj
     * @return JSON string representation from object
     */
    public static String toPrettyJSON(Object obj, Class<?> klass) throws IOException {
        // and could also do other configuration...
        try {
            if (obj == null)
                return null;
            return MAPPER_PRETTY.writerFor(klass).writeValueAsString(obj);
        } catch (IOException e) {
            LOG.error("Error formating JSON from object: {}", obj, e);
            throw e;
        }
    }

    public static String toPrettyJSON(Object obj) throws IOException {
        try {
            if (obj == null)
                return null;
            return MAPPER_PRETTY.writeValueAsString(obj);
        } catch (IOException e) {
            LOG.error("Error formating JSON from object: {}", obj, e);
            throw e;
        }
    }

    public static <T> T parseJSON(String json, Class<T> type) throws IOException {
        try {
            if (json == null)
                return null;
            return MAPPER.readValue(json, type);
        } catch (JsonParseException e) {
            LOG.error("Error parsing JSON string to obejct: {}", json, e);
            if (json.length() > 60) {
                json = json.substring(0, 50) + "...";
            }
            throw new IOException("Error parsing JSON string to object: " + json, e);
        } catch (IOException e) {
            LOG.error("Error parsing JSON string to object: {}", json, e);
            if (json.length() > 60)
                json = json.substring(0, 50) + "...";
            throw e;
        }
    }

    
    public static <T> T parseJSON2(String json, TypeReference<T> type) throws IOException {
        try {
            if (json == null)
                return null;
            return MAPPER.readValue(json, type);
        } catch (JsonParseException e) {
            LOG.error("Error parsing JSON string to obejct: {}", json, e);
            if (json.length() > 60) {
                json = json.substring(0, 50) + "...";
            }
            throw new IOException("Error parsing JSON string to object: " + json, e);
        } catch (IOException e) {
            LOG.error("Error parsing JSON string to object: {}", json, e);
            if (json.length() > 60)
                json = json.substring(0, 50) + "...";
            throw e;
        }
    }

    
    
    /**
     * Create a JSON string from a object compatible or annotated with Jackson,
     * i.e: <code>
     * {"f1":2345,"f2":"Test de valor"}
     * </code>
     * @param obj
     * @return JSON string representation from object
     */
    public static String toJSON(Object obj) throws IOException {
        try {
            if (obj == null) {
                return null;
            }
            return MAPPER.writeValueAsString(obj);
        } catch (IOException e) {
            LOG.error("Error formating JSON from object: {}", obj, e);
            throw e;
        }
    }
}


