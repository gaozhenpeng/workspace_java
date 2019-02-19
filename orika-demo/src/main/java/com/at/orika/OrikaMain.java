package com.at.orika;

import java.util.Date;

import com.at.orika.dto.NotSameAttributeDto;
import com.at.orika.po.NotSameAttribute;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.DateToStringConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Slf4j
public class OrikaMain {
    private static final MapperFacade mapperFacade;
    static {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory  = mapperFactory.getConverterFactory();
        
        // global default date to string converter
        converterFactory.registerConverter(new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        // named converter
        converterFactory.registerConverter("dateFull", new DateToStringConverter("yyyy-MM-dd"));
        converterFactory.registerConverter("dateTimeFull", new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        
        mapperFactory
            .classMap(NotSameAttribute.class, NotSameAttributeDto.class)
//            // NOT map nulls from A to B. default: true
//            .mapNulls(false)
//            // NOT map nulls from B to A. default: true
//            .mapNullsInReverse(false)
            // default mapping for the same names
            .byDefault()
            // mapping for different names
            .field("noMatchedNameFromB", "noMatchedNameFromA")
            .fieldMap("dateOnBoard").converter("dateFull")
            // contine the chain operation
            .add()
            .register()
            ;
        mapperFacade = mapperFactory.getMapperFacade();
    }
    
    public static void main(String[] args) {
        
        
        NotSameAttribute notSameAttribute = new NotSameAttribute();
        notSameAttribute.setId(1234);
        notSameAttribute.setName("myname");
        notSameAttribute.setNoMatchedNameFromB("noMatchedNameFromB");
        notSameAttribute.setUserId(567890L);
        notSameAttribute.setBirthDate(new Date());
        notSameAttribute.setDateOnBoard(new Date());
        log.info("notSameAttribute org: '{}'", notSameAttribute);
        
        NotSameAttributeDto notSameAttributeDto =  
                mapperFacade.map(notSameAttribute, NotSameAttributeDto.class);
        

        log.info("notSameAttributeDto: '{}'", notSameAttributeDto);

        NotSameAttribute notSameAttribute2 =  
                mapperFacade.map(notSameAttributeDto, NotSameAttribute.class);
        

        log.info("notSameAttribute tran back: '{}'", notSameAttribute2);
    }
}
