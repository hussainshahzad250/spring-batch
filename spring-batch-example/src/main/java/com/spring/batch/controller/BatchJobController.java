package com.spring.batch.controller;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.batch.constant.Constant;
import com.spring.batch.exception.Response;

@RestController
@RequestMapping("/api")
public class BatchJobController implements Constant {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@PostMapping("/importDataFromFixedLocation")
	public Response importDataFromFixedLocation() {
		JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
				.toJobParameters();
		try {
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);
			if (jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
				return new Response(FILE_SUCCESS, HttpStatus.OK);
			}
			return new Response(FILE_FAIL, HttpStatus.BAD_REQUEST);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			return new Response(FILE_FAIL, HttpStatus.BAD_REQUEST);
		}
	}
}
