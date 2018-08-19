package com.at.spring_boot.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RootControllerIntTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Value("#{'${server.servlet.context-path}'.trim()}") 
    private String contextPath;
    @Test
    public void testHome() {
        assertThat(
                testRestTemplate
                    .getForObject("http://localhost:" + port + contextPath + "/", String.class))
            .isEqualToIgnoringCase("hello world!")
            ;
    }

}
