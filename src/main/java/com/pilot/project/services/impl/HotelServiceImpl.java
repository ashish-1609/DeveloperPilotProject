package com.pilot.project.services.impl;

import com.pilot.project.payloads.HotelDTO;
import com.pilot.project.repositories.HotelRepository;
import com.pilot.project.services.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    private HotelRepository hotelRepository;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public HotelDTO saveHotel(HotelDTO hotelDTO) {
        return null;
    }

    @Override
    public HotelDTO updateHotel(HotelDTO hotelDTO) {
        return null;
    }

    @Override
    public List<HotelDTO> getHotels() {
        return List.of();
    }

    @Override
    public HotelDTO getHotelById(String hotelId) {
        return null;
    }

    @Override
    public void deleteHotel(String hotelId) {

    }
}
