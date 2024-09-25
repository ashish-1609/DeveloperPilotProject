package com.pilot.project.utils;

import com.pilot.project.entities.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelItemProcessorTest {

    @InjectMocks
    HotelItemProcessor hotelItemProcessor;

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setHotelName("Hotel Name");
        hotel.setHotelAddress("Hotel Address");
        hotel.setHotelCity("Hotel City");
    }

    @Test
    void process() throws Exception {
        Hotel result = this.hotelItemProcessor.process(hotel);
        assert result != null;
        assertEquals(hotel.getHotelName(), result.getHotelName());
        assertEquals(hotel.getHotelAddress(), result.getHotelAddress());
        assertEquals(hotel.getHotelCity(), result.getHotelCity());
        assertNotNull(result);

    }
}