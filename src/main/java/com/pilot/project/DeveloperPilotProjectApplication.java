package com.pilot.project;

import com.pilot.project.entities.Role;
import com.pilot.project.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeveloperPilotProjectApplication implements CommandLineRunner {

    RoleRepository roleRepository;
    @Autowired
    public DeveloperPilotProjectApplication(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public void addRole(){
        roleRepository.save(new Role(1, "ROLE_USER"));
        roleRepository.save(new Role(2, "ROLE_ADMIN"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DeveloperPilotProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        addRole();
    }
}