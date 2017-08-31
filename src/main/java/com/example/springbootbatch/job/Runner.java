package com.example.springbootbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
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
    private JobRepository jobRepository;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobOperator jobOperator;

    @Value("${restart}")
    private String restart;

    @Value("${jobName}")
    private String jobName;

    @Value("${param1}")
    private String param1;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder(new JobParameters())
                .addString("param1", param1).toJobParameters();
        if (restart.equals("true")) {
            System.out.println("Run new instance of job: " + jobName);
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
