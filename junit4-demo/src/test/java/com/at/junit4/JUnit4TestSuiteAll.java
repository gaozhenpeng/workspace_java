package com.at.junit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.at.junit4.spring.JUnit4SpringContextTestsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    JUnit4TestSuite1.class,
    JUnit4TestSuite2.class,
    JUnit4SpringContextTestsTest.class, // this can also be set in inner test suite.
})
public class JUnit4TestSuiteAll {

}
