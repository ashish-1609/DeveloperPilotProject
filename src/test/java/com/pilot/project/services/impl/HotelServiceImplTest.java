package com.pilot.project.services.impl;

import com.pilot.project.entities.Hotel;
import com.pilot.project.exceptions.ResourceNotFoundException;
import com.pilot.project.payloads.HotelDTO;
import com.pilot.project.repositories.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Rollback(value = false)
class HotelServiceImplTest {

    private Hotel hotel;
    private HotelDTO hotelDTO;

    private List<Hotel> hotelList;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        hotelDTO= HotelDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Hotel")
                .address("Test Address")
                .city("Test City")
                .build();

        hotel=new Hotel();
        hotel.setId(hotelDTO.getId());
        hotel.setName(hotelDTO.getName());
        hotel.setAddress(hotelDTO.getAddress());
        hotel.setCity(hotelDTO.getCity());

        hotelList=new ArrayList<>();

    }

    @Test
    @Order(1)
    void saveHotel() {
        when(this.modelMapper.map(hotelDTO, Hotel.class)).thenReturn(hotel);
        when(this.hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(this.modelMapper.map(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO savedHotel = this.hotelService.saveHotel(hotelDTO);

        assertNotNull(savedHotel.getId());
        assertEquals(hotelDTO.getId(), savedHotel.getId());
        assertEquals(hotelDTO.getName(), savedHotel.getName());

        verify(this.hotelRepository, times(1)).save(hotel);
    }

    @Test
    @Order(2)
    void updateHotel() {
        when(this.hotelRepository.findById(hotelDTO.getId())).thenReturn(Optional.of(hotel));
        when(this.hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(this.modelMapper.map(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO updatedHotel = this.hotelService.updateHotel(hotelDTO.getId(), hotelDTO);
        assertNotNull(updatedHotel);
        assertEquals(hotelDTO.getId(), updatedHotel.getId());
        assertEquals(hotelDTO.getName(), updatedHotel.getName());
        verify(this.hotelRepository, times(1)).save(hotel);
    }
    @Test
    void updateHotel_HotelNotFound() {
        when(this.hotelRepository.findById(hotelDTO.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, this::updateHotelById);
    }

    private void updateHotelById(){
        this.hotelService.updateHotel(hotelDTO.getId(), hotelDTO);
    }

    @Test
    @Order(3)
    void getHotels() {
        hotelList.add(hotel);
        when(this.hotelRepository.findAll()).thenReturn(hotelList);
        List<HotelDTO> hotels = this.hotelService.getHotels();
        assertNotNull(hotels);
        assertEquals(hotelList.size(), hotels.size());
        verify(this.hotelRepository, times(1)).findAll();
    }

    @Test
    @Order(4)
    void getHotelById() {
        when(hotelRepository.findById(hotelDTO.getId())).thenReturn(Optional.of(hotel));
        when(modelMapper.map(hotel, HotelDTO.class)).thenReturn(hotelDTO);
        HotelDTO hotelById = hotelService.getHotelById(hotelDTO.getId());
        assertNotNull(hotelById);
        assertEquals(hotelDTO.getId(), hotelById.getId());
        assertEquals(hotelDTO.getName(), hotelById.getName());
        verify(this.hotelRepository, times(1)).findById(hotelDTO.getId());
    }
    @Test
    void getHotelById_HotelNotFound() {
        when(this.hotelRepository.findById(hotelDTO.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, this::getHotelByIdForTest);
    }

    private void getHotelByIdForTest(){
        this.hotelService.getHotelById(hotelDTO.getId());
    }

    @Test
    @Order(5)
    void deleteHotel() {
        when(hotelRepository.findById(hotelDTO.getId())).thenReturn(Optional.of(hotel));
        assertDoesNotThrow(() -> hotelService.deleteHotel(hotelDTO.getId()));
        verify(hotelRepository, times(1)).delete(hotel);
    }

    @Test
    void deleteHotel_HotelNotFound() {
        when(this.hotelRepository.findById(hotelDTO.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, this::deleteHotelById);
    }

    private void deleteHotelById(){
        this.hotelService.deleteHotel(hotelDTO.getId());
    }

    @Test
    @Order(6)
    void existByName() {
        when(this.hotelRepository.existsByName(hotelDTO.getName())).thenReturn(Boolean.TRUE);
        Boolean existByName= this.hotelService.existByName(hotelDTO.getName());
        assertNotNull(existByName);
        assertEquals(Boolean.TRUE, existByName);
        verify(this.hotelRepository, times(1)).existsByName(hotelDTO.getName());
    }
}