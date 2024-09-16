package com.pilot.project.services;

import com.pilot.project.payloads.RatingDTO;

import java.util.List;

public interface RatingService {
    RatingDTO saveRating(RatingDTO ratingDTO);
    RatingDTO updateRating(RatingDTO ratingDTO);
    void deleteRating(RatingDTO ratingDTO);
    List<RatingDTO> getRatings();
    RatingDTO getRatingById(String id);
}
