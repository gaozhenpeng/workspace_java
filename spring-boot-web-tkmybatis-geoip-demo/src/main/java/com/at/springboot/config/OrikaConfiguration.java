package com.at.springboot.config;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.DateToStringConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Slf4j
@Configuration
public class OrikaConfiguration {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    @Bean
    public MapperFacade orikaMapperFacade() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory  = mapperFactory.getConverterFactory();
        
        // global default date to string converter
        converterFactory.registerConverter(new DateToStringConverter(DATE_TIME_PATTERN));
        // named converter
        converterFactory.registerConverter("dateFull", new DateToStringConverter(DATE_PATTERN));
        converterFactory.registerConverter("dateTimeFull", new DateToStringConverter(DATE_TIME_PATTERN));
        

//        converterFactory.registerConverter("bidConverter", new BidirectionalConverter<byte[], String>() {
//            @Override
//            public String convertTo(byte[] bidBytes, Type<String> type, MappingContext mappingContext) {
//                log.debug("bidBytes: '{}'", bidBytes);
//                if(bidBytes == null) {
//                    return null;
//                }
//                boolean isToLowerCase = true;
//                String bidString = Hex.encodeHexString(bidBytes, isToLowerCase);
//                log.debug("bidString: '{}'", bidString);
//                return bidString;
//            }
//            @Override
//            public byte[] convertFrom(String bidString, Type<byte[]> type, MappingContext mappingContext) {
//                log.debug("bidString: '{}'", bidString);
//                if(bidString == null) {
//                    return null;
//                }
//                byte[] bidBytes;
//                try {
//                    bidBytes = Hex.decodeHex(bidString);
//                } catch (DecoderException e) {
//                    log.error("Hex.decodeHex('{}') failed.", bidString, e);
//                    bidBytes = new byte[0];
//                }
//                return bidBytes;
//            }
//        });
//        
//
//
//        converterFactory.registerConverter(new BidirectionalConverter<LocalDateTime, String>(){
//
//            @Override
//            public String convertTo(LocalDateTime localDateTime, Type<String> destinationType, MappingContext mappingContext) {
//                log.debug("source localDateTime: '{}'", localDateTime);
//                if(localDateTime == null) {
//                    return null;
//                }
//                String destinationString = dateTimeFormatter.format(localDateTime);
//                log.debug("destination string: '{}'", destinationString);
//                return destinationString;
//            }
//
//            @Override
//            public LocalDateTime convertFrom(String string, Type<LocalDateTime> destinationType, MappingContext mappingContext) {
//                log.debug("source string: '{}'", string);
//                if(string == null) {
//                    return null;
//                }
//                LocalDateTime destinationLocalDateTime = null;
//                try {
//                    destinationLocalDateTime = LocalDateTime.parse(string, dateTimeFormatter);
//                } catch (DateTimeParseException e) {
//                    log.error("LocalDateTime.parse('{}', DateTimeFormatter.ofPattern('{}')) failed.", string, DATE_TIME_PATTERN, e);
//                    destinationLocalDateTime = null;
//                }
//                log.debug("destination localDateTime: '{}'", destinationLocalDateTime);
//                return destinationLocalDateTime;
//            }
//            
//        });
//        
//        mapperFactory
//            .classMap(UuidUser.class, GeoipDto.class)
//            .byDefault()
//            .fieldMap("bid").converter("bidConverter")
//            .add()
//            .register()
//            ;

        return mapperFactory.getMapperFacade();
    }
    
}
