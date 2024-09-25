package com.pilot.project.exceptions;

import com.pilot.project.payloads.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void resourceNotFoundExceptionHandler(){
       ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException("User","id","kvlacbssvcdsjsvs");
        ResponseEntity<ApiResponse> apiResponseResponseEntity = globalExceptionHandler.resourceNotFoundExceptionHandler(resourceNotFoundException);
        assertNotNull(apiResponseResponseEntity);
        assertNotNull(apiResponseResponseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND ,apiResponseResponseEntity.getStatusCode());
        assertEquals(resourceNotFoundException.getMessage().toUpperCase(), Objects.requireNonNull(apiResponseResponseEntity.getBody()).getMessage());
    }

    @Test
    void customJobExecutionExceptionHandler(){
        CustomJobExecutionException customJobExecutionException = new CustomJobExecutionException("Job Execution Exception");
        ResponseEntity<ApiResponse> apiResponseResponseEntity = globalExceptionHandler.customJobExecutionExceptionHandler(customJobExecutionException);
        assertNotNull(apiResponseResponseEntity);
        assertNotNull(apiResponseResponseEntity.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,apiResponseResponseEntity.getStatusCode());
        assertEquals(customJobExecutionException.getMessage().toUpperCase(), Objects.requireNonNull(apiResponseResponseEntity.getBody()).getMessage());

    }

}