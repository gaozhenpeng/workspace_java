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
    @Test
    public void testRegex() {
        String a = "2019-06-05T01:57:20.000Z {name=node6.xndx} NQ_AC %%10STAMGR/6/STAMGR_ROAM_SUCCESS: Client 5068-0aae-31e8 roamed from BSSID 3cf5-cc82-9d70 on AP ny4s_417 of AC IP 127.0.0.1 to BSSID 3cf5-cc82-7fd0 on AP ny4s_415 of AC IP 127.0.0.1 successfully.";
        
        log.info(a.replaceFirst(".*IP ([0-9.]+) successfully.$", "$1"));
    }
}
