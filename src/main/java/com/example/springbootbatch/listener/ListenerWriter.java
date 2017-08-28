package com.example.springbootbatch.listener;

import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class ListenerWriter implements ItemWriteListener {
    @Override
    public void beforeWrite(List items) {
        System.out.println("Before write ...");
    }

    @Override
    public void afterWrite(List items) {
        System.out.println("After write ...");
    }

    @Override
    public void onWriteError(Exception exception, List items) {
        System.out.println("On write error ...");
    }
}
