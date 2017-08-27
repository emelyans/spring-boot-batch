package com.example.springbootbatch.listeners;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobExecutionListenerInterface implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Before job ...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("After job ...");

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("success");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            System.out.println("fail");
        }
    }
}
