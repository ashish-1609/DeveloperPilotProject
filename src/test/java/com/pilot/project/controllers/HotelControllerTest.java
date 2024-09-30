package com.pilot.project.controllers;


import com.pilot.project.payloads.ApiResponse;
import com.pilot.project.payloads.HotelDTO;
import com.pilot.project.services.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelControllerTest {
    @InjectMocks
    private HotelController hotelController;

    @Mock
    private HotelService hotelService;


    private HotelDTO hotelDTO;

    @BeforeEach
    void setUp() {
        hotelDTO =HotelDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Hotel")
                .city("Test City")
                .address("Test Address")
                .build();
    }

    @Test
    void getAllHotels() {
        when(hotelService.getHotels()).thenReturn(List.of(hotelDTO));
        ResponseEntity<List<HotelDTO>> response = hotelController.getAllHotels();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(hotelDTO), response.getBody());
    }

    @Test
    void saveHotel_HotelExist(){
        when(hotelService.existByName(hotelDTO.getName())).thenReturn(true);
        ResponseEntity<?> responseEntity = hotelController.saveHotel(hotelDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Hotel already exists with this name: " +hotelDTO.getName(), ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }

    @Test
    void saveHotel() {
        ApiResponse apiResponse = new ApiResponse("Hotel Added Successfully");
        when(hotelService.existByName(hotelDTO.getName())).thenReturn(false);
        when(hotelService.saveHotel(hotelDTO)).thenReturn(hotelDTO);
        ResponseEntity<?> responseEntity = hotelController.saveHotel(hotelDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(apiResponse, responseEntity.getBody());
    }
    @Test
    void saveHotel_InvalidHotel(){
        ResponseEntity<?> responseEntity = hotelController.saveHotel(hotelDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(List.of("Hotel's name cannot be empty.").toString(),((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }

    @Test
    void deleteHotel() {
        when(hotelService.getHotelById(hotelDTO.getId())).thenReturn(hotelDTO);
        assertNotNull(hotelController.getHotelById(hotelDTO.getId()));
        ResponseEntity<ApiResponse> apiResponseResponseEntity = hotelController.deleteHotel(hotelDTO.getId());
        assertEquals(HttpStatus.OK, apiResponseResponseEntity.getStatusCode());
        assertEquals("Hotel deleted successfully", ((ApiResponse) Objects.requireNonNull(apiResponseResponseEntity.getBody())).getMessage());
    }

    @Test
    void updateHotel() {
        ApiResponse apiResponse = new ApiResponse("Hotel Updated Successfully");
        when(hotelService.updateHotel(hotelDTO.getId(), hotelDTO)).thenReturn(hotelDTO);
        ResponseEntity<?> responseEntity = hotelController.updateHotel(hotelDTO.getId(), hotelDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(apiResponse, responseEntity.getBody());
    }

    @Test
    void getHotelById() {
        when(hotelService.getHotelById(hotelDTO.getId())).thenReturn(hotelDTO);
        ResponseEntity<HotelDTO> responseEntity = hotelController.getHotelById(hotelDTO.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(hotelDTO, responseEntity.getBody());
    }
}