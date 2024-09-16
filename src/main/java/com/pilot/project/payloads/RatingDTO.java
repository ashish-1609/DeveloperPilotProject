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
    @NotBlank(message = "Please provide a user.")
    private String userId;
    @NotBlank(message = "Please Select a Hotel.")
    private String hotelId;
    @Range(min = 0, max = 10, message = "A hotel can be rated only in range of 0-10.")
    private int rating;
    private User user;
    private Hotel hotel;
}
