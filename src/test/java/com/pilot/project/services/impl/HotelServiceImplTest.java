package com.pilot.project.services.impl;

import com.pilot.project.entities.Hotel;
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
                .hotelId(UUID.randomUUID().toString())
                .hotelName("Test Hotel")
                .hotelAddress("Test Address")
                .hotelCity("Test City")
                .build();

        hotel=new Hotel();
        hotel.setHotelId(hotelDTO.getHotelId());
        hotel.setHotelName(hotelDTO.getHotelName());
        hotel.setHotelAddress(hotelDTO.getHotelAddress());
        hotel.setHotelCity(hotelDTO.getHotelCity());

        hotelList=new ArrayList<>();

    }

    @Test
    @Order(1)
    void saveHotel() {
        when(this.modelMapper.map(hotelDTO, Hotel.class)).thenReturn(hotel);
        when(this.hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(this.modelMapper.map(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO savedHotel = this.hotelService.saveHotel(hotelDTO);

        assertNotNull(savedHotel.getHotelId());
        assertEquals(hotelDTO.getHotelId(), savedHotel.getHotelId());
        assertEquals(hotelDTO.getHotelName(), savedHotel.getHotelName());

        verify(this.hotelRepository, times(1)).save(hotel);
    }

    @Test
    @Order(2)
    void updateHotel() {
        when(this.hotelRepository.findById(hotelDTO.getHotelId())).thenReturn(Optional.of(hotel));
        when(this.hotelRepository.save(any(Hotel.class))).thenReturn(hotel);
        when(this.modelMapper.map(hotel, HotelDTO.class)).thenReturn(hotelDTO);

        HotelDTO updatedHotel = this.hotelService.updateHotel(hotelDTO.getHotelId(), hotelDTO);
        assertNotNull(updatedHotel);
        assertEquals(hotelDTO.getHotelId(), updatedHotel.getHotelId());
        assertEquals(hotelDTO.getHotelName(), updatedHotel.getHotelName());
        verify(this.hotelRepository, times(1)).save(hotel);
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
        when(hotelRepository.findById(hotelDTO.getHotelId())).thenReturn(Optional.of(hotel));
        when(modelMapper.map(hotel, HotelDTO.class)).thenReturn(hotelDTO);
        HotelDTO hotelById = hotelService.getHotelById(hotelDTO.getHotelId());
        assertNotNull(hotelById);
        assertEquals(hotelDTO.getHotelId(), hotelById.getHotelId());
        assertEquals(hotelDTO.getHotelName(), hotelById.getHotelName());
        verify(this.hotelRepository, times(1)).findById(hotelDTO.getHotelId());
    }

    @Test
    @Order(5)
    void deleteHotel() {
        when(hotelRepository.findById(hotelDTO.getHotelId())).thenReturn(Optional.of(hotel));
        assertDoesNotThrow(() -> hotelService.deleteHotel(hotelDTO.getHotelId()));
        verify(hotelRepository, times(1)).delete(hotel);
    }

    @Test
    @Order(6)
    void existByName() {
        when(this.hotelRepository.existsByHotelName(hotelDTO.getHotelName())).thenReturn(Boolean.TRUE);
        assertTrue(this.hotelRepository.existsByHotelName(hotelDTO.getHotelName()));
        verify(this.hotelRepository, times(1)).existsByHotelName(hotelDTO.getHotelName());
    }
}