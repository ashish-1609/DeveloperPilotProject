package com.pilot.project.repositories;

import com.pilot.project.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, String> {
    Boolean existsByName(String name);
}
