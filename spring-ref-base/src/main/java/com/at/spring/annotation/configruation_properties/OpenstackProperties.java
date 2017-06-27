package com.at.spring.annotation.configruation_properties;

//import org.springframework.boot.context.properties.ConfigurationProperties;

import com.sun.istack.internal.NotNull;

/**
 * The spring-boot package is required for the @ConfigurationProperties annotation
 * <dependency>
 *  <groupId>org.springframework.boot</groupId>
 *  <artifactId>spring-boot</artifactId>
 *  <!-- @EnableConfigurationProperties -->
 *  <version>1.2.6.RELEASE</version>
 * </dependency>
 */
//@ConfigurationProperties(
//      /** It's INVALID to use wildcard in the path.
//       * see the doc of {@link org.springframework.context.annotation.PropertySource#value()} for details
//       */
//      locations = {"classpath:/openstack.properties"}
//      ,prefix = "openstack"
//      ,ignoreUnknownFields = false)
public class OpenstackProperties {
    @NotNull
    private Identity identity;
    @NotNull
    private Compute compute;
    
    
    public Identity getIdentity() {
        return identity;
    }
    public void setIdentity(Identity identity) {
        this.identity = identity;
    }
    public Compute getCompute() {
        return compute;
    }
    public void setCompute(Compute compute) {
        this.compute = compute;
    }
    public static class Identity{
        private Service service;
        public Service getService() {
            return service;
        }
        public void setService(Service service) {
            this.service = service;
        }
        public static class Service{
            private String targeturl;
            private String domainname;
            private String username;
            private String userid;
            private String password;
            public String getTargeturl() {
                return targeturl;
            }
            public void setTargeturl(String targeturl) {
                this.targeturl = targeturl;
            }
            public String getDomainname() {
                return domainname;
            }
            public void setDomainname(String domainname) {
                this.domainname = domainname;
            }
            public String getUsername() {
                return username;
            }
            public void setUsername(String username) {
                this.username = username;
            }
            public String getUserid() {
                return userid;
            }
            public void setUserid(String userid) {
                this.userid = userid;
            }
            public String getPassword() {
                return password;
            }
            public void setPassword(String password) {
                this.password = password;
            }
            
        }
    }
    public static class Compute{
        private Service service;
        public Service getService() {
            return service;
        }
        public void setService(Service service) {
            this.service = service;
        }
        public static class Service{
            private String targeturl;

            public String getTargeturl() {
                return targeturl;
            }

            public void setTargeturl(String targeturl) {
                this.targeturl = targeturl;
            }
        }
    }
}