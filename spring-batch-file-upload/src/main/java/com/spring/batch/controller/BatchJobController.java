package com.spring.batch.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.batch.constant.Constant;
import com.spring.batch.entity.Customer;
import com.spring.batch.exception.Response;
import com.spring.batch.repository.CustomerRepository;

@RestController
@RequestMapping("/api")
public class BatchJobController implements Constant {

	@Autowired
	private Job job;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private CustomerRepository repository;

	@PostMapping(path = "/importFile")
	public Response startBatch(@RequestParam("file") MultipartFile multipartFile) {
		try {
			String fileName = multipartFile.getOriginalFilename();
			File fileLocation = new File(TEMP_STORAGE + fileName);
			multipartFile.transferTo(fileLocation);

			JobParameters jobParameters = new JobParametersBuilder().addString("filePath", TEMP_STORAGE + fileName)
					.addLong("startAt", System.currentTimeMillis()).toJobParameters();

			JobExecution execution = jobLauncher.run(job, jobParameters);
			if (execution.getExitStatus().equals(ExitStatus.COMPLETED)) {
//				Files.deleteIfExists(Paths.get(TEMP_STORAGE + originalFileName));
				return new Response(FILE_SUCCESS, HttpStatus.OK);
			} else {
				return new Response(FILE_FAIL, HttpStatus.BAD_REQUEST);
			}
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException | IOException e) {
			return new Response(FILE_FAIL, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getAll")
	public List<Customer> getAll() {
		return repository.findAll();
	}
}
