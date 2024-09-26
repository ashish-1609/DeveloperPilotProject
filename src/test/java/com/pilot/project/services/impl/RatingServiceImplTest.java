package com.pilot.project.services.impl;

import com.pilot.project.entities.Hotel;
import com.pilot.project.entities.Rating;
import com.pilot.project.entities.User;
import com.pilot.project.exceptions.ResourceNotFoundException;
import com.pilot.project.payloads.HotelDTO;
import com.pilot.project.payloads.RatingDTO;
import com.pilot.project.payloads.UserDTO;
import com.pilot.project.repositories.HotelRepository;
import com.pilot.project.repositories.RatingRepository;
import com.pilot.project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Rollback(value = false)
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
                .id(UUID.randomUUID().toString())
                .name("test user").password("test")
                .email("test@test.com")
                .about("test about")
                .build();
        user=new User(userDTO.getId(),userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(),userDTO.getAbout(),null, null);
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


        ratingDTO=RatingDTO.builder()
                .id(UUID.randomUUID().toString())
                .points(10)
                .user(userDTO)
                .hotel(hotelDTO)
                .build();

        rating=new Rating();
        rating.setId(UUID.randomUUID().toString());
        rating.setPoints(10);
        rating.setUser(user);
        rating.setHotel(hotel);

    }

    @Test
    @Order(1)
    void saveRating_WhenUserIsValid() {
        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        when(this.hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel));
        when(modelMapper.map(rating,RatingDTO.class)).thenReturn(ratingDTO);
        when(this.ratingRepository.save(rating)).thenReturn(rating);
        when(this.modelMapper.map(ratingDTO,Rating.class)).thenReturn(rating);

        RatingDTO ratingDTO1 = this.ratingService.saveRating(user.getEmail(), hotel.getId(), ratingDTO);
        assertNotNull(ratingDTO1);
        assertEquals(ratingDTO.getId(),ratingDTO1.getId());
        assertEquals(ratingDTO.getPoints(),ratingDTO1.getPoints());
        verify(ratingRepository, times(1)).save(rating);

    }

    @Test
    void saveUser_WhenUserNotFound(){
        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, this::saveRating);
    }
    private void saveRating() {
        ratingService.saveRating(user.getEmail(), hotel.getId(), ratingDTO);
    }
    @Test
    @Order(2)
    void updateRating_WhenUserFound() {
        when(this.ratingRepository.findById(ratingDTO.getId())).thenReturn(Optional.of(rating));
        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(Optional.ofNullable(user));
        when(this.hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel));
        when(this.ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(modelMapper.map(rating,RatingDTO.class)).thenReturn(ratingDTO);

        RatingDTO savedRating = this.ratingService.updateRating(user.getEmail(), hotel.getId(),ratingDTO.getId(), ratingDTO);
        assertNotNull(savedRating);
        assertEquals(ratingDTO.getId(),savedRating.getId());
        assertEquals(ratingDTO.getPoints(),savedRating.getPoints());
        verify(ratingRepository, times(1)).save(rating);

    }
    @Test
    void updateRating_WhenRatingNotFound(){
        when(this.ratingRepository.findById(ratingDTO.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, this::updateRating);
    }
    private void updateRating(){
        this.ratingService.updateRating(userDTO.getEmail(), hotelDTO.getId(), ratingDTO.getId(), ratingDTO);
    }
    @Test
    @Order(3)
    void deleteRating() {
        when(this.ratingRepository.findById(ratingDTO.getId())).thenReturn(Optional.of(rating));
        assertDoesNotThrow(()-> this.ratingService.deleteRating(ratingDTO.getId()));
        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    @Order(4)
    void getRatings() {
        List<Rating> ratings = List.of(rating);
        when(this.ratingRepository.findAll()).thenReturn(ratings);
        List<RatingDTO> ratingDTOList = this.ratingService.getRatings();
        assertNotNull(ratingDTOList);
        assertEquals(ratingDTOList.size(),ratings.size());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    @Order(5)
    void getRatingById() {
        when(this.ratingRepository.findById(ratingDTO.getId())).thenReturn(Optional.of(rating));
        when(this.modelMapper.map(rating,RatingDTO.class)).thenReturn(ratingDTO);
        RatingDTO ratingById = this.ratingService.getRatingById(ratingDTO.getId());
        assertNotNull(ratingById);
        assertEquals(ratingDTO.getId(), ratingById.getId());
        verify(ratingRepository, times(1)).findById(ratingDTO.getId());
    }
    @Test
    void getRatingById_WhenRatingNotFound(){
        when(this.ratingRepository.findById(ratingDTO.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, this::getRatingByIdForTest );
    }

    private void getRatingByIdForTest(){
        this.ratingService.getRatingById(ratingDTO.getId());
    }

    @Test
    @Order(6)
    void findAllRatingsByUser() {
         List<Rating> ratings = List.of(rating);
         when(this.userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
         when(this.ratingRepository.findAllByUser(user)).thenReturn(ratings);
        List<RatingDTO> allRatingsByUser = this.ratingService.findAllRatingsByUser(userDTO.getEmail());
        assertNotNull(allRatingsByUser);
        assertEquals(allRatingsByUser.size(),ratings.size());
        verify(ratingRepository, times(1)).findAllByUser(user);
    }
    @Test
    void findAllRatingsByUser_WhenUserNotFound(){
        when(this.userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,this::findAllRatingsByUserForTest);
    }
    private void findAllRatingsByUserForTest(){
        this.ratingService.findAllRatingsByUser(userDTO.getEmail());
    }

    @Test
    @Order(7)
    void findAllRatingsByHotel() {
        List<Rating> ratings = List.of(rating);
        when(this.hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel));
        when(this.ratingRepository.findAllByHotel(hotel)).thenReturn(ratings);
        List<RatingDTO> allRatingsByHotel = this.ratingService.findAllRatingsByHotel(hotelDTO.getId());
        assertNotNull(allRatingsByHotel);
        assertEquals(allRatingsByHotel.size(),ratings.size());
        verify(ratingRepository, times(1)).findAllByHotel(hotel);
    }
    @Test
    void findAllRatingsByHotel_WhenHotelNotFound(){
        when(this.hotelRepository.findById(hotel.getId())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,this::findAllRatingsByHotelForTest);
    }
    private void findAllRatingsByHotelForTest(){
        this.ratingService.findAllRatingsByHotel(hotelDTO.getId());
    }

    @Test
    @Order(8)
    void deleteAllRatingsByUser() {
        List<Rating> allByUser = this.ratingRepository.findAllByUser(user);
        when(this.userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        when(this.ratingRepository.findAllByUser(user)).thenReturn(allByUser);
        assertDoesNotThrow(()-> this.ratingService.deleteAllRatingsByUser(userDTO.getEmail()));
        verify(ratingRepository, times(1)).deleteAll(allByUser);
    }

    @Test
    @Order(9)
    void deleteAllRatingsByHotel() {
        List<Rating> allByHotel = this.ratingRepository.findAllByHotel(hotel);
        when(this.hotelRepository.findById(hotelDTO.getId())).thenReturn(Optional.of(hotel));
        when(this.ratingRepository.findAllByHotel(hotel)).thenReturn(allByHotel);
        assertDoesNotThrow(()->this.ratingService.deleteAllRatingsByHotel(hotelDTO.getId()));
        verify(ratingRepository, times(1)).deleteAll(allByHotel);
    }

    @Test
    @Order(10)
    void deleteAllRatings() {
        assertDoesNotThrow(()->this.ratingService.deleteAllRatings());
        verify(ratingRepository, times(1)).deleteAll();
    }
}