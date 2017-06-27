package com.at.feign;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import feign.Feign;
import feign.Feign.Builder;
import feign.okhttp.OkHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

@Configuration
@ComponentScan
public class FeignSpringMain {
    private Builder feignBuilder = null;

    @Bean
    @Scope("prototype")
    public Builder feignBuilder(){
        return Feign.builder();
    }

    @Autowired
    public void setFeignBuilder(Builder feignBuilder){
        this.feignBuilder = feignBuilder;
    }

    public void access(){
        GitHub4Jackson github4Jackson = feignBuilder
                .logger(new Slf4jLogger())
                .logLevel(feign.Logger.Level.FULL)
                .client(new OkHttpClient()) // in order to use the HTTP methods like 'PATCH', 'MOVE', 'COPY'
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(GitHub4Jackson.class, "https://api.github.com");

        // Fetch and print a list of the contributors to this library.
        List<Contributor4Jackson> contributors4Jackson = github4Jackson.contributors("netflix", "feign");
        for (Contributor4Jackson contributor : contributors4Jackson) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(FeignSpringMain.class);
        FeignSpringMain main = ctx.getBean(FeignSpringMain.class);
        main.access();

    }
}