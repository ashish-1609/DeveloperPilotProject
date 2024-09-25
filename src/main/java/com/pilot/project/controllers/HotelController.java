package com.pilot.project.controllers;

import com.pilot.project.payloads.ApiResponse;
import com.pilot.project.payloads.HotelDTO;
import com.pilot.project.services.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@Tag(name = "Hotel Controller", description = "Perform all the operation for adding, fetching, deleting and modifying the Details of hotels.")
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "Get All Hotels", description = "Fetch details of all the Hotels.")
    @GetMapping("/")
    public ResponseEntity<List<HotelDTO>> getAllHotels() {
        return new ResponseEntity<>(this.hotelService.getHotels(), HttpStatus.OK);
    }

    @Operation(summary = "Add Hotel", description = "Add hotel in the system.")
    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveHotel(@RequestBody @Valid HotelDTO hotelDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            return new ResponseEntity<>(new ApiResponse(bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList().toString(), false), HttpStatus.BAD_REQUEST);
        }
        if (Boolean.TRUE.equals(this.hotelService.existByName(hotelDTO.getHotelName()))) {
            return new ResponseEntity<>(new ApiResponse("Hotel already exists with this name: " + hotelDTO.getHotelName(), false), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("Hotel Added Successfully",true, this.hotelService.saveHotel(hotelDTO)), HttpStatus.OK);
    }

    @Operation(summary = "Delete Hotel By Id", description = "Delete the details of a hotel using it's ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteHotel(@PathVariable String id) {
        this.hotelService.deleteHotel(id);
        return new ResponseEntity<>(new ApiResponse("Hotel deleted successfully", true), HttpStatus.OK);
    }

    @Operation(summary = "Update Hotel By Id", description = "Update the details of a hotel using it's ID.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateHotel(@PathVariable String id, @RequestBody @Valid HotelDTO hotelDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ApiResponse(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), false), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse("Hotel Updated Successfully", true, this.hotelService.updateHotel(id, hotelDTO)), HttpStatus.OK);
    }

    @Operation(summary = "Get the Hotel by Id", description = "Fetch details of the Hotel by it's ID.")
    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable String id) {
        return new ResponseEntity<>(this.hotelService.getHotelById(id), HttpStatus.OK);
    }


}
