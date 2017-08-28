package com.example.springbootbatch.processor;

import com.example.springbootbatch.model.Car;
import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.annotation.BeforeProcess;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProcessorPassThrough<T> implements ItemProcessor<T, T> {

    @BeforeProcess
    public void beforeProcess(Object item) {
        System.out.println("Before process (annotation) ...");
    }

    @AfterProcess
    public void afterProcess(Object item, Object result) {
        System.out.println("After process (annotation) ...");
    }

    @OnProcessError
    public void onProcessError(Object item, Exception e) {
        System.out.println("On process error (annotation) ...");
    }

    @Override
    public T process(T o) throws Exception {
        return o;
    }
}
