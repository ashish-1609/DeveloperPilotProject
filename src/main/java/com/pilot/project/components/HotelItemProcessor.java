package com.pilot.project.components;

import com.pilot.project.entities.Hotel;
import org.springframework.batch.item.ItemProcessor;

import java.util.UUID;

public class HotelItemProcessor implements ItemProcessor<Hotel, Hotel> {
    @Override
    public Hotel process(Hotel item) throws Exception {
        return new Hotel(UUID.randomUUID().toString(), item.getName(), item.getCity(),item.getCity(),null);
    }
}
