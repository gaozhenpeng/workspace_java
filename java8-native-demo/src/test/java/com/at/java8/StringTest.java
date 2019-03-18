package com.at.java8;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringTest {
    @Test
    public void testString() {
        List<Integer> idList = Arrays.asList(1,2,3,4,5);
        
        log.info(String.format("'%s'", idList));
    }
}
