package com.pilot.project.services;

import com.pilot.project.payloads.RatingDTO;

import java.util.List;

public interface RatingService {
    RatingDTO saveRating(String userId, String hotelId, RatingDTO ratingDTO);
    RatingDTO updateRating(String userId, String hotelId, String id, RatingDTO ratingDTO);
    void deleteRating(String id);
    List<RatingDTO> getRatings();
    RatingDTO getRatingById(String id);
    List<RatingDTO> findAllRatingsByUser(String userId);
    List<RatingDTO> findAllRatingsByHotel(String hotelId);
    void deleteAllRatingsByUser(String userId);
    void deleteAllRatingsByHotel(String hotelId);
    void deleteAllRatings();
}
