package com.at.junit5.exams.intf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Class Output:
 * 
 * [INFO ] [2018-09-09T22:36:21,663] [           main] [  com.at.jun.exa.int.TestLifecycleLogger:20   ] | Before all tests
 * [INFO ] [2018-09-09T22:36:21,669] [           main] [  com.at.jun.exa.int.TestLifecycleLogger:30   ] | About to execute [dynamicTestsFromCollection()]
 * [INFO ] [2018-09-09T22:36:21,685] [           main] [      com.at.jun.exa.int.TimingExtension:29   ] | Method [dynamicTestsFromCollection] took 14 ms.
 * [INFO ] [2018-09-09T22:36:21,686] [           main] [  com.at.jun.exa.int.TestLifecycleLogger:36   ] | Finished executing [dynamicTestsFromCollection()]
 * [INFO ] [2018-09-09T22:36:21,688] [           main] [  com.at.jun.exa.int.TestLifecycleLogger:30   ] | About to execute [isEqualValue()]
 * [INFO ] [2018-09-09T22:36:21,689] [           main] [      com.at.jun.exa.int.TimingExtension:29   ] | Method [isEqualValue] took 1 ms.
 * [INFO ] [2018-09-09T22:36:21,689] [           main] [  com.at.jun.exa.int.TestLifecycleLogger:36   ] | Finished executing [isEqualValue()]
 * [INFO ] [2018-09-09T22:36:21,690] [           main] [  com.at.jun.exa.int.TestLifecycleLogger:25   ] | After all tests
 */
public class InterfaceTest implements TestLifecycleLogger, TimeExecutionLogger, TestInterfaceDynamicTests {

    @Test
    public void isEqualValue() {
        assertEquals(1, 1, "is always equal");
    }

}
