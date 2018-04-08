package com.at.spring_boot.swagger.controller;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.at.spring_boot.swagger.controller.RootController;
import com.at.spring_boot.swagger.model.Greeting;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RootControllerTest {

    @Autowired
    private RootController controller;

    @Test
    public void testHome() {
        assertThat(controller).isNotNull();
        Greeting greeting = controller.home("world");
        assertThat(greeting).isNotNull();
        assertThat(greeting.getName()).isEqualToIgnoringCase("world");
        assertThat(greeting.getMessage()).isEqualToIgnoringCase("hello world!");
    }

}
