package com.pilot.project.controllers;

import com.pilot.project.exceptions.CustomJobExecutionException;
import com.pilot.project.payloads.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BatchControllerTest {
    @InjectMocks
    private BatchController batchController;

    @Mock
    private MockMvc mockMvc;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job hotelJob;

    @Test
    void testHotelBatchController_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hotels.csv", "text/csv", "some data".getBytes());
        ResponseEntity<ApiResponse> response = batchController.hotelBatchController(file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hotels Added Successfully", Objects.requireNonNull(response.getBody()).getMessage());

        verify(jobLauncher, times(1)).run(any(Job.class), any(JobParameters.class));
    }

    @Test
    void testHotelBatchController_JobExecutionAlreadyRunning() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "hotels.csv", "text/csv", "some data".getBytes());

        doThrow(new JobExecutionAlreadyRunningException("Job already running"))
                .when(jobLauncher).run(any(Job.class), any(JobParameters.class));

        CustomJobExecutionException exception = assertThrows(
                CustomJobExecutionException.class,
                () -> batchController.hotelBatchController(file)
        );

        assertEquals("Job already running", exception.getMessage());
    }
}
