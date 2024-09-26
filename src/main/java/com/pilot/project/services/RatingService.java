package com.pilot.project.services;

import com.pilot.project.payloads.RatingDTO;

import java.util.List;

public interface RatingService {
    RatingDTO saveRating(String email, String hotelId, RatingDTO ratingDTO);
    RatingDTO updateRating(String email, String hotelId, String id, RatingDTO ratingDTO);
    void deleteRating(String id);
    List<RatingDTO> getRatings();
    RatingDTO getRatingById(String id);
    List<RatingDTO> findAllRatingsByUser(String email);
    List<RatingDTO> findAllRatingsByHotel(String hotelId);
    void deleteAllRatingsByUser(String email);
    void deleteAllRatingsByHotel(String hotelId);
    void deleteAllRatings();
}
