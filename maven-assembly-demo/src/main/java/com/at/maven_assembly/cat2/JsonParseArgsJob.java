package com.at.maven_assembly.cat2;

import java.util.Arrays;

import org.json.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonParseArgsJob {
    // java call: "{\"JsonParseArgsJob\" : {\"somekey\" : \"somevalue\" }, \"JsonParseArgsJob1\" : {\"otherkey\" : \"othervalue\"}}"
    // azkaban flow parameters: '{"JsonParseArgsJob" : {"somekey" : "somevalue" }, "JsonParseArgsJob1" : {"otherkey" : "othervalue"}}'
    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            Arrays.stream(args).forEach((arg) -> {
                log.info("arg: '{}'", arg);
            });
            
            // parse the first argument
            JSONObject json = new JSONObject(args[0]);
            JSONObject jsonParseArgsJob = json.getJSONObject(JsonParseArgsJob.class.getSimpleName());
            log.info("'{}' of args[0]: '{}' ", JsonParseArgsJob.class.getSimpleName(), jsonParseArgsJob.toString(2));
        }
    }
}
