package com.at.junit5.exams;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.Test;

public class MessageSupplierTest {

    @Test
    public void testFail() {
        assumeTrue("dev".equalsIgnoreCase(System.getProperty("ENV")));
        assertNotEquals(4, 2+2, () -> "4 == 2+2");
    }
}
