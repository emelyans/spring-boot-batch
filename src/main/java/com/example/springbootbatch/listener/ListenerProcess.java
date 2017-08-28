package com.example.springbootbatch.listener;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.item.ItemProcessor;

public class ListenerProcess implements ItemProcessListener {
    @Override
    public void beforeProcess(Object item) {
        System.out.println("Before process ...");
    }

    @Override
    public void afterProcess(Object item, Object result) {
        System.out.println("Before after ...");
    }

    @Override
    public void onProcessError(Object item, Exception e) {
        System.out.println("On process error ...");
    }
}
