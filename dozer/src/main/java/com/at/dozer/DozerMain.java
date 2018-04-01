package com.at.dozer;

import java.util.Arrays;
import java.util.Date;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.at.dozer.dto.NotSameAttributeDto;
import com.at.dozer.po.NotSameAttribute;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DozerMain {
    private static final Mapper mapper = new DozerBeanMapper(Arrays.asList("dozer/dozer-mapping.xml"));
    public static void main(String[] args) {
        
        
        NotSameAttribute notSameAttribute = new NotSameAttribute();
        notSameAttribute.setId(1234);
        notSameAttribute.setName("myname");
        notSameAttribute.setNoMatchedNameFromB("noMatchedNameFromB");
        notSameAttribute.setUserId(567890L);
        notSameAttribute.setBirthDate(new Date());
        log.info("notSameAttribute org: '{}'", notSameAttribute);
        
        NotSameAttributeDto notSameAttributeDto =  
            mapper.map(notSameAttribute, NotSameAttributeDto.class);
        

        log.info("notSameAttributeDto: '{}'", notSameAttributeDto);

        NotSameAttribute notSameAttribute2 =  
            mapper.map(notSameAttributeDto, NotSameAttribute.class);
        

        log.info("notSameAttribute tran back: '{}'", notSameAttribute2);
    }
}
