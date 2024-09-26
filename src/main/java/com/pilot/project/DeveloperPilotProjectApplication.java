package com.pilot.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class DeveloperPilotProjectApplication {

    private static Logger logger = LogManager.getLogger(DeveloperPilotProjectApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DeveloperPilotProjectApplication.class, args);
        logger.info(UUID.randomUUID().toString());
    }

}