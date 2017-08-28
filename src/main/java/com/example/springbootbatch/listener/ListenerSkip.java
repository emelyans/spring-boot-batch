package com.example.springbootbatch.listener;

import org.springframework.batch.core.SkipListener;

public class ListenerSkip implements SkipListener {
    @Override
    public void onSkipInRead(Throwable t) {
        System.out.println("On skip in read ...");
    }

    @Override
    public void onSkipInWrite(Object item, Throwable t) {
        System.out.println("On skip in write ...");
    }

    @Override
    public void onSkipInProcess(Object item, Throwable t) {
        System.out.println("On skip in process ...");
    }
}
