package com.at.spring.annotation.property_source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan(basePackages = "com.at.spring.annotation.property_source")
@PropertySource(
        /** It's INVALID to use wildcard in the path.
         * see the doc of {@link org.springframework.context.annotation.PropertySource#value()} for details
         */
        value = "classpath:/openstack.properties"
        , ignoreResourceNotFound = false)
public class PropertySourceValue {
	private static final Logger logger = LoggerFactory.getLogger(PropertySourceValue.class);
	

    /**
     * This Bean is used to resolve the expression ${} .
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    /**
     * This required the registration of PropertySourcesPlaceholderConfigurer.
     * 
     * <pre>
     * @Bean
     * public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(){
     *     return new PropertySourcesPlaceholderConfigurer();
     * }
     * </pre>
     */
    @Value("#{'${openstack.identity.service.targeturl}'.trim()}")
    private String identity_service_targeturl = null;
    
    @Value("#{'${openstack.identity.service.domainname}'.trim()}")
    private String identity_service_domainname = null;

    @Value("#{'${openstack.identity.service.username}'.trim()}")
    private String identity_service_username = null;
    
    @Value("#{'${openstack.identity.service.userid}'.trim()}")
    private String identity_service_userid = null;

    @Value("#{'${openstack.identity.service.password}'.trim()}")
    private String identity_service_password = null;

    @Value("#{'${openstack.compute.service.targeturl}'.trim()}")
    private String compute_service_targeturl = null;

    /** set default value if not set*/
    @Value("#{'${openstack.identity.service.username:admin}'.trim()}")
    private String identity_service_username_default = null;



    public String IDENTITY_TARGETURL(){
        return identity_service_targeturl;
    }
    public String IDENTITY_DOMAINNAME(){
        return identity_service_domainname;
    }
    public String IDENTITY_USERNAME(){
        return identity_service_username;
    }
    public String IDENTITY_USERID(){
        return identity_service_userid;
    }
    public String IDENTITY_PASSWORD(){
        return identity_service_password;
    }
    public String COMPUTE_TARGETURL(){
        return compute_service_targeturl;
    }

    
    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(PropertySourceValue.class);
        PropertySourceValue app = context.getBean(PropertySourceValue.class);
        // by @Value()
        logger.info("by @Value(), " + app.IDENTITY_TARGETURL());
        //
        ValueInComponent com = context.getBean(ValueInComponent.class);
        logger.info("@Value() in component, " + com.compute_service_targeturl);
        
    }
}