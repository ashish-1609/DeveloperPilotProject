package com.pilot.project.services.impl;

import com.pilot.project.entities.Hotel;
import com.pilot.project.exceptions.ResourceNotFoundException;
import com.pilot.project.payloads.HotelDTO;
import com.pilot.project.repositories.HotelRepository;
import com.pilot.project.services.HotelService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HotelServiceImpl implements HotelService {

    private static final String HOTEL = "Hotel";
    private static final String ID = "ID";

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository, ModelMapper modelMapper) {
        this.hotelRepository = hotelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public HotelDTO saveHotel(HotelDTO hotelDTO) {
        hotelDTO.setId(UUID.randomUUID().toString());
        Hotel hotel = hotelRepository.save(modelMapper.map(hotelDTO, Hotel.class));
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO updateHotel(String id, HotelDTO hotelDTO) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(
                    () -> new ResourceNotFoundException(HOTEL, ID, id));
        hotel.setName(hotelDTO.getName());
        hotel.setAddress(hotelDTO.getAddress());
        hotel.setCity(hotelDTO.getCity());
        return modelMapper.map(hotelRepository.save(hotel), HotelDTO.class);
    }

    @Override
    public List<HotelDTO> getHotels() {
        return hotelRepository.findAll()
                .stream().map(hotel -> modelMapper.map(hotel, HotelDTO.class))
                .toList();
    }

    @Override
    public HotelDTO getHotelById(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(HOTEL, ID,hotelId));
        return modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public void deleteHotel(String hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(HOTEL, ID,hotelId));
        hotelRepository.delete(hotel);
    }

    @Override
    public Boolean existByName(String hotelName) {
        return hotelRepository.existsByName(hotelName);
    }

}
