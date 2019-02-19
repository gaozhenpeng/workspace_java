package com.at.junit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.at.junit4.exams.JUnit4Example3;
import com.at.junit4.exams.JUnit4Example4;

@RunWith(Suite.class)
@Suite.SuiteClasses({ JUnit4Example3.class, JUnit4Example4.class, })
public class JUnit4TestSuite2 {

}
