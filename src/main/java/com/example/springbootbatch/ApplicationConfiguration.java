package com.example.springbootbatch;

import com.example.springbootbatch.job.Runner;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@ComponentScan("com.example.springbootbatch")
public class ApplicationConfiguration {

    @Bean
    Runner runner() {
        return new Runner();
    }

}
