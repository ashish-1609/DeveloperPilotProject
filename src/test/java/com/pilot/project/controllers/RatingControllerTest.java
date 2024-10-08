package com.pilot.project.controllers;

import com.pilot.project.payloads.ApiResponse;
import com.pilot.project.payloads.HotelDTO;
import com.pilot.project.payloads.RatingDTO;
import com.pilot.project.payloads.UserDTO;
import com.pilot.project.services.RatingService;
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
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RatingControllerTest {

    @InjectMocks
    private RatingController ratingController;

    @Mock
    private RatingService ratingService;
    @Mock
    private BindingResult bindingResult;

    private UserDTO userDTO;
    private HotelDTO hotelDTO;
    private RatingDTO ratingDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("test")
                .email("test@email.com")
                .password("password")
                .about("Test About")
                .build();
        hotelDTO =HotelDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Hotel")
                .city("Test City")
                .address("Test Address")
                .build();
        ratingDTO=RatingDTO.builder()
                .id(UUID.randomUUID().toString())
                .points(10)
                .user(userDTO)
                .hotel(hotelDTO)
                .build();
    }

    @Test
    void saveRating() {
        ApiResponse apiResponse = new ApiResponse("Rating Added Successfully");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(ratingService.saveRating(userDTO.getId(), hotelDTO.getId(), ratingDTO)).thenReturn(ratingDTO);
        ResponseEntity<?> responseEntity = ratingController.saveRating(userDTO.getId(), hotelDTO.getId(), ratingDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(apiResponse, responseEntity.getBody());

    }

    @Test
    void saveRating_InvalidRating(){
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("rating", "points", "Hotel can be rated only in range of 0-10.")));
        ResponseEntity<?> responseEntity = ratingController.saveRating(userDTO.getId(), hotelDTO.getId(), ratingDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(List.of("Hotel can be rated only in range of 0-10.").toString(), ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }
    @Test
    void saveRating_InternalServerError(){
        ApiResponse apiResponse = new ApiResponse("Internal Server Error");
        when(ratingController.saveRating(userDTO.getId(), hotelDTO.getId(), ratingDTO)).thenThrow(HttpServerErrorException.InternalServerError.class);
        ResponseEntity<ApiResponse> apiResponseResponseEntity = ratingController.saveRating(userDTO.getId(), hotelDTO.getId(), ratingDTO);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, apiResponseResponseEntity.getStatusCode());
        assertEquals(apiResponse, apiResponseResponseEntity.getBody());
    }

    @Test
    void updateRating() {
        ApiResponse apiResponse = new ApiResponse("Rating Updated Successfully");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(ratingService.updateRating(userDTO.getId(), hotelDTO.getId(), ratingDTO.getId(), ratingDTO)).thenReturn(ratingDTO);

        ResponseEntity<?> responseEntity = ratingController.updateRating(userDTO.getId(), hotelDTO.getId(), ratingDTO.getId(), ratingDTO);
            assertEquals(apiResponse, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateRating_InvalidRating(){
        ResponseEntity<ApiResponse> apiResponseResponseEntity = ratingController.updateRating(userDTO.getId(), hotelDTO.getId(), ratingDTO.getId(), ratingDTO);
        assertEquals(HttpStatus.BAD_REQUEST, apiResponseResponseEntity.getStatusCode());
        assertEquals(List.of("Hotel can be rated in range of 0-10.").toString(),((ApiResponse) Objects.requireNonNull(apiResponseResponseEntity.getBody())).getMessage());
    }

    @Test
    void deleteRating() {
        ResponseEntity<?> responseEntity = ratingController.deleteRating(ratingDTO.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Rating Deleted Successfully", ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }

    @Test
    void getAllRatings() {
        when(ratingService.getRatings()).thenReturn(List.of(ratingDTO));
        ResponseEntity<?> responseEntity = ratingController.getAllRatings();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(List.of(ratingDTO), responseEntity.getBody());

    }

    @Test
    void getRatingsByUser() {
        when(ratingService.findAllRatingsByUser(userDTO.getId())).thenReturn(List.of(ratingDTO));
        ResponseEntity<?> responseEntity = ratingController.getRatingsByUser(userDTO.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ratingService.findAllRatingsByUser(userDTO.getId()), responseEntity.getBody());
    }

    @Test
    void getRatingsByHotel() {
        when(ratingService.findAllRatingsByHotel(hotelDTO.getId())).thenReturn(List.of(ratingDTO));
        ResponseEntity<?> responseEntity = ratingController.getRatingsByHotel(hotelDTO.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ratingService.findAllRatingsByHotel(hotelDTO.getId()), responseEntity.getBody());
    }

    @Test
    void getRatingById() {
        when(ratingService.getRatingById(ratingDTO.getId())).thenReturn(ratingDTO);
        ResponseEntity<?> responseEntity = ratingController.getRatingById(ratingDTO.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ratingDTO, responseEntity.getBody());
    }

    @Test
    void deleteRatingByUserId() {
        ResponseEntity<ApiResponse> apiResponseResponseEntity = ratingController.deleteRatingByUser(userDTO.getId());
        assertEquals(HttpStatus.OK, apiResponseResponseEntity.getStatusCode());
        assertEquals("Ratings Deleted Successfully", ((ApiResponse) Objects.requireNonNull(apiResponseResponseEntity.getBody())).getMessage());
    }

    @Test
    void deleteRatingByHotelId() {

        ResponseEntity<ApiResponse> apiResponseResponseEntity = ratingController.deleteRatingByHotelId(hotelDTO.getId());
        assertEquals(HttpStatus.OK, apiResponseResponseEntity.getStatusCode());
        assertEquals("Ratings Deleted Successfully", ((ApiResponse) Objects.requireNonNull(apiResponseResponseEntity.getBody())).getMessage());
    }

    @Test
    void deleteAllRatings() {
        ResponseEntity<ApiResponse> apiResponseResponseEntity = ratingController.deleteAllRatings();
        assertEquals(HttpStatus.OK, apiResponseResponseEntity.getStatusCode());
        assertEquals("All Ratings Deleted Successfully", ((ApiResponse) Objects.requireNonNull(apiResponseResponseEntity.getBody())).getMessage());
    }
}