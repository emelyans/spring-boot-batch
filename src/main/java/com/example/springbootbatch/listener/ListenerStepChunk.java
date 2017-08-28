package com.example.springbootbatch.listener;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ListenerStepChunk implements ChunkListener{

    @Override
    public void beforeChunk(ChunkContext context) {
        System.out.println("Before chunk ...");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        System.out.println("After chunk ...");
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        System.out.println("After chunk error ...");
    }
}
