package com.pilot.project.services;

import com.pilot.project.payloads.HotelDTO;

import java.util.List;

public interface HotelService {
    HotelDTO saveHotel(HotelDTO hotelDTO);
    HotelDTO updateHotel(String id, HotelDTO hotelDTO);
    List<HotelDTO> getHotels();
    HotelDTO getHotelById(String hotelId);
    void deleteHotel(String hotelId);
    Boolean existByName(String hotelName);
    Boolean existById(String hotelId);
}
