package com.at.java8;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class OptionalTest {
    private static final String OUTER_COMPLETE_NESTED_INNER_FOO_MSG = "outerCompleteNestedInnerFoo";
    private static final String MISSING = "missing";
    private Outer outerComplete;
    private Outer outerMissingNested;
    private Outer outerMissingInner;
    private Outer outerMissingFoo;
    
    @Before
    public void setup() {
        
        setupOuterComplete();
        setupOuterMissingNested();
        setupOutputMissinginner();
        setupOutputMissingFoo();
        
    }

    private void setupOutputMissinginner() {
        outerMissingInner = new Outer();
        Nested outerMissingInnerNested = new Nested();
        outerMissingInner.setNested(outerMissingInnerNested);
    }

    private void setupOuterMissingNested() {
        outerMissingNested = new Outer();
    }

    private void setupOuterComplete() {
        outerComplete = new Outer();
        Nested outerCompleteNested = new Nested();
        Inner outerCompleteNestedInner = new Inner();
        outerCompleteNestedInner.setFoo(OUTER_COMPLETE_NESTED_INNER_FOO_MSG);
        
        
        outerCompleteNested.setInner(outerCompleteNestedInner);
        outerComplete.setNested(outerCompleteNested);
    }
    private void setupOutputMissingFoo() {
        outerMissingFoo = new Outer();
        Nested outerMissingFooNested = new Nested();
        Inner outerMissingFooNestedInner = new Inner();
        
        
        outerMissingFooNested.setInner(outerMissingFooNestedInner);
        outerMissingFoo.setNested(outerMissingFooNested);
    }
    
    @Test
    public void testChainComplete() {
        String result = Optional.ofNullable(outerComplete)
            .map(Outer::getNested)
            .map(Nested::getInner)
            .map(Inner::getFoo)
            .orElse(MISSING);
        assertEquals("result is expected to be '"+OUTER_COMPLETE_NESTED_INNER_FOO_MSG+"'", OUTER_COMPLETE_NESTED_INNER_FOO_MSG, result);

    }

    @Test
    public void testChainMissingNested() {
        String result = Optional.ofNullable(outerMissingNested)
            .map(Outer::getNested)
            .map(Nested::getInner)
            .map(Inner::getFoo)
            .orElse(MISSING);
        assertEquals("result is expected to be '"+MISSING+"'", MISSING, result);

    }
    

    @Test
    public void testChainMissingInner() {
        String result = Optional.ofNullable(outerMissingInner)
            .map(Outer::getNested)
            .map(Nested::getInner)
            .map(Inner::getFoo)
            .orElse(MISSING);
        assertEquals("result is expected to be '"+MISSING+"'", MISSING, result);
    }

    @Test
    public void testChainMissingFoo() {
        String result = Optional.ofNullable(outerMissingFoo)
            .map(Outer::getNested)
            .map(Nested::getInner)
            .map(Inner::getFoo)
            .orElse(MISSING);
        assertEquals("result is expected to be '"+MISSING+"'", MISSING, result);
    }
    
    
    
    
    
    
    
    @Data
    public static class Outer{
        private Nested nested;
    }
    @Data
    public static class Nested{
        private Inner inner;
    }
    @Data
    public static class Inner{
        private String Foo;
    }
}
