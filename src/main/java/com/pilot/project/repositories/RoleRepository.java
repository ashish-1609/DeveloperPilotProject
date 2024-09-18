package com.pilot.project.repositories;

import com.pilot.project.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface RoleRepository extends JpaRepository<Role, Integer> {

}
