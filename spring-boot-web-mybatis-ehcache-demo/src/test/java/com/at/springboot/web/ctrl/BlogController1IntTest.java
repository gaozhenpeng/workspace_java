package com.at.springboot.web.ctrl;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import com.at.springboot.mybatis.dto.BlogDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class BlogController1IntTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Value("#{'${server.servlet.context-path}'.trim()}") 
    private String contextPath;
    
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Test
    public void testListAll() throws JsonParseException, JsonMappingException, IOException {
        Map<String, ?> variables = new HashMap<>();
        String blogsStr = testRestTemplate
                    .getForObject(
                            "http://localhost:" + port + contextPath + "/blog1"
                            , String.class
                            , variables);
        log.info("blogsStr: '{}'", blogsStr);
        assertNotNull("blogsStr is null", blogsStr);
        BlogDto[] blogDtos = OBJECT_MAPPER.readValue(blogsStr, BlogDto[].class);
        assertNotNull("blogDtos is null", blogDtos);
        for(int i = 0 ; i < blogDtos.length ; i++) {
            BlogDto blogDto = blogDtos[i];
            assertNotNull("blogId is null, '"+blogDto+"'", blogDto.getBlogId());
        }
    }

}


