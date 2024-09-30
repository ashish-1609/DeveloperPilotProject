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
        when(this.hotelService.getHotels()).thenReturn(List.of(hotelDTO));
        ResponseEntity<List<HotelDTO>> response = this.hotelController.getAllHotels();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(hotelDTO), response.getBody());
    }

    @Test
    void saveHotel_HotelExist(){
        when(this.hotelService.existByName(hotelDTO.getName())).thenReturn(true);
        ResponseEntity<?> responseEntity = this.hotelController.saveHotel(hotelDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Hotel already exists with this name: " +hotelDTO.getName(), ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }

    @Test
    void saveHotel() {
        ApiResponse apiResponse = new ApiResponse("Hotel Added Successfully");
        when(this.hotelService.existByName(hotelDTO.getName())).thenReturn(false);
        when(this.hotelService.saveHotel(hotelDTO)).thenReturn(hotelDTO);
        ResponseEntity<?> responseEntity = this.hotelController.saveHotel(hotelDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(apiResponse, responseEntity.getBody());
    }
    @Test
    void saveHotel_InvalidHotel(){
        ResponseEntity<?> responseEntity = this.hotelController.saveHotel(hotelDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(List.of("Hotel's name cannot be empty.").toString(),((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }

    @Test
    void deleteHotel() {
        when(this.hotelService.getHotelById(hotelDTO.getId())).thenReturn(hotelDTO);
        assertNotNull(this.hotelController.getHotelById(hotelDTO.getId()));
        ResponseEntity<ApiResponse> apiResponseResponseEntity = this.hotelController.deleteHotel(hotelDTO.getId());
        assertEquals(HttpStatus.OK, apiResponseResponseEntity.getStatusCode());
        assertEquals("Hotel deleted successfully", ((ApiResponse) Objects.requireNonNull(apiResponseResponseEntity.getBody())).getMessage());
    }

    @Test
    void updateHotel() {
        ApiResponse apiResponse = new ApiResponse("Hotel Updated Successfully");
        when(this.hotelService.updateHotel(hotelDTO.getId(), hotelDTO)).thenReturn(hotelDTO);
        ResponseEntity<?> responseEntity = this.hotelController.updateHotel(hotelDTO.getId(), hotelDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(apiResponse, responseEntity.getBody());
    }

    @Test
    void getHotelById() {
        when(this.hotelService.getHotelById(hotelDTO.getId())).thenReturn(hotelDTO);
        ResponseEntity<HotelDTO> responseEntity = this.hotelController.getHotelById(hotelDTO.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(hotelDTO, responseEntity.getBody());
    }
}