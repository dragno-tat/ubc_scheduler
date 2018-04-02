package com.dragno.rest.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
    @Override
    public LocalTime deserialize(JsonParser jsonParser,
            DeserializationContext deserializationContext) throws IOException {
        try {
            return LocalTime.parse(jsonParser.getText());
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
