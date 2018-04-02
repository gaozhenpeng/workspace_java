package com.at.spring.orika;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.at.spring.orika.dto.NotSameAttributeDto;
import com.at.spring.orika.po.NotSameAttribute;

import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.converter.builtin.DateToStringConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;

@Configuration
//search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring.orika")
@Slf4j
public class SpringOrikaMain {
    
    @Autowired
    private MapperFacade mapperFacade;
    
    @Bean
    public MapperFacade orikaMapperFacader() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory  = mapperFactory.getConverterFactory();
        
        // global default date to string converter
        converterFactory.registerConverter(new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        // named converter
        converterFactory.registerConverter("dateFull", new DateToStringConverter("yyyy-MM-dd"));
        converterFactory.registerConverter("dateTimeFull", new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        
        mapperFactory
            .classMap(NotSameAttribute.class, NotSameAttributeDto.class)
            // NOT map nulls from A to B. default: true
            .mapNulls(false)
            // NOT map nulls from B to A. default: true
            .mapNullsInReverse(false)
            // default mapping for the same names
            .byDefault()
            // mapping for different names
            .field("noMatchedNameFromB", "noMatchedNameFromA")
            .fieldMap("dateOnBoard").converter("dateFull").add()
            .register()
            ;
        return mapperFactory.getMapperFacade();
    }
    
    public NotSameAttributeDto po2dto(NotSameAttribute notSameAttribute) {
        NotSameAttributeDto notSameAttributeDto =  
                mapperFacade.map(notSameAttribute, NotSameAttributeDto.class);
        return notSameAttributeDto;
    }
    

    public NotSameAttribute dto2po(NotSameAttributeDto notSameAttributeDto) {
        NotSameAttribute notSameAttribute =  
                mapperFacade.map(notSameAttributeDto, NotSameAttribute.class);
        return notSameAttribute;
    }
    
    public static void main(String[] args) {
        log.debug("Enterring Main.");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringOrikaMain.class);

        // 默认 bean 的名字
        SpringOrikaMain springOrikaMain = context.getBean(SpringOrikaMain.class);
        
        NotSameAttribute notSameAttribute = new NotSameAttribute();
        notSameAttribute.setId(1234);
        notSameAttribute.setName("myname");
        notSameAttribute.setNoMatchedNameFromB("noMatchedNameFromB");
        notSameAttribute.setUserId(567890L);
        notSameAttribute.setBirthDate(new Date());
        notSameAttribute.setDateOnBoard(new Date());
        log.info("notSameAttribute org: '{}'", notSameAttribute);
        
        NotSameAttributeDto notSameAttributeDto = springOrikaMain.po2dto(notSameAttribute);
        
        log.info("notSameAttributeDto: '{}'", notSameAttributeDto);

        NotSameAttribute notSameAttribute2 = springOrikaMain.dto2po(notSameAttributeDto);
        
        log.info("notSameAttribute tran back: '{}'", notSameAttribute2);
    }
}
