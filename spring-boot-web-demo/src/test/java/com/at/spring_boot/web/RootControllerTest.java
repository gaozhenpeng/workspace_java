package com.at.spring_boot.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.at.spring_boot.web.RootController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RootControllerTest {

    @Autowired
    private RootController controller;

    @Test
    public void testHome() {
        assertThat(controller).isNotNull();
        assertThat(controller.home()).isEqualToIgnoringCase("hello world!");
    }

}
