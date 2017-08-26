package com.example.springbootbatch.listeners;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(2)
public class JobExecutionListenerInterface implements JobExecutionListener {

    @Value("${jobName}")
    private String jobName;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobOperator jobOperator;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("beforeJob interface");

//        ExecutionContext executionContext = jobExecution.getExecutionContext();
//        try {
//            Set<Long> runningExecutions = jobOperator.getRunningExecutions(jobName);
//        } catch (NoSuchJobException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("afterJob interface");
        if( jobExecution.getStatus() == BatchStatus.COMPLETED ){
            System.out.println("success");
        }
        else if(jobExecution.getStatus() == BatchStatus.FAILED){
            System.out.println("fail");
        }
    }
}
