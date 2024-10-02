package com.colak.springtutorial.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job job(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("job1", jobRepository)
                .start(step1())
                .next(step2()) // Decide between StepEven or StepOdd

                // If step2 fails go to step4
                .from(step2())
                .on("EVEN").to(stepEven()) // If EVEN, go to evenStep

                // If step2 completes go to step2
                .from(step2())
                .on("ODD").to(stepOdd()) // If EVEN, go to evenStep
                .end()// End the job
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step_one", jobRepository)
                .tasklet((_, _) -> {
                    log.info("STEP1 EXECUTED");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    // Decision step
    @Bean
    public JobExecutionDecider step2() {
        return (jobExecution, _) -> {
            // Example logic: decide based on job parameters or execution context
            JobParameters jobParameters = jobExecution.getJobParameters();
            Long runDecision = jobParameters.getLong("runDecision");

            if (runDecision % 2 == 0) {
                log.info("Even number detected : {}", runDecision);
                return new FlowExecutionStatus("EVEN");
            } else {
                log.info("Odd number detected : {}", runDecision);
                return new FlowExecutionStatus("ODD");
            }
        };
    }

    @Bean
    public Step stepOdd() {
        return new StepBuilder("step_four", jobRepository)
                .tasklet((_, _) -> {
                    log.info("STEP ODD EXECUTED");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }

    @Bean
    public Step stepEven() {
        return new StepBuilder("step_three", jobRepository)
                .tasklet((_, _) -> {
                    log.info("STEP EVEN EXECUTED");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }


}
