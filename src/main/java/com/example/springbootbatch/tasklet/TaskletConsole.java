package com.example.springbootbatch.tasklet;

import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class TaskletConsole implements Tasklet {

    @BeforeJob
     public void beforeJob(JobExecution jobExecution) {
        System.out.println("Before job in TaskletConsole (annotation) ...");
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.out.println("After job in TaskletConsole (annotation) ...");

        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("success");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            System.out.println("fail");
        }
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("Before step in TaskletConsole (annotation) ...");
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("After step in TaskletConsole (annotation) ...");
        return stepExecution.getExitStatus();
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Execute in TaskletConsole ...");
//        throw new RuntimeException("oops");
        return RepeatStatus.FINISHED;
    }
}
