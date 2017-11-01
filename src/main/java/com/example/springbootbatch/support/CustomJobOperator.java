package com.example.springbootbatch.support;

import com.example.springbootbatch.Constants;
import com.example.springbootbatch.job.JobParametersHelper;
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
            JobParameters lastExecutionParameters = lastJobExecution.getJobParameters();
            if (isRestart) {
                LOGGER.info("Rerun job (new instance will be created) ...");
                restart(job, jobParameters, lastExecutionParameters);
            } else {
                if (lastJobExecution.isRunning()) {
                    String errorMessage = "There is a running or terminated execution of the job with following parameters: " + JobParametersHelper.allParametersToString(lastExecutionParameters);
                    LOGGER.error(errorMessage);
                    LOGGER.error("Please investigate and RERUN the job (use '--rerun=true' command line option) if necessary.");
                    throw new JobExecutionAlreadyRunningException(errorMessage);
                } else if (lastJobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                    String errorMessage = "Job with following parameters already completed: " + JobParametersHelper.allParametersToString(lastExecutionParameters);
                    LOGGER.error(errorMessage);
                    LOGGER.error("Please investigate and RERUN the job (use '--rerun=true' command line option) if necessary.");
                    throw new JobInstanceAlreadyCompleteException(errorMessage);
                } else {
                    LOGGER.info("Resume job ...");
                    start(job, lastExecutionParameters);
                }
            }
        } else {
            if (isRestart)
                LOGGER.warn("Job is configured as RERUN, but no previous executions were found... Job will be started as first run...");
            else
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
        LOGGER.info("Start job with following parameters: {}", JobParametersHelper.allParametersToString(jobParameters));
        jobLauncher.run(job, jobParameters);
    }

    private void restart(Job job, JobParameters newParameters, JobParameters prevExecParameters) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        LOGGER.info("Previous execution parameters: {}", JobParametersHelper.allParametersToString(prevExecParameters));
        JobParameters rerunParameters = increaseRestartParameter(newParameters, prevExecParameters);
        LOGGER.info("New execution parameters: {}", JobParametersHelper.allParametersToString(rerunParameters));
        start(job, rerunParameters);
    }

    public JobParameters increaseRestartParameter(JobParameters newParameters, JobParameters parameters) {

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder(newParameters);

        jobParametersBuilder.addString(Constants.BUSINESS_PARAMETERS_KEY_JOB_PARAMETER_NAME, jobKeyGenerator.generateKey(newParameters));
        jobParametersBuilder.addLong(Constants.RESTART_JOB_PARAMETER_NAME, parameters.getLong(Constants.RESTART_JOB_PARAMETER_NAME, 0L) + 1);

        return jobParametersBuilder.toJobParameters();
    }

}
