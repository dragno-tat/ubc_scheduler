package com.dragno.rest.util;

import com.dragno.rest.service.model.Day;
import com.dragno.rest.service.model.Schedule;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;

public class ScheduleSerializer extends JsonSerializer<Schedule>{
    @Override
    public void serialize(Schedule schedule, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        for(Day day : Day.values()) {
            int dayNum = schedule.getDay(day);
            if(dayNum != 0) {
                String binaryString = new StringBuilder(Integer.toBinaryString(dayNum)).reverse().toString();
                int first = binaryString.indexOf('1');
                int last = binaryString.lastIndexOf('1');
                LocalTime startTime = LocalTime.of(Schedule.EARLIEST_START_HOUR + first / 2,(first % 2) * 30);
                LocalTime endTime = startTime.plusMinutes(30 * (last - first + 1));
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("day", day.toString());
                jsonGenerator.writeStringField("startTime", startTime.toString());
                jsonGenerator.writeStringField("endTime", endTime.toString());
                jsonGenerator.writeEndObject();
            }
        }
        jsonGenerator.writeEndArray();
    }
}
