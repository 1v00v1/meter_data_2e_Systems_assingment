package com.ivoovi.meter.utility;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;

@Component

public class JsonFilterUtil {

    private  final ObjectMapper mapper;

 private JsonFilterUtil(ObjectMapper mapper) {
     this.mapper = mapper;
 }


    public Map<String, Object> filterObject(Object object, String fields) throws JsonProcessingException {

        if (object == null) {
            return Map.of();
        }

        if (fields == null || fields.isBlank()) {

            return mapper.convertValue(object, new TypeReference<>() {});
        }

        Set<String> fieldSet = Stream.of(fields.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        // Temporary MixIn class for filtering
        @JsonFilter("dynamicFilter")
        class MixIn {}

        ObjectMapper localMapper = mapper.copy();
        localMapper.addMixIn(object.getClass(), MixIn.class);

        SimpleFilterProvider filters = new SimpleFilterProvider()
                .addFilter("dynamicFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fieldSet))
                .setFailOnUnknownId(false);

        String json = localMapper.writer(filters).writeValueAsString(object);
        return localMapper.readValue(json, new TypeReference<>() {});
    }
}
