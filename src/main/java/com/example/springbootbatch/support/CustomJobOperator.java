package com.example.springbootbatch.support;

import com.example.springbootbatch.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
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

    Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private CustomJobExecutionDao customJobExecutionDao;

//    @Autowired
//    private JdbcJobExecutionDao jdbcJobExecutionDao;

    @Autowired
    private JobKeyGenerator<JobParameters> jobKeyGenerator;

    public void run(Job job, JobParameters jobParameters, boolean isRestart) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, JobParametersNotFoundException, NoSuchJobException {

        String jobName = job.getName();

        JobExecution lastJobExecution = getLastJobExecution(jobName, jobParameters);

        if (lastJobExecution != null) {
            JobParameters lastInstanceParameters = lastJobExecution.getJobParameters();
            if (isRestart) {
                LOGGER.info("Restart job ...");
                restart(job, lastInstanceParameters);
            } else {
                if (lastJobExecution.isRunning()) {
                    LOGGER.error("There is a running or terminated instance of the job. Please investigate and RERUN the job if necessary.");
                    throw new JobExecutionAlreadyRunningException("There is a running or terminated instance of the job. Please investigate and RERUN the job if necessary.");
                } else if (lastJobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                    LOGGER.error("Job already completed. Please investigate and RERUN the job if necessary.");
                    throw new JobInstanceAlreadyCompleteException("Job already completed. Please investigate and RERUN the job if necessary.");
                } else {
                    LOGGER.info("Resume job ...");
                    start(job, lastInstanceParameters);
                }
            }
        } else {
            if (isRestart)
                LOGGER.warn("Job is configured as RERUN, but no previous executions were found... Job will be started as first run...");
            LOGGER.info("First run of the job ...");
            start(job, jobParameters);
        }
    }

    private JobExecution getLastJobExecution(String jobName, JobParameters jobParameters) {
        Long lastRestartExecutionId = customJobExecutionDao.getLastRerunExecutionId(jobName, jobParameters);
        JobExecution lastRestartExecution = lastRestartExecutionId != null ? jobExplorer.getJobExecution(lastRestartExecutionId) : null;
        return lastRestartExecution != null ? lastRestartExecution : jobRepository.getLastJobExecution(jobName, jobParameters);
    }

    private void start(Job job, JobParameters jobParameters) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        jobLauncher.run(job, jobParameters);
    }

    private void restart(Job job, JobParameters jobParameters) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobParameters restartParameters = increaseRestartParameter(jobParameters);
        start(job, restartParameters);
    }

    public JobParameters increaseRestartParameter(JobParameters parameters) {

        JobParameters params = (parameters == null) ? new JobParameters() : parameters;

        long id = params.getLong(Constants.RESTART_JOB_PARAMETER_NAME, 0L) + 1;

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(params);

        if (id == 1)
            jobParametersBuilder.addString(Constants.BUSINESS_PARAMETERS_KEY_JOB_PARAMETER_NAME, jobKeyGenerator.generateKey(params));

        return jobParametersBuilder.addLong(Constants.RESTART_JOB_PARAMETER_NAME, id).toJobParameters();
    }

}
