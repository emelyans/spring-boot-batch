package com.example.springbootbatch;

import com.example.springbootbatch.job.Runner;
import com.example.springbootbatch.support.CustomJobExecutionDao;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.dao.JdbcJobExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcOperations;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing
@ComponentScan("com.example.springbootbatch")
public class ApplicationConfiguration {

    @Bean
    Runner runner() {
        return new Runner();
    }

    @Bean
    public CustomJobExecutionDao customJobExecutionDao(JdbcOperations jdbcTemplate) {
        CustomJobExecutionDao customJobExecutionDao = new CustomJobExecutionDao();
        customJobExecutionDao.setJdbcTemplate(jdbcTemplate);
        return customJobExecutionDao;
    }

//    @Bean
//    public JdbcJobExecutionDao jdbcJobExecutionDao(JdbcOperations jdbcTemplate) {
//        JdbcJobExecutionDao jdbcJobExecutionDao = new JdbcJobExecutionDao();
//        jdbcJobExecutionDao.setJdbcTemplate(jdbcTemplate);
//        jdbcJobExecutionDao.setJobExecutionIncrementer();
//        return jdbcJobExecutionDao;
//    }
}
