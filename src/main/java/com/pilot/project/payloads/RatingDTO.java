package com.pilot.project.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingDTO {
    private String ratingId;
    @Range(min = 0, max = 10, message = "Hotel can be rated only in range of 0-10.")
    @NotNull(message = "Rating Field Cannot be Blank.")
    private int rating;
    @Size(min = 2, max = 100, message = "Comment Should be in between 2-100 characters.")
    @NotBlank(message = "Comment Field Cannot be empty.")
    private String comment;
    private UserDTO user;
    private HotelDTO hotel;
}
