package com.colak.springtutorial.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BatchController {

    private final JobLauncher jobLauncher;

    private final Job job;

    // http://localhost:8080/job
    @GetMapping("/job")
    public void startJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("runDecision", System.currentTimeMillis() % 10)
                .toJobParameters();

        jobLauncher.run(job,jobParameters);
    }
}
