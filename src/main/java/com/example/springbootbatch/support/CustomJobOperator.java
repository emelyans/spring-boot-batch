package com.example.springbootbatch.support;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomJobOperator {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobOperator jobOperator;

    public void run(Job job, JobParameters jobParameters, boolean isRestart) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, JobParametersNotFoundException, NoSuchJobException {

        String jobName = job.getName();

        if (isRestart) {
            jobOperator.startNextInstance(jobName);
        } else {
//            Set<Long> runningExecutions = jobOperator.getRunningExecutions(jobName);
//            if (!runningExecutions.isEmpty()) {
//                Long runningExecution = runningExecutions.iterator().next();
//                System.out.println("Restart execution: " + jobOperator.getSummary(runningExecution));
//                jobOperator.restart(runningExecution); // it doesn't work
//            } else {
            System.out.println("Run job: " + jobName);
            jobLauncher.run(job, jobParameters);
//            }
        }
    }
}
