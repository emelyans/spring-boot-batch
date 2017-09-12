package com.example.springbootbatch.support;

import com.example.springbootbatch.Constants;
import org.springframework.batch.core.JobKeyGenerator;
import org.springframework.batch.core.JobParameters;

/**
 * Created by emelyans on 9/12/2017.
 */
public class CustomJobKeyGenerator implements JobKeyGenerator<JobParameters> {

    @Override
    public String generateKey(JobParameters source) {
        return source.getString(Constants.PARAM1_JOB_PARAMETER_NAME) + "_" +
                source.getString(Constants.PARAM2_JOB_PARAMETER_NAME);
    }
}
