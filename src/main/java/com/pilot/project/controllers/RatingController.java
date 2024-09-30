package com.pilot.project.controllers;

import com.pilot.project.payloads.ApiResponse;
import com.pilot.project.payloads.RatingDTO;
import com.pilot.project.services.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@Log4j2
@Tag(name = "Rating Controller", description = "Perform all the operation for adding, fetching, deleting and modifying the Details of Ratings.")
public class RatingController {
    private final RatingService ratingService;


    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Operation(summary = "Add Rating", description = "Add Rating in the system, and provide the user id and hotel id.")
    @PostMapping("/user/{email}/hotel/{hotelId}")
    public ResponseEntity<ApiResponse> saveRating(@PathVariable String email, @PathVariable String hotelId, @RequestBody @Valid RatingDTO ratingDTO) {
        try{
            ratingService.saveRating(email, hotelId, ratingDTO);
            return new ResponseEntity<>(new ApiResponse("Rating Added Successfully"), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(new ApiResponse("Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Rating By Id", description = "Update the details of a Rating using it's ID, and also provide User Id, Hotel Id.")
    @PutMapping("/user/{email}/hotel/{hotelId}/{ratingId}")
    public ResponseEntity<ApiResponse> updateRating(
            @PathVariable String email,
            @PathVariable String hotelId,
            @PathVariable String ratingId,
            @RequestBody @Valid RatingDTO ratingDTO) {
        ratingService.updateRating(email, hotelId,ratingId, ratingDTO);
        return new ResponseEntity<>(new ApiResponse("Rating Updated Successfully"), HttpStatus.OK);
    }

    @Operation(summary = "Delete Rating By Id", description = "Delete the details of a Rating using it's ID.")
    @DeleteMapping("/delete/{ratingId}")
    public ResponseEntity<ApiResponse> deleteRating(@PathVariable String ratingId) {
        ratingService.deleteRating(ratingId);
        return new ResponseEntity<>(new ApiResponse("Rating Deleted Successfully"), HttpStatus.OK);
    }

    @Operation(summary = "Get All Ratings", description = "Fetch details of All Ratings.")
    @GetMapping("/")
    public ResponseEntity<List<RatingDTO>> getAllRatings(){
        log.info("Get All Ratings");
        return new ResponseEntity<>(ratingService.getRatings(), HttpStatus.OK);
    }

    @Operation(summary = "Get All Ratings by User Id", description = "Fetch details of the Ratings by user ID.")
    @GetMapping("/user/{email}")
    public ResponseEntity<List<RatingDTO>> getRatingsByUser(@PathVariable String email){
        return new ResponseEntity<>(ratingService.findAllRatingsByUser(email), HttpStatus.OK);
    }

    @Operation(summary = "Get All Ratings by Hotel Id", description = "Fetch details of All Ratings by Hotel ID.")
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RatingDTO>> getRatingsByHotel(@PathVariable String hotelId){
        return new ResponseEntity<>(ratingService.findAllRatingsByHotel(hotelId), HttpStatus.OK);
    }

    @Operation(summary = "Get the Rating by Id", description = "Fetch details of the Rating by it's ID.")
    @GetMapping("/{ratingId}")
    public ResponseEntity<RatingDTO> getRatingById(@PathVariable String ratingId){
        return new ResponseEntity<>(ratingService.getRatingById(ratingId), HttpStatus.OK);
    }

    @Operation(summary = "Delete All Ratings By User Id", description = "Delete the details of all Ratings using User ID.")
    @DeleteMapping("/user/{email}")
    public ResponseEntity<ApiResponse> deleteRatingByUser(@PathVariable String email){
        ratingService.deleteAllRatingsByUser(email);
        return new ResponseEntity<>(new ApiResponse("Ratings Deleted Successfully"), HttpStatus.OK);
    }

    @Operation(summary = "Delete All Ratings By Hotel Id", description = "Delete the details of all Ratings using Hotel ID.")
    @DeleteMapping("/{hotelId}")
    public ResponseEntity<ApiResponse> deleteRatingByHotelId(@PathVariable String hotelId){
        ratingService.deleteAllRatingsByHotel(hotelId);
        return new ResponseEntity<>(new ApiResponse("Ratings Deleted Successfully"), HttpStatus.OK);
    }

    @Operation(summary = "Delete All Ratings", description = "Delete the details of all ratings present in the system.")
    @DeleteMapping("/")
    public ResponseEntity<ApiResponse> deleteAllRatings(){
        ratingService.deleteAllRatings();
        return new ResponseEntity<>(new ApiResponse("All Ratings Deleted Successfully"), HttpStatus.OK);
    }

}
