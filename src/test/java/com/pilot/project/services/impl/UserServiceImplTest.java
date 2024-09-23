package com.pilot.project.services.impl;

import com.pilot.project.entities.User;
import com.pilot.project.payloads.UserDTO;
import com.pilot.project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Rollback(value = false)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private ModelMapper modelMapper ;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserDTO userDTO ;
    private User user;
    private List<User> userList;
    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder().userId(UUID.randomUUID().toString()).name("test user").password("test").email("test@test.com").about("test about").build();

        user = new User();
        user.setUserId(userDTO.getUserId());
        user.setName(userDTO.getName());
        user.setPassword("encoded");
        user.setEmail(userDTO.getEmail());
        user.setAbout(userDTO.getAbout());

        userList = new ArrayList<>();
    }

    @Test
    @Order(1)
    void saveUser() {
        when(bCryptPasswordEncoder.encode(any())).thenReturn("encoded");
        when(modelMapper.map(userDTO, User.class)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        UserDTO savedUser = this.userService.saveUser(userDTO);
        assertNotNull(savedUser);
        assertEquals(userDTO.getUserId(), savedUser.getUserId());
        assertEquals(userDTO.getName(), savedUser.getName());
        assertEquals(userDTO.getEmail(), savedUser.getEmail());
        assertEquals(userDTO.getAbout(), savedUser.getAbout());
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    @Order(2)
    void updateUser() {
        when(this.userRepository.findById(userDTO.getUserId())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.encode(userDTO.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        UserDTO updatedUser = this.userService.updateUser(userDTO.getUserId(), userDTO);
        assertNotNull(userDTO);
        assertEquals(userDTO.getUserId(), updatedUser.getUserId());
        verify(this.userRepository).save(any(User.class));

    }

    @Test
    @Order(3)
    void getUserById() {
        when(this.userRepository.findById(userDTO.getUserId())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        UserDTO userById = this.userService.getUserById(userDTO.getUserId());
        assertNotNull(userById);
        assertEquals(userDTO.getUserId(), userById.getUserId());
        verify(this.userRepository, times(1)).findById(userDTO.getUserId());
    }

    @Test
    @Order(4)
    void getAllUsers() {
        userList.add(user);
        when(this.userRepository.findAll()).thenReturn(List.of(user));
        List<UserDTO> allUsers = this.userService.getAllUsers();
        assertNotNull(allUsers);
        assertEquals(userList.size(), allUsers.size());
        verify(this.userRepository, times(1)).findAll();
    }

    @Test
    @Order(5)
    void deleteUser() {
        when(this.userRepository.findById(userDTO.getUserId())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> this.userService.deleteUser(userDTO.getUserId()));
        verify(this.userRepository, times(1)).delete(user);
    }

    @Test
    @Order(6)
    void isUserExistByEmail() {
        when(this.userRepository.existsByEmail(userDTO.getEmail())).thenReturn(Boolean.TRUE);
        assertTrue(this.userService.isUserExistByEmail(userDTO.getEmail()));
        verify(this.userRepository, times(1)).existsByEmail(userDTO.getEmail());
    }

    @Test
    void isUserExistByUserid(){
        when(this.userRepository.existsById(userDTO.getUserId())).thenReturn(Boolean.TRUE);
        assertTrue(this.userService.isUserExistByUserid(userDTO.getUserId()));
    }

}