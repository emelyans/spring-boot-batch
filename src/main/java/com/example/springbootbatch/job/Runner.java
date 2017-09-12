package com.example.springbootbatch.job;

import com.example.springbootbatch.Constants;
import com.example.springbootbatch.support.CustomJobOperator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Runner implements ApplicationRunner {

    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private CustomJobOperator customJobOperator;

    @Value("${restart}")
    private String restart;

    @Value("${jobName}")
    private String jobName;

    @Value("${" + Constants.PARAM1_JOB_PARAMETER_NAME + "}")
    private String param1;

    @Value("${" + Constants.PARAM2_JOB_PARAMETER_NAME + "}")
    private String param2;

    @Override
    public void run(ApplicationArguments args) throws NoSuchJobException, JobParametersNotFoundException, JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters jobParameters = new JobParametersBuilder(new JobParameters())
                .addString(Constants.PARAM1_JOB_PARAMETER_NAME, param1)
                .addString(Constants.PARAM2_JOB_PARAMETER_NAME, param2, false)
                .toJobParameters();

        Job job = jobRegistry.getJob(jobName);

        boolean isRestart = restart.equals(Constants.RESTART_CMDLINE_OPTION_TRUE);

        customJobOperator.run(job, jobParameters, isRestart);
    }
}
