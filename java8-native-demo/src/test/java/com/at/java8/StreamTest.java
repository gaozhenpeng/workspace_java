package com.at.java8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamTest {
    private List<MyPo> myPos;
    
    @Before
    public void setup() {
        myPos = new ArrayList<>();
        for(int i = 1; i <= 10 ; i++) {
            MyPo myPo = new MyPo();
            myPo.setId(i*100L);
            myPo.setName("myname_" + i);
            myPo.setRemark("remark" + i%3);
            myPos.add(myPo);
        }
    }
    
    
    @Test
    public void testList2List() {
        List<String> names = new ArrayList<>();
        if(myPos != null) {
            names.addAll(
                myPos.stream().parallel()
                    // filter out the nulls
                    .filter(v->v!=null)
                    // get name, chain operations can be followed
                    .map(mp -> mp.getName())
                    .collect(Collectors.toList())
                );
        }
        log.info("names: '{}'", names);
    }
    
    @Test
    public void testList2Map() {
        Map<Long, MyPo> id2MyPo = new HashMap<>();
        if(myPos != null) {
            id2MyPo.putAll(
                myPos.stream().parallel()
                    // filter out the nulls
                    .filter(v->v!=null)
                    .collect(
                            Collectors.toMap(
                                    // key, reference of function
                                    MyPo::getId
                                    // value, lambda, return the value itself
                                    , mp->mp
                                    // merge function
                                    , (oldValue, newValue)->newValue))
                );
        }
        log.info("id2MyPo: '{}'", id2MyPo);
    }
    

    @Test
    public void testGroupBy() {
        Map<String, Long> remarkCounting = new HashMap<>();
        if(myPos != null) {
            remarkCounting.putAll(
                myPos.stream().parallel()
                    // filter out the nulls
                    .filter(v->v!=null)
//                    // get name, chain operations can be followed
//                    .map(mp -> mp.getName())
                    .collect(Collectors.groupingBy(MyPo::getRemark, Collectors.counting()))
                    
//                    .entrySet().stream()
//                    .sorted(Map.Entry.<String,Long>comparingByValue())
//                    .sorted(Map.Entry.<String,Long>comparingByValue().reversed())
//                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                );
        }
        log.info("remarkCounting: '{}'", remarkCounting);
    }
    

    @Test
    public void testGroupByAndSubMapping() {
        Map<String, Set<String>> remarkCountingAndSubMapping = new HashMap<>();
        if(myPos != null) {
            remarkCountingAndSubMapping.putAll(
                myPos.stream().parallel()
                    // filter out the nulls
                    .filter(v->v!=null)
//                    // get name, chain operations can be followed
//                    .map(mp -> mp.getName())
                    .collect(Collectors.groupingBy(MyPo::getRemark, 
                                Collectors.mapping(MyPo::getName, 
                                        Collectors.toSet())))
                );
        }
        log.info("remarkCountingAndSubMapping: '{}'", remarkCountingAndSubMapping);
    }
    @Test
    public void testSorted() {
        List<String> names = new ArrayList<>();
        if(myPos != null) {
            names.addAll(
                myPos.stream().parallel()
                    // filter out the nulls
                    .filter(v->v!=null)
//                    .sorted(Comparator.comparing(MyPo::getName))
//                    .sorted(Comparator.comparing(MyPo::getName).reversed())
//                    .sorted((a,b)->b.getId().compareTo(a.getId())
                    .map(mp->mp.getName())
                    .sorted((a,b)->a.compareTo(b))
                    .collect(Collectors.toList())
                );
        }
        log.info("names: '{}'", names);
    }
    
    @Data
    private static class MyPo{
        private Long id;
        private String name;
        private String remark;
    }
}
