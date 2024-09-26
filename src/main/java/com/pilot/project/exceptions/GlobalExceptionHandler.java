package com.pilot.project.exceptions;

import com.pilot.project.payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        return new ResponseEntity<>(
                new ApiResponse(
                        ex.getMessage().toUpperCase())
                , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomJobExecutionException.class)
    public ResponseEntity<ApiResponse> customJobExecutionExceptionHandler(CustomJobExecutionException ex){
        return new ResponseEntity<>(new ApiResponse(ex.getMessage().toUpperCase()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
