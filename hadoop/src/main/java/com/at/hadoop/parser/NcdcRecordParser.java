package com.at.hadoop.parser;

import org.apache.hadoop.io.Text;

public class NcdcRecordParser {
    private static final int MISSING_TEMPERATURE = 9999;
    private String year;
    private int yearInt;
    private int airTemperature;
    private String quality;
    private String stationId;

    public void parse(String record) {
        // stationid
        String usaf = record.substring(4, 10); // USAF weather station identifier
        String wban = record.substring(10, 15); // WBAN weather station identifier
        stationId = "" + usaf + "-" + wban;
        // year
        year = record.substring(15, 19);
        yearInt = Integer.parseInt(year, 10);
        // air temperature
        String airTemperatureString;
        if (record.charAt(87) == '+') { // parseInt doesn't like leading plus signs
            airTemperatureString = record.substring(88, 92);
        } else {
            airTemperatureString = record.substring(87, 92);
        }
        airTemperature = Integer.parseInt(airTemperatureString, 10);
        // quality
        quality = record.substring(92, 93);
    }

    public void parse(Text record) {
        parse(record.toString());
    }

    public boolean isValidTemperature() {
        return airTemperature != MISSING_TEMPERATURE && quality.matches("[01459]");
    }

    public boolean isMalformedTemperature() {
        return airTemperature > 1000;
    }
    public boolean isMissingTemperature() {
        return airTemperature == MISSING_TEMPERATURE;
    }

    public String getYear() {
        return year;
    }

    public int getYearInt() {
        return yearInt;
    }

    public String getStationId() {
        return stationId;
    }

    public int getAirTemperature() {
        return airTemperature;
    }
    public String getQuality() {
        return quality;
    }
}
