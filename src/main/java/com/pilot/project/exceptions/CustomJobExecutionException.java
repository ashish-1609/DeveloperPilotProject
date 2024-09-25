package com.pilot.project.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

@Getter
@Setter
public class CustomJobExecutionException extends RuntimeException {
    public CustomJobExecutionException(String msg) {
        super(msg);
    }
    public CustomJobExecutionException(JobExecutionAlreadyRunningException ex){
        super(ex.getMessage());
    }
    public CustomJobExecutionException(JobRestartException ex){
        super(ex.getMessage());
    }
    public CustomJobExecutionException(JobInstanceAlreadyCompleteException ex){
        super(ex.getMessage());
    }
    public CustomJobExecutionException(JobParametersInvalidException ex){
        super(ex.getMessage());
    }

}
