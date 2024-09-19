package com.pilot.project.services.impl;

import com.pilot.project.entities.Hotel;
import com.pilot.project.entities.Rating;
import com.pilot.project.entities.User;
import com.pilot.project.payloads.HotelDTO;
import com.pilot.project.payloads.RatingDTO;
import com.pilot.project.payloads.UserDTO;
import com.pilot.project.repositories.HotelRepository;
import com.pilot.project.repositories.RatingRepository;
import com.pilot.project.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.lang.management.GarbageCollectorMXBean;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    private Rating rating;
    private RatingDTO ratingDTO;
    private User user;
    private UserDTO userDTO;
    private Hotel hotel;
    private HotelDTO hotelDTO;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private ModelMapper modelMapper;



    @BeforeEach
    public void setUp() {
        userDTO = UserDTO.builder()
                .userId(UUID.randomUUID().toString())
                .name("test user").password("test")
                .email("test@test.com")
                .about("test about")
                .build();
        user=new User(userDTO.getUserId(),userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(),userDTO.getAbout(),null, null);
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


        ratingDTO=RatingDTO.builder()
                .ratingId(UUID.randomUUID().toString())
                .rating(10)
                .user(userDTO)
                .hotel(hotelDTO)
                .build();

        rating=new Rating();
        rating.setRatingId(UUID.randomUUID().toString());
        rating.setRating(10);
        rating.setUser(user);
        rating.setHotel(hotel);

    }

    @AfterEach
    void tearDown() throws Throwable {
        System.gc();
    }

    @Test
    void saveRating() {
        when(this.userRepository.findById(user.getUserId())).thenReturn(Optional.ofNullable(user));
//        when(this.modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        when(this.hotelRepository.findById(hotel.getHotelId())).thenReturn(Optional.of(hotel));
//        when(this.modelMapper.map(hotel, HotelDTO.class)).thenReturn(hotelDTO);
        when(modelMapper.map(rating,RatingDTO.class)).thenReturn(ratingDTO);
        when(this.ratingRepository.save(rating)).thenReturn(rating);
        when(this.modelMapper.map(ratingDTO,Rating.class)).thenReturn(rating);

        RatingDTO ratingDTO1 = this.ratingService.saveRating(user.getUserId(), hotel.getHotelId(), ratingDTO);
        assertNotNull(ratingDTO1);
        assertEquals(ratingDTO.getRatingId(),ratingDTO1.getRatingId());
        assertEquals(ratingDTO.getRating(),ratingDTO1.getRating());
        verify(ratingRepository, times(1)).save(rating);

    }

    @Test
    void updateRating() {
        when(this.ratingRepository.findById(ratingDTO.getRatingId())).thenReturn(Optional.of(rating));
        when(this.userRepository.findById(user.getUserId())).thenReturn(Optional.ofNullable(user));
        when(this.hotelRepository.findById(hotel.getHotelId())).thenReturn(Optional.of(hotel));
        when(this.ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(modelMapper.map(rating,RatingDTO.class)).thenReturn(ratingDTO);

        RatingDTO savedRating = this.ratingService.updateRating(user.getUserId(), hotel.getHotelId(),ratingDTO.getRatingId(), ratingDTO);
        assertNotNull(savedRating);
        assertEquals(ratingDTO.getRatingId(),savedRating.getRatingId());
        assertEquals(ratingDTO.getRating(),savedRating.getRating());
        verify(ratingRepository, times(1)).save(rating);

    }

    @Test
    void deleteRating() {
        when(this.ratingRepository.findById(ratingDTO.getRatingId())).thenReturn(Optional.of(rating));
        assertDoesNotThrow(()-> this.ratingService.deleteRating(ratingDTO.getRatingId()));
        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    void getRatings() {
        List<Rating> ratings = List.of(rating);
        when(this.ratingRepository.findAll()).thenReturn(ratings);
        List<RatingDTO> ratingDTOList = this.ratingService.getRatings();
        assertNotNull(ratingDTOList);
        assertEquals(ratingDTOList.size(),ratings.size());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    void getRatingById() {
        when(this.ratingRepository.findById(ratingDTO.getRatingId())).thenReturn(Optional.of(rating));
        when(this.modelMapper.map(rating,RatingDTO.class)).thenReturn(ratingDTO);
        RatingDTO ratingById = this.ratingService.getRatingById(ratingDTO.getRatingId());
        assertNotNull(ratingById);
        assertEquals(ratingDTO.getRatingId(), ratingById.getRatingId());
        verify(ratingRepository, times(1)).findById(ratingDTO.getRatingId());
    }

    @Test
    void findAllRatingsByUser() {
         List<Rating> ratings = List.of(rating);
         when(this.userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
         when(this.ratingRepository.findAllByUser(user)).thenReturn(ratings);
        List<RatingDTO> allRatingsByUser = this.ratingService.findAllRatingsByUser(userDTO.getUserId());
        assertNotNull(allRatingsByUser);
        assertEquals(allRatingsByUser.size(),ratings.size());
        verify(ratingRepository, times(1)).findAllByUser(user);
    }

    @Test
    void findAllRatingsByHotel() {
        List<Rating> ratings = List.of(rating);
        when(this.hotelRepository.findById(hotel.getHotelId())).thenReturn(Optional.of(hotel));
        when(this.ratingRepository.findAllByHotel(hotel)).thenReturn(ratings);
        List<RatingDTO> allRatingsByHotel = this.ratingService.findAllRatingsByHotel(hotelDTO.getHotelId());
        assertNotNull(allRatingsByHotel);
        assertEquals(allRatingsByHotel.size(),ratings.size());
        verify(ratingRepository, times(1)).findAllByHotel(hotel);
    }

    @Test
    void deleteAllRatingsByUser() {
        List<Rating> allByUser = this.ratingRepository.findAllByUser(user);
        when(this.userRepository.findById(userDTO.getUserId())).thenReturn(Optional.of(user));
        when(this.ratingRepository.findAllByUser(user)).thenReturn(allByUser);
        assertDoesNotThrow(()-> this.ratingService.deleteAllRatingsByUser(userDTO.getUserId()));
        verify(ratingRepository, times(1)).deleteAll(allByUser);
    }

    @Test
    void deleteAllRatingsByHotel() {
        List<Rating> allByHotel = this.ratingRepository.findAllByHotel(hotel);
        when(this.hotelRepository.findById(hotelDTO.getHotelId())).thenReturn(Optional.of(hotel));
        when(this.ratingRepository.findAllByHotel(hotel)).thenReturn(allByHotel);
        assertDoesNotThrow(()->this.ratingService.deleteAllRatingsByHotel(hotelDTO.getHotelId()));
        verify(ratingRepository, times(1)).deleteAll(allByHotel);
    }

    @Test
    void deleteAllRatings() {
        assertDoesNotThrow(()->this.ratingService.deleteAllRatings());
        verify(ratingRepository, times(1)).deleteAll();
    }
}