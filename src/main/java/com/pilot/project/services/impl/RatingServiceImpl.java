package com.pilot.project.services.impl;

import com.pilot.project.entities.Rating;
import com.pilot.project.entities.User;
import com.pilot.project.exceptions.ResourceNotFoundException;
import com.pilot.project.payloads.RatingDTO;
import com.pilot.project.repositories.HotelRepository;
import com.pilot.project.repositories.RatingRepository;
import com.pilot.project.repositories.UserRepository;
import com.pilot.project.services.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RatingServiceImpl implements RatingService  {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public RatingServiceImpl( RatingRepository ratingRepository,
                              ModelMapper modelMapper,
                              UserRepository userRepository,
                              HotelRepository hotelRepository ) {
        this.ratingRepository=ratingRepository;
        this.modelMapper=modelMapper;
        this.userRepository=userRepository;
        this.hotelRepository=hotelRepository;
    }

    @Override
    public RatingDTO saveRating(String userId, String hotelId, RatingDTO ratingDTO) {
        ratingDTO.setRatingId(UUID.randomUUID().toString());
        Rating rating = this.modelMapper.map(ratingDTO, Rating.class);
        rating.setUser(userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User", "id", userId)));
        rating.setHotel(hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException("Hotel", "id", hotelId)));
        return this.modelMapper.map(this.ratingRepository.save(rating), RatingDTO.class);
    }

    @Override
    public RatingDTO updateRating(String userId, String hotelId, String id, RatingDTO ratingDTO) {
        Rating rating = this.ratingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));
        rating.setRating(ratingDTO.getRating());
        rating.setHotel(this.hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException("Hotel", "id", hotelId)));
        rating.setUser(this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)));
        return this.modelMapper.map(
                this.ratingRepository.save(rating),
                RatingDTO.class);
    }

    @Override
    public void deleteRating(String id) {
        Rating rating = this.ratingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id));
        this.ratingRepository.delete(rating);
    }

    @Override
    public List<RatingDTO> getRatings() {
        List<Rating> ratings = this.ratingRepository.findAll();
        return ratings.stream().map(
                rating -> this.modelMapper.map(rating, RatingDTO.class)
        ).toList();
    }

    @Override
    public RatingDTO getRatingById(String id) {
        return this.modelMapper.map(
                this.ratingRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id)),
                RatingDTO.class);
    }

    @Override
    public List<RatingDTO> findAllRatingsByUser(String userId) {
        List<Rating> ratings = this.ratingRepository.findAllByUser(this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId)));
        return ratings.stream().map(rating -> this.modelMapper.map(rating, RatingDTO.class)).toList();
    }

    @Override
    public List<RatingDTO> findAllRatingsByHotel(String hotelId) {
        List<Rating> ratings = this.ratingRepository.findAllByHotel(this.hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hotelId)));
        return ratings.stream().map(rating -> this.modelMapper.map(rating, RatingDTO.class)).toList();
    }

    @Override
    public void deleteAllRatingsByUser(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        this.ratingRepository.deleteAllByUser(user);
    }

    @Override
    public void deleteAllRatingsByHotel(String hotelId) {
        this.ratingRepository.deleteAllByHotel(this.hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException("Hotel", "id", hotelId)));
    }

    @Override
    public void deleteAllRatings() {
        this.ratingRepository.deleteAll();
    }
}
