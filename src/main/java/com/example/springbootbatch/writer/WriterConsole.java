package com.example.springbootbatch.writer;

import org.springframework.batch.core.annotation.AfterWrite;
import org.springframework.batch.core.annotation.BeforeWrite;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class WriterConsole<T> implements ItemWriter<T> {

    @BeforeWrite
    public void beforeWrite(List items) {
        System.out.println("Before write (annotation) ...");
    }

    @AfterWrite
    public void afterWrite(List items) {
        System.out.println("After write (annotation) ...");
    }

    @OnWriteError
    public void onWriteError(Exception exception, List items) {
        System.out.println("On write error (annotation) ...");
    }

    @Override
    public void write(List list) throws Exception {
        System.out.println(list);
    }
}
