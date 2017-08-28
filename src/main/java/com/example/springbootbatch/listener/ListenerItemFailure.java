package com.example.springbootbatch.listener;

import org.springframework.batch.core.listener.ItemListenerSupport;

public class ListenerItemFailure extends ItemListenerSupport {

    public void onReadError(Exception ex) {
        System.out.println("Encountered error on read ...");
    }

    public void onWriteError(Exception ex, Object item) {
        System.out.println("Encountered error on write ...");
    }
}
