package com.at.spring.dozer;

import java.util.Arrays;
import java.util.Date;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.at.spring.dozer.dto.NotSameAttributeDto;
import com.at.spring.dozer.po.NotSameAttribute;

import lombok.extern.slf4j.Slf4j;

@Configuration
//search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring.dozer")
@Slf4j
public class SpringDozerMain {
    
    @Autowired
    private DozerBeanMapper dozerBeanMapper;
    
    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        DozerBeanMapper dozerBeanMapper = new DozerBeanMapper(Arrays.asList("dozer/dozer-mapping.xml"));
        return dozerBeanMapper;
    }
    
    public NotSameAttributeDto po2dto(NotSameAttribute notSameAttribute) {
        NotSameAttributeDto notSameAttributeDto =  
                dozerBeanMapper.map(notSameAttribute, NotSameAttributeDto.class);
        return notSameAttributeDto;
    }
    

    public NotSameAttribute dto2po(NotSameAttributeDto notSameAttributeDto) {
        NotSameAttribute notSameAttribute =  
                dozerBeanMapper.map(notSameAttributeDto, NotSameAttribute.class);
        return notSameAttribute;
    }
    
    public static void main(String[] args) {
        log.debug("Enterring Main.");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringDozerMain.class);

        // 默认 bean 的名字
        SpringDozerMain springDozerMain = context.getBean(SpringDozerMain.class);
        
        NotSameAttribute notSameAttribute = new NotSameAttribute();
        notSameAttribute.setId(1234);
        notSameAttribute.setName("myname");
        notSameAttribute.setNoMatchedNameFromB("noMatchedNameFromB");
        notSameAttribute.setUserId(567890L);
        notSameAttribute.setBirthDate(new Date());
        notSameAttribute.setDateOnBoard(new Date());
        log.info("notSameAttribute org: '{}'", notSameAttribute);
        
        NotSameAttributeDto notSameAttributeDto = springDozerMain.po2dto(notSameAttribute);
        
        log.info("notSameAttributeDto: '{}'", notSameAttributeDto);

        NotSameAttribute notSameAttribute2 = springDozerMain.dto2po(notSameAttributeDto);
        
        log.info("notSameAttribute tran back: '{}'", notSameAttribute2);
    }
}
