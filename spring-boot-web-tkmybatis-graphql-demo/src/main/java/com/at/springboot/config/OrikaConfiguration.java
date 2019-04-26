package com.at.springboot.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.DateToStringConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

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
        

        converterFactory.registerConverter(new BidirectionalConverter<LocalDateTime, String>(){

            @Override
            public String convertTo(LocalDateTime localDateTime, Type<String> destinationType, MappingContext mappingContext) {
                log.debug("source localDateTime: '{}'", localDateTime);
                if(localDateTime == null) {
                    return null;
                }
                String destinationString = dateTimeFormatter.format(localDateTime);
                log.debug("destination string: '{}'", destinationString);
                return destinationString;
            }

            @Override
            public LocalDateTime convertFrom(String string, Type<LocalDateTime> destinationType, MappingContext mappingContext) {
                log.debug("source string: '{}'", string);
                if(string == null) {
                    return null;
                }
                LocalDateTime destinationLocalDateTime = null;
                try {
                    destinationLocalDateTime = LocalDateTime.parse(string, dateTimeFormatter);
                } catch (DateTimeParseException e) {
                    log.error("LocalDateTime.parse('{}', DateTimeFormatter.ofPattern('{}')) failed.", string, DATE_TIME_PATTERN, e);
                    destinationLocalDateTime = null;
                }
                log.debug("destination localDateTime: '{}'", destinationLocalDateTime);
                return destinationLocalDateTime;
            }
            
        });
        
//      mapperFactory
//          .classMap(NotSameAttribute.class, NotSameAttributeDto.class)
////          // NOT map nulls from A to B. default: true
////          .mapNulls(false)
////          // NOT map nulls from B to A. default: true
////          .mapNullsInReverse(false)
//          // default mapping for the same names
//          .byDefault()
//          // mapping for different names
//          .field("noMatchedNameFromB", "noMatchedNameFromA")
//          .fieldMap("dateOnBoard").converter("dateFull")
//          // contine the chain operation
//          .add()
//          .register()
//          ;
        MapperFacade mapperFacade = mapperFactory.getMapperFacade();
        log.info("Leaving orikaMapperFacade()...");
        return mapperFacade;
    }
    
}
