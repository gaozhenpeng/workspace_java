package com.at.spring_boot.cl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.at.spring_boot.cl.Application;
import com.at.spring_boot.cl.CommandLineJob;

/**
 * Command line test example. 
 * <strong>WARNING: <code>@RunWith(SpringRunner.class)</code> will trigger the running of the CommandLineRunner</strong>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Application.class })
@Configuration
// search current package if no basePackages are defined
@ComponentScan(basePackages = "com.at.spring_boot")
public class CommandLineJobTest {

    @Autowired
    private CommandLineJob commandLine;

    @Test
    public void testRun() {
        assertThat(commandLine).isNotNull();
        String[] args = new String[]{"testarg1", "testarg2", "testarg3", "testarg4", "testarg5"};
        try{
            commandLine.run(args);
        }catch(Exception e) {
            String msg = String.format("Exception happens when commandLine.run(%s)", Arrays.toString(args));
            fail(msg, e);
        }
    }

}
