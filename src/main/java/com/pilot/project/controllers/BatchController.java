package com.pilot.project.controllers;

import com.pilot.project.payloads.ApiResponse;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

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
            return new ResponseEntity<>(new ApiResponse("Empty file, Please insert a valid file"), HttpStatus.BAD_REQUEST);
        }
        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(".csv")) {
            return new ResponseEntity<>(new ApiResponse("Invalid file, Please insert a .csv file"), HttpStatus.BAD_REQUEST);
        }
        String path = new ClassPathResource("/").getFile().getAbsolutePath();
//        String path =System.getProperty("user.dir")+"\\src\\main\\resources\\";
        File fileToSave = new File(path + fileName);
        try {
            file.transferTo(fileToSave);
        } catch (IOException e) {
            return new ResponseEntity<>(new ApiResponse("Error while saving file"), HttpStatus.BAD_REQUEST);
        }

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("fullPathFileName", path + fileName)
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            JobExecution jobExecution = jobLauncher.run(hotelJob, jobParameters);
            if(jobExecution.getStatus()== BatchStatus.FAILED){
                return new ResponseEntity<>(new ApiResponse("Some Error Occurred : "+jobExecution.getAllFailureExceptions().stream().map(Throwable::getMessage).collect(Collectors.toSet())), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new ApiResponse("Hotels Added Successfully"), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse("Some Error Occurred : "+e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
