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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelControllerTest {
    @InjectMocks
    private HotelController hotelController;

    @Mock
    private HotelService hotelService;

    @Mock
    private BindingResult bindingResult;

    private HotelDTO hotelDTO;

    @BeforeEach
    void setUp() {
        hotelDTO =HotelDTO.builder()
                .hotelId(UUID.randomUUID().toString())
                .hotelName("Test Hotel")
                .hotelCity("Test City")
                .hotelAddress("Test Address")
                .build();
    }

    @Test
    void getAllHotels() {
        when(this.hotelService.getHotels()).thenReturn(List.of(hotelDTO));
        ResponseEntity<List<HotelDTO>> response = this.hotelController.getAllHotels();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(hotelDTO), response.getBody());
    }

    @Test
    public void saveHotel_HotelExist(){
        when(this.hotelService.existByName(hotelDTO.getHotelName())).thenReturn(true);
        ResponseEntity<?> responseEntity = this.hotelController.saveHotel(hotelDTO, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Hotel already exists with this name: " +hotelDTO.getHotelName(), ((ApiResponse)responseEntity.getBody()).getMessage());
    }

    @Test
    void saveHotel() {
        when(this.hotelService.existByName(hotelDTO.getHotelName())).thenReturn(false);
        when(this.bindingResult.hasErrors()).thenReturn(false);
        when(this.hotelService.saveHotel(hotelDTO)).thenReturn(hotelDTO);
        ResponseEntity<?> responseEntity = this.hotelController.saveHotel(hotelDTO, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(hotelDTO, responseEntity.getBody());
    }
    @Test
    public void saveHotel_InvalidHotel(){
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("Hotel","hotelName","Hotel's name cannot be empty.")));
        ResponseEntity<?> responseEntity = this.hotelController.saveHotel(hotelDTO, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(List.of("Hotel's name cannot be empty.").toString(),((ApiResponse)responseEntity.getBody()).getMessage());
    }

    @Test
    void deleteHotel() {
        ResponseEntity<ApiResponse> apiResponseResponseEntity = this.hotelController.deleteHotel(hotelDTO.getHotelId());
        assertEquals(HttpStatus.OK, apiResponseResponseEntity.getStatusCode());
        assertEquals("Hotel deleted successfully", ((ApiResponse) apiResponseResponseEntity.getBody()).getMessage());

    }

    @Test
    void updateHotel() {
        when(this.bindingResult.hasErrors()).thenReturn(false);
        when(this.hotelService.updateHotel(hotelDTO.getHotelId(), hotelDTO)).thenReturn(hotelDTO);
        ResponseEntity<?> responseEntity = this.hotelController.updateHotel(hotelDTO.getHotelId(), hotelDTO, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(hotelDTO, responseEntity.getBody());


    }

    @Test
    void getHotelById() {
        when(this.hotelService.getHotelById(hotelDTO.getHotelId())).thenReturn(hotelDTO);
        ResponseEntity<HotelDTO> responseEntity = this.hotelController.getHotelById(hotelDTO.getHotelId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(hotelDTO, responseEntity.getBody());
    }
}