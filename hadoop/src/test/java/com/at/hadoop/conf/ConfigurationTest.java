package com.at.hadoop.conf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.apache.hadoop.conf.Configuration;
import org.junit.Test;

public class ConfigurationTest {
	@Test
	public void testOverride(){
		boolean isLoadDefaults = false;
		Configuration conf = new Configuration(isLoadDefaults);
		conf.addResource("com/at/hadoop/conf/configuration-1.xml");
		conf.addResource("com/at/hadoop/conf/configuration-2.xml");
		System.setProperty("weight", "medium"); // redefine weight, medium
		
		assertThat(conf.get("color"), is("yellow"));
		assertThat(conf.getInt("size", 0), is(12));
		assertThat(conf.get("weight"), is("light")); // original weight, light
		assertThat(conf.get("breadth", "wide"), is("wide"));
		assertThat(conf.get("size-weight",""), is("12,medium")); // override weight, medium

		assertThat(conf.get("non-exists-variable"), is((String)null));
	}
}
