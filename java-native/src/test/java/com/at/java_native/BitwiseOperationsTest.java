package com.at.java_native;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * Bitwise Operators:
 * <ul>
 *   <li>| Bitwise OR</li>
 *   <li>&amp; Bitwise AND</li>
 *   <li>~ Bitwise Complement</li>
 *   <li>^ Bitwise XOR</li>
 *   <li>&lt;&lt; Left Shift</li>
 *   <li>&gt;&gt; Right Shift</li>
 *   <li>&gt;&gt;&gt; Unsigned Right Shift</li>
 * </ul>
 *
 * @see <a href="https://www.programiz.com/java-programming/bitwise-operators">https://www.programiz.com/java-programming/bitwise-operators</a> 
 *
 */
@Slf4j
public class BitwiseOperationsTest {
    
    /**
     * <code><pre>
     *  12 = 00001100 (In Binary)
     *  25 = 00011001 (In Binary)
     *  
     *  Bitwise OR Operation of 12 and 25
     *    00001100
     *  | 00011001
     *    ________
     *    00011101  = 29 (In decimal)
     * </pre></code>
     */
    @Test
    public void testBitwiseOr() {
        int number1 = 12;
        int number2 = 25;
        int result = number1 | number2;
        log.info("number1 | number2 : '{}'", result);
        assertEquals("29 != result ", 29, result);
        
    }

    /**
     * <code><pre>
     *  12 = 00001100 (In Binary)
     *  25 = 00011001 (In Binary)
     *  
     *  Bit Operation of 12 and 25
     *    00001100
     *  &amp; 00011001
     *    ________
     *    00001000  = 8 (In decimal)
     * </pre></code>
     */
    @Test
    public void testBitwiseAnd() {
        int number1 = 12;
        int number2 = 25;
        int result = number1 & number2;
        log.info("number1 & number2 : '{}'", result);
        assertEquals("8 != result ", 8, result);
        
    }
    
    /**
     * <code><pre>
     *   35 = 00100011 (In Binary)
     *   
     *   Bitwise complement Operation of 35
     *   ~ 00100011 
     *     ________
     *     11011100  = 220 (In decimal)
     *     
     *   But, java shows negative result (the leading bit '1') with its 2's complement
     *   ~n = -(-n + 1)
     *   -11011100 
     *    ________
     *    00100011  = 35
     *  +        1
     *    ________
     *    00100100  = 36
     *    
     *  -(36) = -36
     *   
     *   So the complement operations: (if the leading bit is '1' then) ~n = -(~~n + 1) = -(35 + 1) = -36
     * </pre></code>
     */
    @Test
    public void testBitwiseComplement() {
        int number1 = 35; // 00100011
        int result1 = ~number1; // 11011100 => -36, 首位(符号位)为1，表示此码使用了2补码的转换，则需要再次进行补码获得原值，才会得到-36
        log.info("~number1: '{}'", result1);
        assertEquals("-36 != result ", -36, result1);
        int number2 = 0x0F;
        int result2 = ~number1 & number2;
        log.info("~number1 & number2: '{}'", result2);
        assertEquals("12 != result ", 12, result2);

        int result3 = ~~number1; // 00100011 -> 11011100 -> 00100011, 35
        log.info("~~number1: '{}'", result3);
        assertEquals("35 != result ", 35, result3);
        
        
    }

    /**
     * <code><pre>
     *  12 = 00001100 (In Binary)
     *  25 = 00011001 (In Binary)
     *  
     *  Bitwise XOR Operation of 12 and 25
     *    00001100
     *  ^ 00011001
     *    ________
     *    00010101  = 21 (In decimal)
     * </pre></code>
     */
    @Test
    public void testBitwiseXOR() {
        int number1 = 12; // 00001100
        int number2 = 25; // 00011001
        int result1 = number1 ^ number2; // 
        log.info("number1 ^ number2: '{}'", result1);
        assertEquals("21 != result ", 21, result1);
    }

    /**
     * <code><pre>
     *   212 (In binary: 0000 1101 0100)
     *   
     *   212 &lt;&lt; 0 evaluates to 212  (In binary: 0000 0000 1101 0100)
     *   212 &lt;&lt; 1 evaluates to 424  (In binary: 0000 0001 1010 1000)
     *   212 &lt;&lt; 4 evaluates to 3392 (In binary: 0000 1101 0100 0000)
     *
     *   -212 (In binary: 1111 0010 1100)
     *   
     *   -212 &lt;&lt; 0 evaluates to -212  (In binary: 1111 1111 0010 1100)
     *   -212 &lt;&lt; 1 evaluates to -424  (In binary: 1111 1110 0101 1000)
     *   -212 &lt;&lt; 4 evaluates to -3392 (In binary: 1111 0010 1100 0000)
     * </pre></code>
     */
    @Test
    public void testBitwiseSignedLeftShift() {
        int number1 = 212; // 0000 1101 0100
        for(int i = 0 ; i < 8 ; i++){
            int result1 = number1 << i;
            log.info("number1 << {} : '{}'", i, result1);
            assertTrue("result1 >= 0", result1 >= 0);
        }

        int number2 = -212; // 1111 0010 1100 
        for(int i = 0 ; i < 8 ; i++){
            int result2 = number2 << i;
            log.info("number2 << {} : '{}'", i, result2);
            assertTrue("result2 >= 0", result2 <= 0);
        }
    }

    /**
     * <code><pre>
     *   212 (In binary: 0000 1101 0100)
     *   
     *   212 &gt;&gt; 0 evaluates to 212  (In binary: 0000 1101 0100)
     *   212 &gt;&gt; 1 evaluates to 106  (In binary: 0000 0110 1010)
     *   212 &gt;&gt; 4 evaluates to 13   (In binary: 0000 0000 1101) **
     *
     *   -212 (In binary: 1111 0010 1100)
     *   
     *   -212 &gt;&gt; 0 evaluates to -212  (In binary: 1111 1111 0010 1100)
     *   -212 &gt;&gt; 1 evaluates to -106  (In binary: 1111 1111 1001 0110)
     *   -212 &gt;&gt; 4 evaluates to -14   (In binary: 1111 1111 1111 0010) **
     * </pre></code>
     */
    @Test
    public void testBitwiseSignedRightShift() {
        int number1 = 212; // 0000 1101 0100
        for(int i = 0 ; i < 8 ; i++){
            int result1 = number1 >> i;
            log.info("number1 >> {} : '{}'", i, result1);
            assertTrue("result1 >= 0", result1 >= 0);
        }

        int number2 = -212; // 1111 0010 1100 
        for(int i = 0 ; i < 8 ; i++){
            int result2 = number2 >> i;
            log.info("number2 >> {} : '{}'", i, result2);
            assertTrue("result2 <= 0", result2 <= 0);
        }
    }

    /**
     * <code><pre>
     *   212 (In binary: 0000 1101 0100)
     *   
     *   212 &gt;&gt;&gt; 0 evaluates to 212  (In binary: 0000 1101 0100)
     *   212 &gt;&gt;&gt; 1 evaluates to 106  (In binary: 0000 0110 1010)
     *   212 &gt;&gt;&gt; 4 evaluates to 13   (In binary: 0000 0000 1101)
     *
     *   -212 (In binary,  64bit system : 1111 1111 1111 1111 1111 1111 0010 1100)
     *   
     *   -212 &gt;&gt;&gt; 0 evaluates to -212        (In binary: 1111 1111 1111 1111 1111 1111 0010 1100)
     *   -212 &gt;&gt;&gt; 1 evaluates to 2147483542  (In binary: 0111 1111 1111 1111 1111 1111 1001 0110)
     *   -212 &gt;&gt;&gt; 4 evaluates to 268435442   (In binary: 0000 1111 1111 1111 1111 1111 1111 0010)
     * </pre></code>
     */
    @Test
    public void testBitwiseUnsignedRightShift() {
        int number1 = 212; // 0000 1101 0100
        for(int i = 0 ; i < 8 ; i++){
            int result1 = number1 >>> i;
            log.info("number1 >>> {} : '{}'", i, result1);
            assertTrue("result1 >= 0", result1 >= 0);
        }

        int number2 = -212; // 1111 0010 1100 
        for(int i = 0 ; i < 8 ; i++){
            int result2 = number2 >>> i;
            log.info("number2 >>> {} : '{}'", i, result2);
            if(i == 0 ) {
                assertTrue("result2 != -212", result2 == -212);
            }else{
                assertTrue("result2 >= 0", result2 >= 0);
            }
        }
    }
}
