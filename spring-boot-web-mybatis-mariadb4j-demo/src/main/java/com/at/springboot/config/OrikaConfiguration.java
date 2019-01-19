package com.at.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.DateToStringConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Configuration
public class OrikaConfiguration {


    @Bean
    public MapperFacade orikaMapperFacade() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory  = mapperFactory.getConverterFactory();
        
        // global default date to string converter
        converterFactory.registerConverter(new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        // named converter
        converterFactory.registerConverter("dateFull", new DateToStringConverter("yyyy-MM-dd"));
        converterFactory.registerConverter("dateTimeFull", new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        
//        mapperFactory
//            .classMap(NotSameAttribute.class, NotSameAttributeDto.class)
////            // NOT map nulls from A to B. default: true
////            .mapNulls(false)
////            // NOT map nulls from B to A. default: true
////            .mapNullsInReverse(false)
//            // default mapping for the same names
//            .byDefault()
//            // mapping for different names
//            .field("noMatchedNameFromB", "noMatchedNameFromA")
//            .fieldMap("dateOnBoard").converter("dateFull")
//            // contine the chain operation
//            .add()
//            .register()
//            ;
        return mapperFactory.getMapperFacade();
    }
    
}
