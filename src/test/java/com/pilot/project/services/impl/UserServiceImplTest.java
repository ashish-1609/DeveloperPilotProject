package com.pilot.project.services.impl;

import com.pilot.project.entities.User;
import com.pilot.project.exceptions.ResourceNotFoundException;
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
        userDTO = UserDTO.builder().id(UUID.randomUUID().toString()).name("test user").password("test").email("test@test.com").about("test about").build();

        user = new User();
        user.setId(userDTO.getId());
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
        UserDTO savedUser = userService.saveUser(userDTO);
        assertNotNull(savedUser);
        assertEquals(userDTO.getId(), savedUser.getId());
        assertEquals(userDTO.getName(), savedUser.getName());
        assertEquals(userDTO.getEmail(), savedUser.getEmail());
        assertEquals(userDTO.getAbout(), savedUser.getAbout());
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    @Order(2)
    void updateUser() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.encode(userDTO.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        UserDTO updatedUser = userService.updateUser(userDTO.getEmail(), userDTO);
        assertNotNull(userDTO);
        assertEquals(userDTO.getEmail(), updatedUser.getEmail());
        verify(userRepository).save(any(User.class));

    }

    @Test
    void updateUser_UserNotFound(){
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, this::updateUserForTest);
    }

    private void updateUserForTest() {
        userService.updateUser(userDTO.getEmail(), userDTO);
    }

    @Test
    @Order(3)
    void getUserById() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);
        UserDTO userById = userService.getUserByEmail(userDTO.getEmail());
        assertNotNull(userById);
        assertEquals(userDTO.getEmail(), userById.getEmail());
        verify(userRepository, times(1)).findByEmail(userDTO.getEmail());
    }

    @Test
    @Order(4)
    void getAllUsers() {
        userList.add(user);
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDTO> allUsers = userService.getAllUsers();
        assertNotNull(allUsers);
        assertEquals(userList.size(), allUsers.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @Order(5)
    void deleteUser() {
        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.deleteUser(userDTO.getEmail()));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @Order(6)
    void isUserExistByEmail() {
        when(userRepository.existsByEmail(userDTO.getEmail())).thenReturn(Boolean.TRUE);
        assertTrue(userService.isUserExistByEmail(userDTO.getEmail()));
        verify(userRepository, times(1)).existsByEmail(userDTO.getEmail());
    }

    @Test
    void isUserExistByUserid(){
        when(userRepository.existsById(userDTO.getId())).thenReturn(Boolean.TRUE);
        assertTrue(userService.isUserExistByUserid(userDTO.getId()));
    }

}