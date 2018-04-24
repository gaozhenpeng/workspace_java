package com.at.hadoop.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.Text;

public class NcdcRecordParser {
    private static final int MISSING_TEMPERATURE = 9999;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
    private String year;
    private int yearInt;
    private int airTemperature;
    private String quality;
    private String stationId;
    private Date observationDate;

    public void parse(String record) {
        // stationid
        String usaf = record.substring(4, 10); // USAF weather station identifier
        String wban = record.substring(10, 15); // WBAN weather station identifier
        stationId = "" + usaf + "-" + wban;
        
        // observation datetime
        String observationDateTimeStr = record.substring(15, 27);
        try {
            observationDate = simpleDateFormat.parse(observationDateTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
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

    public Date getObservationDate() {
        return observationDate;
    }

    public void setObservationDate(Date observationDate) {
        this.observationDate = observationDate;
    }
}
