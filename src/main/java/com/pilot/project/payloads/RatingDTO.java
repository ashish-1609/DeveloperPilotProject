package com.pilot.project.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RatingDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    @Range(min = 0, max = 10, message = "Hotel can be rated only in range of 0-10.")
    @NotNull(message = "Rating Field Cannot be Blank.")
    private int points;
    @Size(min = 2, max = 100, message = "Comment Should be in between 2-100 characters.")
    @NotBlank(message = "Comment Field Cannot be empty.")
    private String comment;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(hidden = true, accessMode = Schema.AccessMode.READ_ONLY)
    private UserDTO user;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(hidden = true, accessMode = Schema.AccessMode.READ_ONLY)
    private HotelDTO hotel;
}
