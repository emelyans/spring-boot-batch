package com.example.springbootbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Runner implements ApplicationRunner {

    @Autowired
    private Job job;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobOperator jobOperator;

    @Value("${restart}")
    private String restart = null;

    @Value("${jobName}")
    private String jobName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (restart.equals("true"))
            jobOperator.startNextInstance(jobName);
        else
            jobLauncher.run(job, new JobParameters());
    }
}
