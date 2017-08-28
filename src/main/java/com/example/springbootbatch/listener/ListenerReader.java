package com.example.springbootbatch.listener;

import org.springframework.batch.core.ItemReadListener;

public class ListenerReader implements ItemReadListener {
    @Override
    public void beforeRead() {
        System.out.println("Before read ...");
    }

    @Override
    public void afterRead(Object item) {
        System.out.println("After read ...");
    }

    @Override
    public void onReadError(Exception ex) {
        System.out.println("On read error ...");
    }
}
