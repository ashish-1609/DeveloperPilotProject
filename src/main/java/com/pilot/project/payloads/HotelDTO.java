package com.pilot.project.payloads;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelDTO {
    private String hotelId;
    @NotBlank(message = "Hotel's name cannot be empty.")
    @Size(min = 2, max = 50, message = "Hotel's name should be in range of 2-50 characters.")
    private String hotelName;
    @NotBlank(message = "Hotel's Address cannot be empty.")
    private String hotelAddress;
    @NotBlank(message = "Hotel's city cannot be empty.")
    private String hotelCity;

}
