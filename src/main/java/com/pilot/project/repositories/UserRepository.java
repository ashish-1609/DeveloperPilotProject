package com.pilot.project.repositories;


import com.pilot.project.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByEmail(String email);
    Boolean existsByUserId(String userId);
}
