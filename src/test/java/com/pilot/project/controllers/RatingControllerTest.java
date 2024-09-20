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
                .userId(UUID.randomUUID().toString())
                .name("test")
                .email("test@email.com")
                .password("password")
                .about("Test About")
                .build();
        hotelDTO =HotelDTO.builder()
                .hotelId(UUID.randomUUID().toString())
                .hotelName("Test Hotel")
                .hotelCity("Test City")
                .hotelAddress("Test Address")
                .build();
        ratingDTO=RatingDTO.builder()
                .ratingId(UUID.randomUUID().toString())
                .rating(10)
                .user(userDTO)
                .hotel(hotelDTO)
                .build();
    }

    @Test
    void saveRating() {
        when(this.bindingResult.hasErrors()).thenReturn(false);
        when(this.ratingService.saveRating(userDTO.getUserId(), hotelDTO.getHotelId(), ratingDTO)).thenReturn(ratingDTO);
        ResponseEntity<?> responseEntity = this.ratingController.saveRating(userDTO.getUserId(), hotelDTO.getHotelId(), ratingDTO, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ratingDTO, responseEntity.getBody());

    }

    @Test
    public void saveRating_InvalidRating(){
        when(this.bindingResult.hasErrors()).thenReturn(true);
        when(this.bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("rating", "rating", "Hotel can be rated only in range of 0-10.")));
        ResponseEntity<?> responseEntity = this.ratingController.saveRating(userDTO.getUserId(), hotelDTO.getHotelId(), ratingDTO, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(List.of("Hotel can be rated only in range of 0-10.").toString(), ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }

    @Test
    void updateRating() {
        when(this.bindingResult.hasErrors()).thenReturn(false);
        when(this.ratingService.updateRating(userDTO.getUserId(), hotelDTO.getHotelId(), ratingDTO.getRatingId(), ratingDTO)).thenReturn(ratingDTO);
        ResponseEntity<?> responseEntity = this.ratingController.updateRating(userDTO.getUserId(), hotelDTO.getHotelId(), ratingDTO.getRatingId(), ratingDTO, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ratingDTO, responseEntity.getBody());
    }

    @Test
    void deleteRating() {
        ResponseEntity<?> responseEntity = this.ratingController.deleteRating(ratingDTO.getRatingId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Rating Deleted Successfully", ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }

    @Test
    void getAllRatings() {
        when(this.ratingService.getRatings()).thenReturn(List.of(ratingDTO));
        ResponseEntity<?> responseEntity = this.ratingController.getAllRatings();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(List.of(ratingDTO), responseEntity.getBody());

    }

    @Test
    void getRatingsByUser() {
        when(this.ratingService.findAllRatingsByUser(userDTO.getUserId())).thenReturn(List.of(ratingDTO));
        ResponseEntity<?> responseEntity = this.ratingController.getRatingsByUser(userDTO.getUserId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(this.ratingService.findAllRatingsByUser(userDTO.getUserId()), responseEntity.getBody());
    }

    @Test
    void getRatingsByHotel() {
        when(this.ratingService.findAllRatingsByHotel(hotelDTO.getHotelId())).thenReturn(List.of(ratingDTO));
        ResponseEntity<?> responseEntity = this.ratingController.getRatingsByHotel(hotelDTO.getHotelId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(this.ratingService.findAllRatingsByHotel(hotelDTO.getHotelId()), responseEntity.getBody());
    }

    @Test
    void getRatingById() {
        when(this.ratingService.getRatingById(ratingDTO.getRatingId())).thenReturn(ratingDTO);
        ResponseEntity<?> responseEntity = this.ratingController.getRatingById(ratingDTO.getRatingId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ratingDTO, responseEntity.getBody());
    }

    @Test
    void deleteRatingByUserId() {
        ResponseEntity<ApiResponse> apiResponseResponseEntity = this.ratingController.deleteRatingByUserId(userDTO.getUserId());
        assertEquals(HttpStatus.OK, apiResponseResponseEntity.getStatusCode());
        assertEquals("Ratings Deleted Successfully", ((ApiResponse) Objects.requireNonNull(apiResponseResponseEntity.getBody())).getMessage());
    }

    @Test
    void deleteRatingByHotelId() {
        ResponseEntity<ApiResponse> apiResponseResponseEntity = this.ratingController.deleteRatingByUserId(hotelDTO.getHotelId());
        assertEquals(HttpStatus.OK, apiResponseResponseEntity.getStatusCode());
        assertEquals("Ratings Deleted Successfully", ((ApiResponse) Objects.requireNonNull(apiResponseResponseEntity.getBody())).getMessage());
    }

    @Test
    void deleteAllRatings() {
        ResponseEntity<ApiResponse> apiResponseResponseEntity = this.ratingController.deleteAllRatings();
        assertEquals(HttpStatus.OK, apiResponseResponseEntity.getStatusCode());
        assertEquals("All Ratings Deleted Successfully", ((ApiResponse) Objects.requireNonNull(apiResponseResponseEntity.getBody())).getMessage());
    }
}