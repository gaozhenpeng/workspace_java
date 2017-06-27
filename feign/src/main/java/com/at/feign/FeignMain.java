package com.at.feign;

import java.util.List;

import feign.Feign;
import feign.okhttp.OkHttpClient;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

public class FeignMain {

    public static void main(String[] args) {
        GitHub4Gson github4Gson = Feign
                .builder()
                .logger(new Slf4jLogger())
                .logLevel(feign.Logger.Level.FULL)
                .client(new OkHttpClient()) // in order to use the HTTP methods like 'PATCH', 'MOVE', 'COPY'
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .errorDecoder(new GitHubErrorDecoder())
                .target(GitHub4Gson.class, "https://api.github.com");

        // Fetch and print a list of the contributors to this library.
        List<Contributor4Gson> contributors4Gson = github4Gson.contributors("netflix", "feign");
        for (Contributor4Gson contributor : contributors4Gson) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }

        GitHub4Jackson github4Jackson = Feign
                .builder()
                .logger(new Slf4jLogger())
                .logLevel(feign.Logger.Level.FULL)
                .client(new OkHttpClient()) // in order to use the HTTP methods like 'PATCH', 'MOVE', 'COPY'
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new GitHubErrorDecoder())
                .target(GitHub4Jackson.class, "https://api.github.com");

        // Fetch and print a list of the contributors to this library.
        List<Contributor4Jackson> contributors4Jackson = github4Jackson.contributors("netflix", "feign");
        for (Contributor4Jackson contributor : contributors4Jackson) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }

        // error
        GitHub4Gson github4Gson2 = Feign
                .builder()
                .logger(new Slf4jLogger())
                .logLevel(feign.Logger.Level.FULL)
                .client(new OkHttpClient()) // in order to use the HTTP methods like 'PATCH', 'MOVE', 'COPY'
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .errorDecoder(new GitHubErrorDecoder())
                .target(GitHub4Gson.class, "https://api.github.com/xk");
        github4Gson2.contributors("netflix", "feign");
    }
}