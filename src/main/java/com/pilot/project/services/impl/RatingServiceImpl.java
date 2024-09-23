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
    private static final String RATING = "RATING";
    private static final String USER = "USER";
    private static final String HOTEL = "Hotel";
    private static final String ID = "ID";

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
        rating.setUser(userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException(USER, ID, userId)));

        rating.setHotel(hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException(HOTEL, ID, hotelId)));
        return this.modelMapper.map(this.ratingRepository.save(rating), RatingDTO.class);
    }

    @Override
    public RatingDTO updateRating(String userId, String hotelId, String id, RatingDTO ratingDTO) {
        Rating rating = this.ratingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RATING, ID, id));
        rating.setPoints(ratingDTO.getPoints());
        rating.setHotel(this.hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException(RATING, ID, hotelId)));
        rating.setUser(this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId)));
        return this.modelMapper.map(
                this.ratingRepository.save(rating),
                RatingDTO.class);
    }

    @Override
    public void deleteRating(String id) {
        Rating rating = this.ratingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RATING, ID, id));
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
                        .orElseThrow(() -> new ResourceNotFoundException(RATING, ID, id)),
                RatingDTO.class);
    }

    @Override
    public List<RatingDTO> findAllRatingsByUser(String userId) {
        List<Rating> ratings = this.ratingRepository.findAllByUser(this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId)));
        return ratings.stream().map(rating -> this.modelMapper.map(rating, RatingDTO.class)).toList();
    }

    @Override
    public List<RatingDTO> findAllRatingsByHotel(String hotelId) {
        List<Rating> ratings = this.ratingRepository.findAllByHotel(this.hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException(HOTEL, ID, hotelId)));
        return ratings.stream().map(rating -> this.modelMapper.map(rating, RatingDTO.class)).toList();
    }

    @Override
    public void deleteAllRatingsByUser(String userId) {

        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));
        List<Rating> allByUser = this.ratingRepository.findAllByUser(user);
        this.ratingRepository.deleteAll(allByUser);
    }

    @Override
    public void deleteAllRatingsByHotel(String hotelId) {
        List<Rating> ratings = this.ratingRepository.findAllByHotel(this.hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException(HOTEL, ID, hotelId)));
        this.ratingRepository.deleteAll(ratings);
    }

    @Override
    public void deleteAllRatings() {
        this.ratingRepository.deleteAll();
    }
}
