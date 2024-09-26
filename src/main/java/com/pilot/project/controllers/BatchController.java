package com.pilot.project.controllers;

import com.pilot.project.exceptions.CustomJobExecutionException;
import com.pilot.project.payloads.ApiResponse;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@RestController
public class BatchController {
    private final JobLauncher jobLauncher;
    private final Job hotelJob;

    @Autowired
    public BatchController(JobLauncher jobLauncher, Job hotelJob) {
        this.jobLauncher = jobLauncher;
        this.hotelJob = hotelJob;
    }

    @PostMapping("/hotels-upload/")
    public ResponseEntity<ApiResponse> hotelBatchController(@RequestParam("file") MultipartFile file) throws IOException {


        String fileName = file.getOriginalFilename();
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse("Empty file, Please insert a file"), HttpStatus.BAD_REQUEST);
        }
        if(!Objects.requireNonNull(file.getOriginalFilename()).contains(".csv")){
            return new ResponseEntity<>(new ApiResponse("Invalid file, Please insert a .csv file"), HttpStatus.BAD_REQUEST);
        }


        String path = new ClassPathResource("/files/").getFile().getAbsolutePath();
//        String path =System.getProperty("user.dir")+"\\src\\main\\resources\\";
        File fileToSave = new File(path +fileName);
        file.transferTo(fileToSave);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", path +fileName)
        .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(hotelJob, jobParameters);
            return new ResponseEntity<>(new ApiResponse("Hotels Added Successfully"), HttpStatus.OK);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new CustomJobExecutionException(e.getMessage());
        }
    }

}
