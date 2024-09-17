package com.pilot.project.payloads;

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
    private String comment;
    private UserDTO user;
    private HotelDTO hotel;
}
