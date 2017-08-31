package com.example.springbootbatch.support;

import com.example.springbootbatch.Constants;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

@Component
public class CustomJobOperator {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CustomJobExecutionDao customJobExecutionDao;

//    @Autowired
//    private JdbcJobExecutionDao jdbcJobExecutionDao;

    private JobKeyGenerator<JobParameters> jobKeyGenerator = new DefaultJobKeyGenerator();

    public void run(Job job, JobParameters jobParameters, boolean isRestart) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException, JobParametersNotFoundException, NoSuchJobException {

        String jobName = job.getName();

        JobExecution lastJobExecution = getLastJobExecution(jobName, jobParameters);

        if (lastJobExecution != null) {
            JobParameters lastInstanceParameters = lastJobExecution.getJobParameters();
            if (isRestart || lastJobExecution.getExitStatus() == ExitStatus.UNKNOWN) {
                restart(job, jobParameters);
            } else
                start(job, jobParameters);
        } else {
            start(job, jobParameters);
        }
    }

    private JobExecution getLastJobExecution(String jobName, JobParameters jobParameters) {
//        Long lastRestartExecutionId = customJobExecutionDao.getLastRerunExecutionId(jobName, jobParameters);
//        JobExecution lastRestartExecution = lastRestartExecutionId != null ? jdbcJobExecutionDao.getJobExecution(lastRestartExecutionId) : null;
//        return lastRestartExecution != null ? lastRestartExecution : jobRepository.getLastJobExecution(jobName, jobParameters);
        return jobRepository.getLastJobExecution(jobName, jobParameters);
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

        long id = params.getLong(Constants.RESTART_CMDLINE_OPTION, 0L) + 1;

        return new JobParametersBuilder(params)
                .addString(Constants.BUSINESS_PARAMETERS_KEY_JOB_PARAMETER_NAME, jobKeyGenerator.generateKey(params))
                .addLong(Constants.RESTART_CMDLINE_OPTION, id).toJobParameters();
    }

}
