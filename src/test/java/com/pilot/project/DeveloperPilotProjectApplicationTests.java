package com.pilot.project;

import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DeveloperPilotProjectApplicationTests {


    @Test
    void contextLoads() {
    }

    @Test
    void main() {
        assertThat(SpringApplication.run(DeveloperPilotProjectApplication.class)).isNotNull();
    }

}
