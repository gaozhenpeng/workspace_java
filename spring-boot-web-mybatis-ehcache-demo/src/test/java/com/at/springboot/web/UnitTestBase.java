package com.at.springboot.web;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.DateToStringConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@RunWith(MockitoJUnitRunner.class)
@Ignore
public class UnitTestBase {
    protected MapperFacade orikaMapperFacade;
    
    public UnitTestBase() {
        super();
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory  = mapperFactory.getConverterFactory();
                
        // global default date to string converter
        converterFactory.registerConverter(new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        // named converter
        converterFactory.registerConverter("dateFull", new DateToStringConverter("yyyy-MM-dd"));
        converterFactory.registerConverter("dateTimeFull", new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        orikaMapperFacade = mapperFactory.getMapperFacade();
    }
}
