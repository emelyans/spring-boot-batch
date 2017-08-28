package com.example.springbootbatch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class ListenerStepExecution implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Before step ...");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("After step ...");
        return stepExecution.getExitStatus();
    }
}
