package com.example.springbootbatch.job;

import com.example.springbootbatch.listener.*;
import com.example.springbootbatch.processor.ProcessorPassThrough;
import com.example.springbootbatch.reader.ReaderList;
import com.example.springbootbatch.tasklet.TaskletConsole;
import com.example.springbootbatch.writer.WriterConsole;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

//    @Autowired
//    private AutomaticJobRegistrar automaticJobRegistrar(ApplicationContext applicationContext) {
//        AutomaticJobRegistrar automaticJobRegistrar = new AutomaticJobRegistrar();
//        automaticJobRegistrar.setApplicationContext(applicationContext);
//        automaticJobRegistrar.setJobLoader(new DefaultJobLoader());
//        return  automaticJobRegistrar;
//    }

//    @Bean
//    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
//        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
//        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
//        return jobRegistryBeanPostProcessor;
//    }

    @Autowired
    private JobRegistry jobRegistry;

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {

                    @BeforeStep
                    public void beforeStep(StepExecution stepExecution) {
                        System.out.println("Before step (annotation ...");
                    }

                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
                        System.out.println("Hi!");
                        return RepeatStatus.FINISHED;
                    }
                })
                .listener(new ListenerReader())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .chunk(2)
                .reader(new ReaderList())
                .processor(new ProcessorPassThrough<>())
                .writer(new WriterConsole<>())
                .listener(new ListenerStepExecution())
                .listener(new ListenerStepChunk())
                .listener(new ListenerReader())
                .listener(new ListenerProcess())
                .listener(new ListenerWriter())
                .listener(new ListenerSkip())
                .listener(new ListenerItemFailure())
                .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(new TaskletConsole())
                .listener(new ListenerStepExecution())
                .listener(new ListenerStepChunk())
                .listener(new ListenerReader())
                .listener(new ListenerProcess())
                .listener(new ListenerWriter())
                .listener(new ListenerSkip())
                .build();
    }

    @Bean
    public Job job(@Value("${jobName}") String jobName, Step step1, Step step2, Step step3) throws Exception {
        Job job = jobBuilderFactory.get(jobName)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .next(step3)
                .listener(new ListenerJobExecution())
                .build();

        jobRegistry.register(new ReferenceJobFactory(job));

        return job;
    }
}