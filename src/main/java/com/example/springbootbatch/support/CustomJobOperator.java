package com.example.springbootbatch.support;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomJobOperator {

    private static String RUN_ID_KEY = "restart";

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    public void run(Job job, JobParameters jobParameters, boolean isRestart) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, JobParametersNotFoundException, NoSuchJobException {

        String jobName = job.getName();

        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);

        if (lastJobExecution != null) {
            JobParameters lastInstanceParameters = lastJobExecution.getJobParameters();
            if (isRestart || lastJobExecution.getExitStatus() == ExitStatus.UNKNOWN) {
                jobLauncher.run(job, increaseRestartParameter(lastInstanceParameters));
            } else
                jobLauncher.run(job, lastInstanceParameters);
        } else {
            jobLauncher.run(job, jobParameters);
        }
    }

    public JobParameters increaseRestartParameter(JobParameters parameters) {

        JobParameters params = (parameters == null) ? new JobParameters() : parameters;

        long id = params.getLong(RUN_ID_KEY, 0L) + 1;

        return new JobParametersBuilder(params).addLong(RUN_ID_KEY, id, true).toJobParameters();
    }

}
