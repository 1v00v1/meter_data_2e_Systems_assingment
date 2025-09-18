package com.ivoovi.meter.utility;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;

public class JsonFilterUtil {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
    }


    public static Map<String, Object> filterObject(Object object, String fields) throws JsonProcessingException {
        if (fields == null || fields.isBlank()) {

            return mapper.convertValue(object, Map.class);
        }

        Set<String> fieldSet = Stream.of(fields.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        // Temporary MixIn class for filtering
        @JsonFilter("dynamicFilter")
        class MixIn {}

        mapper.addMixIn(object.getClass(), MixIn.class);

        SimpleFilterProvider filters = new SimpleFilterProvider()
                .addFilter("dynamicFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fieldSet));

        String json = mapper.writer(filters).writeValueAsString(object);
        return mapper.readValue(json, Map.class);
    }
}
