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
        Rating rating = modelMapper.map(ratingDTO, Rating.class);
        rating.setUser(userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException(USER, ID, userId)));

        rating.setHotel(hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException(HOTEL, ID, hotelId)));
        return modelMapper.map(ratingRepository.save(rating), RatingDTO.class);
    }

    @Override
    public RatingDTO updateRating(String userId, String hotelId, String id, RatingDTO ratingDTO) {
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RATING, ID, id));
        rating.setPoints(ratingDTO.getPoints());
        rating.setHotel(hotelRepository.findById(hotelId).orElseThrow(()-> new ResourceNotFoundException(RATING, ID, hotelId)));
        rating.setUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId)));
        return modelMapper.map(
                ratingRepository.save(rating),
                RatingDTO.class);
    }

    @Override
    public void deleteRating(String id) {
        Rating rating = ratingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RATING, ID, id));
        ratingRepository.delete(rating);
    }

    @Override
    public List<RatingDTO> getRatings() {
        List<Rating> ratings = ratingRepository.findAll();
        return ratings.stream().map(
                rating -> modelMapper.map(rating, RatingDTO.class)
        ).toList();
    }

    @Override
    public RatingDTO getRatingById(String id) {
        return modelMapper.map(
                ratingRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(RATING, ID, id)),
                RatingDTO.class);
    }

    @Override
    public List<RatingDTO> findAllRatingsByUser(String userId) {
        List<Rating> ratings = ratingRepository.findAllByUser(userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId)));
        return ratings.stream().map(rating -> modelMapper.map(rating, RatingDTO.class)).toList();
    }

    @Override
    public List<RatingDTO> findAllRatingsByHotel(String hotelId) {
        List<Rating> ratings = ratingRepository.findAllByHotel(hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException(HOTEL, ID, hotelId)));
        return ratings.stream().map(rating -> modelMapper.map(rating, RatingDTO.class)).toList();
    }

    @Override
    public void deleteAllRatingsByUser(String userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(USER, ID, userId));
        List<Rating> allByUser = ratingRepository.findAllByUser(user);
        ratingRepository.deleteAll(allByUser);
    }

    @Override
    public void deleteAllRatingsByHotel(String hotelId) {
        List<Rating> ratings = ratingRepository.findAllByHotel(hotelRepository.findById(hotelId).orElseThrow(() -> new ResourceNotFoundException(HOTEL, ID, hotelId)));
        ratingRepository.deleteAll(ratings);
    }

    @Override
    public void deleteAllRatings() {
        ratingRepository.deleteAll();
    }
}
