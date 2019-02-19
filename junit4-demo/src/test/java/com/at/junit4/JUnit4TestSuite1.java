package com.at.junit4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.at.junit4.exams.JUnit4Example1;
import com.at.junit4.exams.JUnit4Example2;

@RunWith(Suite.class)
@Suite.SuiteClasses({ JUnit4Example1.class, JUnit4Example2.class, })
public class JUnit4TestSuite1 {

}
