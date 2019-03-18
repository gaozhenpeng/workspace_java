package com.at.java8;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LocalTimeMain {
    
    public static void main(String[] args){
        LocalDate localDate = LocalDate.of(2014, Month.JUNE, 10);
        int year = localDate.getYear(); // 2014
        Month month = localDate.getMonth(); // 6月
        int dayOfMonth = localDate.getDayOfMonth(); // 10
        DayOfWeek dayOfWeek = localDate.getDayOfWeek(); // 星期二
        int lengOfMonth = localDate.lengthOfMonth(); // 30 （6月份的天数）
        boolean isLeapYear = localDate.isLeapYear(); // false （不是闰年）
        
        StringBuilder sb = new StringBuilder();
        sb.append("LocalDate.toString(): ");
        sb.append(localDate.toString());
        sb.append("; ");
        System.out.println(sb.toString());
        
        sb = new StringBuilder();
        sb.append("year: ");
        sb.append(year);
        sb.append("; ");
        sb.append("month: ");
        sb.append(month.getValue());
        sb.append(",");
        sb.append(month.name());
        sb.append("; ");
        sb.append("DayOfMonth: ");
        sb.append(dayOfMonth);
        sb.append("; ");
        sb.append("DayOfWeek: ");
        sb.append(dayOfWeek);
        sb.append("; ");
        sb.append("lengthOfMonth: ");
        sb.append(lengOfMonth);
        sb.append("; ");
        sb.append("isLeepYear: ");
        sb.append(isLeapYear);
        sb.append("; ");
        System.out.println(sb.toString());
        
        sb = new StringBuilder();
        sb.append(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)); // '2011-12-03', 年-月-日
        System.out.println(sb.toString());
        
        sb = new StringBuilder();
        sb.append(localDate.format(DateTimeFormatter.BASIC_ISO_DATE)); // '20111203', 年月日, 用于程序操作
        System.out.println(sb.toString());
        
        LocalDateTime localDateTime = LocalDateTime.of(2014, Month.APRIL, 20, 13, 24, 28);
        sb = new StringBuilder();
        sb.append("LocalDateTime.toString(): ");
        sb.append(localDateTime.toString());
        sb.append("; ");
        System.out.println(sb.toString());
        
        sb = new StringBuilder();
        sb.append(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // '2011-12-03T10:15:30' 注意，没有任何时区信息
        System.out.println(sb.toString());
        
        sb = new StringBuilder();
        sb.append("Available Zone IDs: ").append("\n");
        List<String> zoneIDs = new ArrayList<String>();
        for(String zoneStr : ZoneId.getAvailableZoneIds()){
            zoneIDs.add(zoneStr);
        }
        zoneIDs.sort(null);
        for(String zoneID : zoneIDs){
            sb.append(zoneID).append("; ").append("\n");
        }
        System.out.println(sb.toString());
        
        ZoneId zoneId = ZoneId.of("Asia/Chongqing"); // 中国没有北京时间，只有 Asia/Chongqing(+08:05:43), PRC(+08:05:43), Hongkong(+07:36:42)
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        
        sb = new StringBuilder();
        sb.append(zonedDateTime.format(DateTimeFormatter.ISO_INSTANT)); // '2011-12-03T10:15:30Z' 最后的 Z 表示UTC 时区
        System.out.println(sb.toString());
        
        ZonedDateTime parsedZoneDateTime = ZonedDateTime.parse("2011-12-03T10:15:30Z");
        System.out.println("parsedZoneDateTime of '2011-12-03T10:15:30Z', offset: " + parsedZoneDateTime.getOffset());
        System.out.println("parsedZoneDateTime.toLocalDateTime(): " + parsedZoneDateTime.toLocalDateTime());
        
        
        ZonedDateTime parsedZoneDateTime_withFormatter = ZonedDateTime.parse("2011-12-03T10:15:30Z", DateTimeFormatter.ISO_ZONED_DATE_TIME);
        System.out.println("parsedZoneDateTime_withFormatter of '2011-12-03T10:15:30Z', offset: " + parsedZoneDateTime_withFormatter.getOffset());
        System.out.println("parsedZoneDateTime_withFormatter.toLocalDateTime(): " + parsedZoneDateTime_withFormatter.toLocalDateTime());
        
        
        
        
        // instant localdatetime zoneddatetime
        LocalDateTime createdLocalDateTime_withZonedDateTime = ZonedDateTime.parse("2011-12-03T10:15:30Z")
                                                                    .withZoneSameInstant(ZoneOffset.systemDefault())
                                                                    .toLocalDateTime();
        System.out.println("createdLocalDateTime_withZonedDateTime: " + createdLocalDateTime_withZonedDateTime.toString());
        Instant createdInstant = Instant.parse("2011-12-03T10:15:30Z");
        LocalDateTime createdLocalDateTime_withInstant = LocalDateTime.ofInstant(createdInstant, ZoneOffset.systemDefault());
        System.out.println("createdLocalDateTime_withInstant: " + createdLocalDateTime_withInstant.toString());
        
        localDateTime = localDateTime
                            .withYear(12)
                            .withMonth(12)
                            .withDayOfMonth(12)
                            .withHour(12)
                            .withMinute(12)
                            .withSecond(12);
        sb = new StringBuilder();
        sb.append("LocalDateTime.toString(): ");
        sb.append(localDateTime.toString());
        sb.append("; ");
        System.out.println(sb.toString());
        
        localDateTime = localDateTime.plusMinutes(12);
        sb = new StringBuilder();
        sb.append("LocalDateTime.toString(): ");
        sb.append(localDateTime.toString());
        sb.append("; ");
        System.out.println(sb.toString());
        ZonedDateTime zonedDateTime2 = localDateTime.atZone(zoneId);
        sb = new StringBuilder();
        sb.append(zonedDateTime2.format(DateTimeFormatter.ISO_INSTANT)); // '2011-12-03T10:15:30Z' 最后的 Z 表示UTC 时区
        sb.append("; ").append("offset: ").append(zonedDateTime2.getOffset()).append(";");
        System.out.println(sb.toString());
        
        
    }
}