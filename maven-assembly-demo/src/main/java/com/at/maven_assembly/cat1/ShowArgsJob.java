package com.at.maven_assembly.cat1;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShowArgsJob {
    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            Arrays.stream(args).forEach((arg) -> {
                log.info("arg: '{}'", arg);
            });
        }
    }
}
