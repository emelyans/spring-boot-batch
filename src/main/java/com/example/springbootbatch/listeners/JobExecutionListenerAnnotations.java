package com.example.springbootbatch.listeners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class JobExecutionListenerAnnotations {
    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("beforeJob annotations");
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.out.println("afterJob annotations");
    }
}
