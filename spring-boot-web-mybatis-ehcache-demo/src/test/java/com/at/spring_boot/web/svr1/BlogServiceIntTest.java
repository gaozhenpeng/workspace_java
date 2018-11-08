package com.at.spring_boot.web.svr1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.at.spring_boot.mybatis.dto.BlogDto;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Slf4j
public class BlogServiceIntTest {
    @Autowired
    @Qualifier("blogService1")
    private BlogService blogService;
    
    
    @Test
    public void testListAll() {
        List<BlogDto> blogDtos = blogService.list(null);
        log.info("blogDtos: '{}'", blogDtos);
        assertNotNull("blogService.list(null) return null", blogDtos);
        assertFalse("blogDtos.isEmpty() is true", blogDtos.isEmpty());
    }
}
