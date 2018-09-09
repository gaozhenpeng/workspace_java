package com.at.junit5.exams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import lombok.extern.slf4j.Slf4j;

/**
 * | Annotation         | Description | 
 * | :----------------- | :---------- |
 * | @Test              | Denotes that a method is a test method. Unlike JUnit 4’s `@Test` annotation, this annotation does not declare any attributes, since test extensions in JUnit Jupiter operate based on their own dedicated annotations. Such methods are `inherited` unless they are`overridden`. | 
 * | @ParameterizedTest | Denotes that a method is a `parameterized test`. Such methods are `inherited` unless they are `overridden`. | 
 * | @RepeatedTestTest      | Denotes that a method is a test template for a `repeated test`. Such methods are `inherited`unless they are `overridden`. | 
 * | @TestFactory       | Denotes that a method is a test factory for `dynamic tests`. Such methods are `inherited` unless they are `overridden`. | 
 * | @TestInstance      | Used to configure the `test instance lifecycle` for the annotated test class. Such annotations are `inherited`. | 
 * | @TestTemplate      | Denotes that a method is a `template for test cases` designed to be invoked multiple times depending on the number of invocation contexts returned by the registered `providers`. Such methods are `inherited` unless they are `overridden`. | 
 * | @DisplayName       | Declares a custom display name for the test class or test method. Such annotations are not `inherited`. | 
 * | @BeforeEach        | Denotes that the annotated method should be executed `before` each `@Test`,`@RepeatedTestTest`,`@ParameterizedTest`, or `@TestFactory` method in the current class; analogous to JUnit 4’s `@Before`. Such methods are `inherited` unless they are `overridden`. | 
 * | @AfterEach         | Denotes that the annotated method should be executed `after` each `@Test`,`@RepeatedTestTest`,`@ParameterizedTest`, or `@TestFactory` method in the current class; analogous to JUnit 4’s `@After`. Such methods are `inherited` unless they are `overridden`. | 
 * | @BeforeAll         | Denotes that the annotated method should be executed `before` all `@Test`,`@RepeatedTestTest`,`@ParameterizedTest`, and `@TestFactory` methods in the current class; analogous to JUnit 4’s `@BeforeClass`. Such methods are `inherited`(unless they are `hidden` or `overridden`) and must be `static`(unless the "per-class" `test instance lifecycle` is used). | 
 * | @AfterAll          | Denotes that the annotated method should be executed `after` all `@Test`,`@RepeatedTestTest`,`@ParameterizedTest`, and `@TestFactory` methods in the current class; analogous to JUnit 4’s `@AfterClass`. Such methods are `inherited`(unless they are `hidden` or `overridden`) and must be `static`(unless the "per-class" `test instance lifecycle` is used). | 
 * | @Nested            | Denotes that the annotated class is a nested, non-static test class.`@BeforeAll` and `@AfterAllmethods` cannot be used directly in a `@Nested` test class unless the "per-class" `test instance lifecycle` is used. Such annotations are not `inherited`. | 
 * | @Tag               | Used to declare `tags` for filtering tests, either at the class or method level; analogous to test groups in TestNG or Categories in JUnit 4. Such annotations are `inherited` at the class level but not at the method level. | 
 * | @Disabled          | Used to `disable` a test class or test method; analogous to JUnit 4’s `@Ignore`. Such annotations are not `inherited`. | 
 * | @ExtendWith        | Used to register custom `extensions`. Such annotations are `inherited`. | 
 */
@Slf4j
public class UsualAnnotationsTest {
    @BeforeAll
    static void setup() {
        log.info("@BeforeAll executed");
    }

    @BeforeEach
    void setupThis() {
        log.info("@BeforeEach executed");
    }

    @Tag("DEV")
    @Test
    void testCalcOne() {
        assertEquals(4, 2 + 2, "4 != 2+2");
    }

    @Tag("PROD")
    @Disabled
    @Test
    void testCalcTwo() {
        assertEquals(6, 2 + 6, "6 != 2+4");
        fail("Should not be here when @Disabled");
    }

    @ParameterizedTest
    @ValueSource(ints = {1,3,4,2})
    void testParameterizedTest(int number) {
        assertTrue(number < 6, () -> "'"+number+"' < 6");
    }

    @AfterEach
    void tearThis() {
        log.info("@AfterEach executed");
    }

    @AfterAll
    static void tear() {
        log.info("@AfterAll executed");
    }
}
