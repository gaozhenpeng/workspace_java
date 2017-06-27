package com.at.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonsLangTest {
	private static final Logger logger = LoggerFactory.getLogger(CommonsLangTest.class);

    @Test
    public void testStringUtils() {
        final String aStr = "";
        final String trimedStr_NULL = StringUtils.trimToNull(aStr);
        final String trimedStr_EMPTY = StringUtils.trimToEmpty(aStr);
        assertNull("the result of trimToNull() is expected to be null",
                trimedStr_NULL);
        assertEquals("the result of trimedStr_EMPTY() is expected to be empty",
                "", trimedStr_EMPTY);
    }

    @Test
    public void testArrayUtils_isEmpty() {
        final String[] aNullStrArr = null;
        final String[] emptyStrArr_10Ele = new String[10];
        final String[] emptyStrArr_0ele = new String[0];
        final String[] emptyStrArr_1ele = new String[] { "a" };

        assertFalse("Array is expected to be non-empty.",
                ArrayUtils.isEmpty(emptyStrArr_1ele));
        assertFalse("Array is expected to be non-empty.",
                ArrayUtils.isEmpty(emptyStrArr_10Ele));
        assertTrue("Array is expected to be empty.",
                ArrayUtils.isEmpty(emptyStrArr_0ele));
        assertTrue("Array is expected to be empty.",
                ArrayUtils.isEmpty(aNullStrArr));
    }

    @Test
    public void testArrayUtils_contains() {
        final String[] aStrArr = new String[] {"a", "aa", "abc", "aaa", "ccc", null, "ao"};

        assertTrue("a", ArrayUtils.contains(aStrArr, "a"));
        assertTrue("aa", ArrayUtils.contains(aStrArr, "aa"));
        assertFalse("bc", ArrayUtils.contains(aStrArr, "bc"));
        assertTrue("null", ArrayUtils.contains(aStrArr, null));
        assertTrue("ccc", ArrayUtils.contains(aStrArr, "ccc"));
    }

    
    
    
    
    
    private static class Builder implements Comparable<Builder> {
        private String name = null;
        private Integer number = null;
        private String tel = null;

        public Builder(final String name, final Integer number, final String tel) {
            this.name = name;
            this.number = number;
            this.tel = tel;
        }

        public String getName() {
            return name;
        }

        public Integer getNumber() {
            return number;
        }

        public String getTel() {
            return tel;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public void setNumber(final Integer number) {
            this.number = number;
        }

        public void setTel(final String tel) {
            this.tel = tel;
        }

        @Override
        public String toString() {
//            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE); //

            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE) //
                .append("name", name) //
                .append("number", number) //
                .append("tel", tel) //
                .toString();
        }

        @Override
        public int hashCode() {
//            return HashCodeBuilder.reflectionHashCode(this, true);

            return new HashCodeBuilder() //
                //.appendSuper(super.hashCode()) // two object created by new is not the same.
                .append(name) //
                .append(number) //
                .append(tel) //
                .toHashCode();
        }

        @Override
        public boolean equals(final Object o) {
//            return EqualsBuilder.reflectionEquals(this, o, true);

            if(!Builder.class.isAssignableFrom(o.getClass())){
                return false;
            }
            final Builder rhs = (Builder)o;
            return new EqualsBuilder() //
                .append(name, rhs.getName()) //
                .append(number, rhs.getNumber()) //
                .append(tel, rhs.getTel()) //
                .isEquals();
        }

        @Override
        public int compareTo(final Builder rhs) {
//            return CompareToBuilder.reflectionCompare(this, rhs);

            return new CompareToBuilder() //
                .append(name, rhs.name) //
                .append(number, rhs.number) //
                .append(tel, rhs.tel) //
                .toComparison();
        }
    }

    @Test
    public void testHashCodeBuilder_EqualsBuilder_CompareToBuilder() {
        final Builder a = new Builder("my name1", 101, "telno1");
        final Builder b1 = new Builder("my name2", 102, "telno2");
        final Builder b2 = new Builder("my name2", 102, "telno2");

        assertFalse("toString", b1.toString().equals(a.toString()));
        assertFalse("hashCode", b1.hashCode() == a.hashCode());
        assertFalse("equals", b1.equals(a));
        assertFalse("equals", b1.equals(new Object()));
        assertFalse("compareTo", b1.compareTo(a) == 0);

        assertFalse("toString", b1.toString() == b2.toString()); // object id
        assertTrue("hashCode", b1.hashCode() == b2.hashCode());
        assertTrue("equals", b1.equals(b2));
        assertTrue("compareTo", b1.compareTo(b2) == 0);
    }
    
    @Test
    public void testRandomStringUtils(){
    	logger.info(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
    }
}