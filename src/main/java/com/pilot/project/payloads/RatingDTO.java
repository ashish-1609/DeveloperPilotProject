package com.pilot.project.payloads;

import com.pilot.project.entities.Hotel;
import com.pilot.project.entities.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDTO {
    private String ratingId;
    @Range(min = 0, max = 10, message = "A hotel can be rated only in range of 0-10.")
    private int rating;
    private UserDTO user;
    private HotelDTO hotel;
}
