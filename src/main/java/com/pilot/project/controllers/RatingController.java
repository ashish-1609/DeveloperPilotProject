package com.pilot.project.controllers;


import com.pilot.project.payloads.ApiResponse;
import com.pilot.project.payloads.RatingDTO;
import com.pilot.project.services.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/ratings")
@Log4j2
@Tag(name = "Rating Controller", description = "Perform all the operation for adding, fetching, deleting and modifying the Details of Ratings.")
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }


    @Operation(summary = "Add Rating", description = "Add Rating in the system, and provide the user id and hotel id.")
    @PostMapping("/user/{userId}/hotel/{hotelId}")
    public ResponseEntity<?> saveRating(@PathVariable String userId, @PathVariable String hotelId, @RequestBody @Valid RatingDTO ratingDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.error("getting errors in Adding Rating : {}", bindingResult.getFieldErrors().stream().map(
                    DefaultMessageSourceResolvable::getDefaultMessage
            ).toList());
            return new ResponseEntity<>(new ApiResponse(bindingResult.getFieldErrors().stream().map(
                    DefaultMessageSourceResolvable::getDefaultMessage
            ).toList().toString(),false), HttpStatus.BAD_REQUEST);
        }
        try{
            return new ResponseEntity<>(this.ratingService.saveRating(userId, hotelId, ratingDTO), HttpStatus.OK);
        }catch(Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(new ApiResponse("Internal Server Error", false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete Rating By Id", description = "Delete the details of a Rating using it's ID.")
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<?> deleteRating(@PathVariable String ratingId) {
        this.ratingService.deleteRating(ratingId);
        return new ResponseEntity<>(new ApiResponse("Rating Deleted Successfully", true), HttpStatus.OK);
    }

    @Operation(summary = "Get All Ratings", description = "Fetch details of All Ratings.")
    @GetMapping("/")
    public ResponseEntity<List<RatingDTO>> getAllRatings(){
        return new ResponseEntity<>(this.ratingService.getRatings(), HttpStatus.OK);
    }

    @Operation(summary = "Get All Ratings by User Id", description = "Fetch details of the Ratings by user ID.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RatingDTO>> getRatingsByUser(@PathVariable String userId){
        return new ResponseEntity<>(this.ratingService.findAllRatingsByUser(userId), HttpStatus.OK);
    }

    @Operation(summary = "Get All Ratings by Hotel Id", description = "Fetch details of All Ratings by Hotel ID.")
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<RatingDTO>> getRatingsByHotel(@PathVariable String hotelId){
        return new ResponseEntity<>(this.ratingService.findAllRatingsByHotel(hotelId), HttpStatus.OK);
    }

    @Operation(summary = "Get the Rating by Id", description = "Fetch details of the Rating by it's ID.")
    @GetMapping("/{ratingId}")
    public ResponseEntity<RatingDTO> getRatingById(@PathVariable String ratingId){
        return new ResponseEntity<>(this.ratingService.getRatingById(ratingId), HttpStatus.OK);
    }

    @Operation(summary = "Delete All Ratings By User Id", description = "Delete the details of all Ratings using User ID.")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteRatingByUserId(@PathVariable String userId){
        this.ratingService.deleteAllRatingsByUser(userId);
        return new ResponseEntity<>(new ApiResponse("Ratings Deleted Successfully", true), HttpStatus.OK);
    }

    @Operation(summary = "Delete All Ratings By Hotel Id", description = "Delete the details of all Ratings using Hotel ID.")
    @DeleteMapping("/{hotelId}")
    public ResponseEntity<ApiResponse> deleteRatingByHotelId(@PathVariable String hotelId){
        this.ratingService.deleteAllRatingsByUser(hotelId);
        return new ResponseEntity<>(new ApiResponse("Ratings Deleted Successfully", true), HttpStatus.OK);
    }

    @Operation(summary = "Delete All Ratings", description = "Delete the details of all ratings present in the system.")
    @DeleteMapping("/")
    public ResponseEntity<ApiResponse> deleteAllRatings(){
        this.ratingService.deleteAllRatings();
        return new ResponseEntity<>(new ApiResponse("All Ratings Deleted Successfully", true), HttpStatus.OK);
    }

}
