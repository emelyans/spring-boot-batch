package com.example.springbootbatch.job;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import java.util.Map;

/**
 * Created by emelyans on 9/21/2017.
 */
public class JobParametersHelper {
    public static String identifyingParametersToString(JobParameters jobParameters) {
        Map<String, JobParameter> props = jobParameters.getParameters();
        StringBuilder stringBuffer = new StringBuilder();

        props.forEach((k, v) -> {
            if (v.isIdentifying()) {
                stringBuffer.append(k).append("=").append(v).append(";");
            }
        });

        return stringBuffer.toString();
    }

    public static String nonIdentifyingParametersToString(JobParameters jobParameters) {
        Map<String, JobParameter> props = jobParameters.getParameters();
        StringBuilder stringBuffer = new StringBuilder();

        props.forEach((k, v) -> {
            if (!v.isIdentifying()) {
                stringBuffer.append(k).append("=").append(v).append(";");
            }
        });

        return stringBuffer.toString();
    }

    public static String allParametersToString(JobParameters jobParameters) {
        StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.append("[Identifying parameters: ").append(identifyingParametersToString(jobParameters)).append("]");
        stringBuffer.append("[Non identifying parameters: ").append(nonIdentifyingParametersToString(jobParameters)).append("]");

        return stringBuffer.toString();
    }

}
