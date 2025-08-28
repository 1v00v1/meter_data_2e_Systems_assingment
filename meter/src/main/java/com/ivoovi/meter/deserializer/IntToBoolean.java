package com.ivoovi.meter.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class IntToBoolean extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();
        System.out.println("value: " + value);
        if("0".equals(value)){
            return false;
        }else if("1".equals(value)){
            return true;
        }
        return "true".equalsIgnoreCase(value);
    }
}