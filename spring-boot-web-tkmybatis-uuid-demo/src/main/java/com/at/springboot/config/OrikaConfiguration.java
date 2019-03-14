package com.at.springboot.config;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.at.springboot.mybatis.dto.UuidUserDto;
import com.at.springboot.mybatis.po.UuidUser;

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


    @Bean
    public MapperFacade orikaMapperFacade() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        ConverterFactory converterFactory  = mapperFactory.getConverterFactory();
        
        // global default date to string converter
        converterFactory.registerConverter(new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        // named converter
        converterFactory.registerConverter("dateFull", new DateToStringConverter("yyyy-MM-dd"));
        converterFactory.registerConverter("dateTimeFull", new DateToStringConverter("yyyy-MM-dd HH:mm:ss"));
        

        converterFactory.registerConverter("bidConverter", new BidirectionalConverter<byte[], String>() {
            @Override
            public String convertTo(byte[] bidBytes, Type<String> type, MappingContext mappingContext) {
                log.debug("bidBytes: '{}'", bidBytes);
                if(bidBytes == null) {
                    return null;
                }
                boolean isToLowerCase = true;
                String bidString = Hex.encodeHexString(bidBytes, isToLowerCase);
                log.debug("bidString: '{}'", bidString);
                return bidString;
            }
            @Override
            public byte[] convertFrom(String bidString, Type<byte[]> type, MappingContext mappingContext) {
                log.debug("bidString: '{}'", bidString);
                if(bidString == null) {
                    return null;
                }
                byte[] bidBytes;
                try {
                    bidBytes = Hex.decodeHex(bidString);
                } catch (DecoderException e) {
                    log.error("Hex.decodeHex('{}') failed.", bidString, e);
                    bidBytes = new byte[0];
                }
                return bidBytes;
            }
        });
        
        mapperFactory
            .classMap(UuidUser.class, UuidUserDto.class)
            .byDefault()
            .fieldMap("bid").converter("bidConverter")
            .add()
            .register()
            ;

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
        return mapperFactory.getMapperFacade();
    }
    
}
