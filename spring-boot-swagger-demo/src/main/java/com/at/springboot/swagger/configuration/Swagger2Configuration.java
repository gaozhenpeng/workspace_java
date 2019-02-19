package com.at.springboot.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** http://localhost:8080/demo/swagger-ui.html */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                // the apiInfo can be ignored
                .apiInfo(getApiInfo())
                .select()
                // the scan base package, usually in a form like 'xxx.xxx.controller'
                .apis(RequestHandlerSelectors.basePackage("com.at.springboot.swagger.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    
    /** The api information. Useful for open source project, but not necessary for proprietary ones ; */
    private ApiInfo getApiInfo() {
        Contact contact = new Contact("Alpha TAN", "http://localhost", "alphatan@users.noreply.github.com");
        return new ApiInfoBuilder()
                .title("Integration of spring-boot and swagger")
                .description("An example of the integration of spring-boot and swagger")
                .version("1.0.0")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .contact(contact)
                .build();
    }
}
