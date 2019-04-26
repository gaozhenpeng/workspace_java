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
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalDateTimeMain {
    
    public static void main(String[] args){
        localDateTimeOps();
        date2LocalDateTime();
        localDateTime2Date();
        
    }

    private static void localDateTime2Date() {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);

        Date date = Date.from(zonedDateTime.toInstant());
        
        log.info("localDateTime: '{}', date: '{}'", localDateTime, date);
    }

    private static void date2LocalDateTime() {
        Date date = new Date();
        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        
        log.info("date: '{}', instant: '{}', localDateTime: '{}'", date, instant, localDateTime);
    }

    private static void localDateTimeOps() {
        LocalDate localDate = LocalDate.of(2014, Month.JUNE, 10);
        int year = localDate.getYear(); // 2014
        Month month = localDate.getMonth(); // 6月
        int dayOfMonth = localDate.getDayOfMonth(); // 10
        DayOfWeek dayOfWeek = localDate.getDayOfWeek(); // 星期二
        int lengthOfMonth = localDate.lengthOfMonth(); // 30 （6月份的天数）
        boolean isLeapYear = localDate.isLeapYear(); // false （不是闰年）
        
        log.info("localDate: '{}'", localDate);
        
        log.info("year: '{}', month: '{}' '{}', dayOfMonth: '{}', dayOfWeek: '{}', lengthOfMonth: '{}', isLeapYear: '{}'"
                , year, month.getValue(), month.name(), dayOfMonth, dayOfWeek, lengthOfMonth, isLeapYear);
        
        // '2011-12-03', 年-月-日
        log.info("localDate.format(DateTimeFormatter.ISO_LOCAL_DATE): '{}'", localDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        
        // '20111203', 年月日, 用于程序操作
        log.info("localDate.format(DateTimeFormatter.BASIC_ISO_DATE): '{}'", localDate.format(DateTimeFormatter.BASIC_ISO_DATE));
        
        
        LocalDateTime localDateTime = LocalDateTime.of(2014, Month.APRIL, 20, 13, 24, 28);
        log.info("LocalDateTime.of(2014, Month.APRIL, 20, 13, 24, 28): '{}'", localDateTime);
        
        // '2011-12-03T10:15:30' 注意，没有任何时区信息
        log.info("localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME): '{}'", localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        
        log.info("Available Zone IDs: ");
        ZoneId.getAvailableZoneIds().stream().sorted().forEach(zoneId -> {log.info("    '{}'", zoneId);});
        
        ZoneId zoneId = ZoneId.of("Asia/Chongqing"); // 中国没有北京时间，只有 Asia/Chongqing(+08:05:43), PRC(+08:05:43), Hongkong(+07:36:42)
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zoneId);
        
        // '2011-12-03T10:15:30Z' 最后的 Z 表示UTC 时区
        log.info("zonedDateTime.format(DateTimeFormatter.ISO_INSTANT): '{}'", zonedDateTime.format(DateTimeFormatter.ISO_INSTANT));
        
        ZonedDateTime parsedZoneDateTime = ZonedDateTime.parse("2011-12-03T10:15:30Z");
        log.info("parsedZoneDateTime of '2011-12-03T10:15:30Z', offset: '{}'",  parsedZoneDateTime.getOffset());
        log.info("parsedZoneDateTime.toLocalDateTime(): '{}'", parsedZoneDateTime.toLocalDateTime());
        
        
        ZonedDateTime parsedZoneDateTime_withFormatter = ZonedDateTime.parse("2011-12-03T10:15:30Z", DateTimeFormatter.ISO_ZONED_DATE_TIME);
        log.info("parsedZoneDateTime_withFormatter of '2011-12-03T10:15:30Z', offset: '{}'", parsedZoneDateTime_withFormatter.getOffset());
        log.info("parsedZoneDateTime_withFormatter.toLocalDateTime(): '{}'", parsedZoneDateTime_withFormatter.toLocalDateTime());
        
        
        
        
        // instant localdatetime zoneddatetime
        LocalDateTime createdLocalDateTime_withZonedDateTime = 
                ZonedDateTime
                    .parse("2011-12-03T10:15:30Z")
                    .withZoneSameInstant(ZoneOffset.systemDefault())
                    .toLocalDateTime();
        log.info("createdLocalDateTime_withZonedDateTime: '{}'", createdLocalDateTime_withZonedDateTime.toString());
        Instant createdInstant = Instant.parse("2011-12-03T10:15:30Z");
        LocalDateTime createdLocalDateTime_withInstant = LocalDateTime.ofInstant(createdInstant, ZoneOffset.systemDefault());
        log.info("createdLocalDateTime_withInstant: '{}'", createdLocalDateTime_withInstant.toString());
        
        localDateTime = localDateTime
                            .withYear(12)
                            .withMonth(12)
                            .withDayOfMonth(12)
                            .withHour(12)
                            .withMinute(12)
                            .withSecond(12);
        log.info("localDateTime: '{}'", localDateTime);
        
        localDateTime = localDateTime.plusMinutes(12);
        log.info("localDateTime, plusMinutes(12): '{}'", localDateTime);
        
        ZonedDateTime zonedDateTime2 = localDateTime.atZone(zoneId);
        // '2011-12-03T10:15:30Z' 最后的 Z 表示UTC 时区
        log.info("zonedDateTime2.format(DateTimeFormatter.ISO_INSTANT): '{}', offset: '{}'"
                , zonedDateTime2.format(DateTimeFormatter.ISO_INSTANT), zonedDateTime2.getOffset());
    }
}