package com.colak.springtutorial.commandlinerunner;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job job;

    @Override
    public void run(String... args) throws Exception {
        // Comment out this section if you want to disable the job from running automatically at startup
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("runDecision", System.currentTimeMillis() % 10)
                .toJobParameters();

        jobLauncher.run(job, jobParameters); // Manually triggering the job
    }
}

