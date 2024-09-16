package com.pilot.project.services.impl;

import com.pilot.project.payloads.RatingDTO;
import com.pilot.project.repositories.RatingRepository;
import com.pilot.project.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository=ratingRepository;
    }

    @Override
    public RatingDTO saveRating(RatingDTO ratingDTO) {
        return null;
    }

    @Override
    public RatingDTO updateRating(RatingDTO ratingDTO) {
        return null;
    }

    @Override
    public void deleteRating(RatingDTO ratingDTO) {

    }

    @Override
    public List<RatingDTO> getRatings() {
        return List.of();
    }

    @Override
    public RatingDTO getRatingById(String id) {
        return null;
    }
}
