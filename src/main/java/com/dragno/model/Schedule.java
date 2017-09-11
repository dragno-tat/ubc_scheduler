package com.dragno.model;


import com.google.common.base.MoreObjects;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.IntStream;

import static java.text.MessageFormat.format;

/**
 * Created by Anthony on 7/8/2017.
 */
public class Schedule {

    // represents 30 min intervals from 08:00 to 19:00
    public static final int INTERVALS_PER_HOUR = 2;
    public static final int EARLIER_START_HOUR = 8;
    public static final int LATEST_END_HOUR = 19;

    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;

    public Schedule() {
        monday = 0;
        tuesday = 0;
        wednesday = 0;
        thursday = 0;
        friday = 0;
    }

    public Schedule(Schedule schedule, Set<Day> days, LocalTime startTime, LocalTime endTime) {
        validateTime(startTime, endTime);
        monday = schedule.monday;
        tuesday = schedule.tuesday;
        wednesday = schedule.wednesday;
        thursday = schedule.thursday;
        friday = schedule.friday;
        days.forEach(day -> addTimeForDay(day, startTime, endTime));
    }

    public Schedule(Set<Day> days, LocalTime startTime, LocalTime endTime) {
        validateTime(startTime, endTime);
        days.forEach(day -> addTimeForDay(day, startTime, endTime));
    }

    private void validateTime(LocalTime startTime, LocalTime endTime) {
        if (startTime.getHour() < 8 || startTime.isAfter(endTime)) {
            throw new IllegalStateException("Cannot have a start time before 8am");
        }
    }

    private void addTimeForDay(Day day, LocalTime startTime, LocalTime endTime) {
        switch (day) {
            case MON:
                monday = getAndAddTime(monday, startTime, endTime);
                break;
            case TUE:
                tuesday = getAndAddTime(tuesday, startTime, endTime);
                break;
            case WED:
                wednesday = getAndAddTime(wednesday, startTime, endTime);
                break;
            case THU:
                thursday = getAndAddTime(thursday, startTime, endTime);
                break;
            case FRI:
                friday = getAndAddTime(friday, startTime, endTime);
                break;
        }
    }

    private int getAndAddTime(int day, LocalTime startTime, LocalTime endTime) {
        int startIndex = getTimeIndex(startTime);
        int endIndex = getTimeIndex(endTime);
        for (int i = startIndex; i < endIndex; i++) {
            day ^= 1 << i;
        }
        return day;
    }

    private int getTimeIndex(LocalTime time) {
        int timeIndex = (time.getHour() - EARLIER_START_HOUR) * INTERVALS_PER_HOUR;
        if (time.getMinute() != 0) {
            timeIndex++;
        }
        return timeIndex;
    }

    public boolean intersects(Schedule schedule) {
        return (monday & schedule.monday) + (tuesday & schedule.tuesday) + (wednesday & schedule.wednesday) +
                (thursday & schedule.thursday) + (friday & schedule.friday) != 0;
    }

    public Schedule or(Schedule schedule) {
        Schedule ret = new Schedule();
        ret.monday = monday | schedule.monday;
        ret.tuesday = tuesday | schedule.tuesday;
        ret.wednesday = wednesday | schedule.wednesday;
        ret.thursday = thursday | schedule.thursday;
        ret.friday = friday | schedule.friday;
        return ret;
    }

    public int cardinality() {
        return Integer.bitCount(monday) + Integer.bitCount(tuesday) + Integer.bitCount(wednesday) +
                Integer.bitCount(thursday) + Integer.bitCount(friday);
    }

    public int cardinality(Set<Day> days) {
        int cardinality = 0;
        for (Day day : days) {
            switch (day) {
                case MON:
                    cardinality += Integer.bitCount(monday);
                    break;
                case TUE:
                    cardinality += Integer.bitCount(tuesday);
                    break;
                case WED:
                    cardinality += Integer.bitCount(wednesday);
                    break;
                case THU:
                    cardinality += Integer.bitCount(thursday);
                    break;
                case FRI:
                    cardinality += Integer.bitCount(friday);
                    break;
            }
        }
        return cardinality;
    }

    public boolean endsBefore(LocalTime time){
        return getLatestEndingTimeIndex() <= getTimeIndex(time);
    }

    private int getLatestEndingTimeIndex() {
        return IntStream.of(getLatestEndingTimeIndex(monday), getLatestEndingTimeIndex(tuesday),
                getLatestEndingTimeIndex(wednesday), getLatestEndingTimeIndex(thursday),
                getLatestEndingTimeIndex(friday)).max().getAsInt();
    }

    private int getLatestEndingTimeIndex(int day) {
        if(day == 0){
            return 0;
        }
        int counter = 0;
        while((day & Integer.MIN_VALUE) != Integer.MIN_VALUE){
            counter++;
            day <<= 1;
        }
        return 32 - counter;
    }

    public boolean startsAfter(LocalTime time){
        return getEarliestStartingTimeIndex() >= getTimeIndex(time);
    }

    private int getEarliestStartingTimeIndex() {
        return IntStream.of(getEarliestStartingTimeIndex(monday), getEarliestStartingTimeIndex(tuesday),
                getEarliestStartingTimeIndex(wednesday), getEarliestStartingTimeIndex(thursday),
                getEarliestStartingTimeIndex(friday)).min().getAsInt();
    }

    private int getEarliestStartingTimeIndex(int day) {
        if(day == 0){
            return 32;
        }
        int counter = 0;
        while((day & 1) != 1){
            counter++;
            day >>= 1;
        }
        return counter;
    }

    public int getDay(Day day){
        switch(day){
            case MON:
                return monday;
            case TUE:
                return tuesday;
            case WED:
                return wednesday;
            case THU:
                return thursday;
            case FRI:
                return friday;
        }
        throw new IllegalStateException(format("Could not find Day {0}", day));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Schedule schedule = (Schedule) o;

        return monday == schedule.monday && tuesday == schedule.tuesday && wednesday == schedule.wednesday &&
                thursday == schedule.thursday && friday == schedule.friday;
    }

    @Override
    public int hashCode() {
        int result = monday;
        result = 31 * result + tuesday;
        result = 31 * result + wednesday;
        result = 31 * result + thursday;
        result = 31 * result + friday;
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("monday=", formatDay(monday))
                          .add("tuesday=", formatDay(tuesday))
                          .add("wednesday=", formatDay(wednesday))
                          .add("thursday=", formatDay(thursday))
                          .add("friday=", formatDay(friday))
                          .toString();
    }

    private String formatDay(int day) {
        return insertSpaceOnEven(new StringBuilder(Integer.toBinaryString(day)).reverse()).toString();
    }

    private StringBuilder insertSpaceOnEven(StringBuilder builder) {
        for(int i = builder.length() - 1; i >= 0; i--){
            if(i % 2 == 0){
                builder.insert(i, " ");
            }
        }
        return builder;
    }
}
