package com.pilot.project.utils;

import com.pilot.project.components.HotelItemProcessor;
import com.pilot.project.entities.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HotelItemProcessorTest {

    @InjectMocks
    HotelItemProcessor hotelItemProcessor;

    private Hotel hotel;

    @BeforeEach
    void setUp() {
        hotel = new Hotel();
        hotel.setName("Hotel Name");
        hotel.setAddress("Hotel Address");
        hotel.setCity("Hotel City");
    }

    @Test
    void process() throws Exception {
        Hotel result = this.hotelItemProcessor.process(hotel);
        assert result != null;
        assertEquals(hotel.getName(), result.getName());
        assertEquals(hotel.getAddress(), result.getAddress());
        assertEquals(hotel.getCity(), result.getCity());
        assertNotNull(result);

    }
}