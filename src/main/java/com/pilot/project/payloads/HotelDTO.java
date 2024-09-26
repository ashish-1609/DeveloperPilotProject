package com.pilot.project.payloads;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @NotBlank(message = "Hotel's name cannot be empty.")
    @Size(min = 2, max = 50, message = "Hotel's name should be in range of 2-50 characters.")
    private String name;
    @NotBlank(message = "Hotel's Address cannot be empty.")
    private String address;
    @NotBlank(message = "Hotel's city cannot be empty.")
    private String city;

}
