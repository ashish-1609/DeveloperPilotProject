package com.pilot.project.utils;

import com.pilot.project.entities.Hotel;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class HotelItemProcessor implements ItemProcessor<Hotel, Hotel> {
    @Override
    public Hotel process(Hotel item) throws Exception {
        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID().toString());
        hotel.setName(item.getName());
        hotel.setAddress(item.getAddress());
        hotel.setCity(item.getCity());
        return hotel;
    }
}
