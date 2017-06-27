package com.at.spring.annotation.property_source;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource(
        /** It's INVALID to use wildcard in the path.
         * see the doc of {@link org.springframework.context.annotation.PropertySource#value()} for details
         */
        value = "classpath:/openstack.properties"
        , ignoreResourceNotFound = false)
public class PropertySourceEnvironment implements EnvironmentAware{
    @Bean(name="IDENTITY_TARGETURL")
    public String IDENTITY_TARGETURL(){
        return readString("openstack.identity.service.targeturl");
    }
    @Bean(name="IDENTITY_DOMAINNAME")
    public String IDENTITY_DOMAINNAME(){
        return readString("openstack.identity.service.domainname");
    }
    @Bean(name="IDENTITY_USERNAME")
    public String IDENTITY_USERNAME(){
        return readString("openstack.identity.service.username");
    }
    @Bean(name="IDENTITY_USERID")
    public String IDENTITY_USERID(){
        return readString("openstack.identity.service.userid");
    }
    @Bean(name="IDENTITY_PASSWORD")
    public String IDENTITY_PASSWORD(){
        return readString("openstack.identity.service.password");
    }
    @Bean(name="COMPUTE_TARGETURL")
    public String COMPUTE_TARGETURL(){
        return readString("openstack.compute.service.targeturl");
    }

    /**
     * The values read by PropertySource will be added to the Environment.
     * So you can call the getProperty() to retrieve the value. 
     */
    private Environment env;
    
    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }
    
    private String readString(String propName) {
        return this.readString(propName, "");
    }
    private String readString(String propName, String defaultValue) {
        return env.getProperty(propName, defaultValue).trim();
    }
    


}