package com.pilot.project.repositories;

import com.pilot.project.entities.Hotel;
import com.pilot.project.entities.Rating;
import com.pilot.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {
    List<Rating> findAllByUser(User user);
    List<Rating> findAllByHotel(Hotel hotel);
}
