package com.pilot.project.repositories;

import com.pilot.project.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
