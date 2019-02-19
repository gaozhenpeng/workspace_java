package com.at.junit5.exams;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Syntax Rules for Tags
 * <ul>
 *   <li> A tag must not be null or blank. </li>
 *   <li> A trimmed tag must not contain whitespace. </li>
 *   <li> A trimmed tag must not contain ISO control characters. </li>
 *   <li> A trimmed tag must not contain any of the following reserved characters.
 *      <ul>
 *          <li> ,: comma </li>
 *          <li> (: left parenthesis </li>
 *          <li> ): right parenthesis </li>
 *          <li> &: ampersand </li>
 *          <li> |: vertical bar </li>
 *          <li> !: exclamation point </li>
 *      </ul>
 *   </li>
 * </ul>
 */
@Tag("fast")
@Tag("model")
public class TagTest {
    @Test
    @Tag("taxes")
    void testingTaxCalculation() {
    }

}
