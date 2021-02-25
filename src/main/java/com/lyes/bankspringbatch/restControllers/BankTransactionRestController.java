package com.lyes.bankspringbatch.restControllers;


import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BankTransactionRestController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;


    @RequestMapping(value="/startJob")
    public BatchStatus load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        Map<String, JobParameter> jobParamters = new HashMap<>();
        jobParamters.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(jobParamters);
        JobExecution jobExecution = jobLauncher.run(job, parameters);
        while(jobExecution.isRunning()){
            System.out.println("job running ...");
        }
        return jobExecution.getStatus();
    }


}
