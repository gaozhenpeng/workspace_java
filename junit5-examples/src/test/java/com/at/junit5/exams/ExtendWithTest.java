package com.at.junit5.exams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.at.junit5.ext.MyJunit5Extension;
import com.at.junit5.ext.MyJunit5Extension.Msg;
import com.at.junit5.ext.MyJunit5Extension.RandDouble;
import com.at.junit5.ext.MyJunit5Extension.RandInt;

@ExtendWith(MyJunit5Extension.class)
public class ExtendWithTest {
    @Test
    void injectsString(@Msg String msg) {
        assertNotNull(msg);
    }

    @Test
    void injectsInt(@RandInt int i, @RandDouble double d) {
        assertEquals(0, i, 10);
        assertEquals(0.0, d, 1.0);
    }

}
