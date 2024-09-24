package com.pilot.project.utils;

import com.pilot.project.entities.Hotel;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class HotelItemProcessor implements ItemProcessor<Hotel, Hotel> {
    @Override
    public Hotel process(Hotel item) throws Exception {
        Hotel hotel = new Hotel();
        hotel.setHotelId(UUID.randomUUID().toString());
        hotel.setHotelName(item.getHotelName());
        hotel.setHotelAddress(item.getHotelAddress());
        hotel.setHotelCity(item.getHotelCity());
        return hotel;
    }
}
