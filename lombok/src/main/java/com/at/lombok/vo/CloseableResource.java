package com.at.lombok.vo;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CloseableResource implements Closeable {
    private static final List<Integer> inList = new ArrayList<>();
    
    @Override
    public void close() throws IOException {
        log.info("I'm being closed.");
        inList.clear();
    }
    @SneakyThrows
    public void noDeclarationOfThrownException() {
        throw new Exception("noThrownException");
    }
    @Synchronized
    public void add(int i) {
        inList.add(i);
    }
    public List<Integer> get(){
        return Collections.unmodifiableList(inList);
    }
}
