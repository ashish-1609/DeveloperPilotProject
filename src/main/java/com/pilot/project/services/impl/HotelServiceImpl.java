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
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public HotelServiceImpl(HotelRepository hotelRepository, ModelMapper modelMapper) {
        this.hotelRepository = hotelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public HotelDTO saveHotel(HotelDTO hotelDTO) {
        hotelDTO.setHotelId(UUID.randomUUID().toString());
        Hotel hotel = this.hotelRepository.save(this.modelMapper.map(hotelDTO, Hotel.class));
        return this.modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public HotelDTO updateHotel(String id, HotelDTO hotelDTO) {
        Hotel hotel = this.hotelRepository.findById(id)
                .orElseThrow(
                    () -> new ResourceNotFoundException("Hotel", "id", id));
        hotel.setHotelName(hotelDTO.getHotelName());
        hotel.setHotelAddress(hotelDTO.getHotelAddress());
        hotel.setHotelCity(hotelDTO.getHotelCity());
        return this.modelMapper.map(this.hotelRepository.save(hotel), HotelDTO.class);
    }

    @Override
    public List<HotelDTO> getHotels() {
        return this.hotelRepository.findAll()
                .stream().map(hotel -> this.modelMapper.map(hotel, HotelDTO.class))
                .toList();
    }

    @Override
    public HotelDTO getHotelById(String hotelId) {
        Hotel hotel = this.hotelRepository.findById(hotelId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Hotel", "id",hotelId));
        return this.modelMapper.map(hotel, HotelDTO.class);
    }

    @Override
    public void deleteHotel(String hotelId) {
        Hotel hotel = this.hotelRepository.findById(hotelId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Hotel", "id",hotelId));
        this.hotelRepository.delete(hotel);
    }

    @Override
    public Boolean existByName(String hotelName) {
        return this.hotelRepository.existsByHotelName(hotelName);
    }

}
