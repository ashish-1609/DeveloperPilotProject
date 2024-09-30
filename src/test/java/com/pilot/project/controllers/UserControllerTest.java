package com.pilot.project.controllers;

import com.pilot.project.payloads.ApiResponse;
import com.pilot.project.payloads.UserDTO;
import com.pilot.project.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    @Mock
    private BindingResult bindingResult;

    private UserDTO userDTO;


    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .id(UUID.randomUUID().toString())
                .name("test")
                .email("test@email.com")
                .password("password")
                .about("Test About")
                .build();
    }

    @AfterEach
    void tearDown() {
        System.gc();
    }

    @Test
    void saveUser_InvalidUser(){
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(new FieldError("user", "email", "Invalid email"));
        ResponseEntity<?> responseEntity = userController.saveUser(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid email", ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }

    @Test
    void saveUser_UserExist(){
        userDTO.setEmail("test@email.com");
        when(userService.isUserExistByEmail(userDTO.getEmail())).thenReturn(true);

        ResponseEntity<?> responseEntity = userController.saveUser(userDTO);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User Already Exist.", ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
        verify(userService, times(1)).isUserExistByEmail(userDTO.getEmail());
    }

    @Test
    void saveUser_validUser() {
        when(bindingResult.hasErrors()).thenReturn(false);
        ApiResponse apiResponse = new ApiResponse("User Added Successfully");
        when(userService.isUserExistByEmail(userDTO.getEmail())).thenReturn(false);
        when(userService.saveUser(userDTO)).thenReturn(userDTO);

        ResponseEntity<?> responseEntity= userController.saveUser(userDTO);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(apiResponse, responseEntity.getBody());

    }

    @Test
    void updateUser() {
        ApiResponse apiResponse = new ApiResponse("User Updated Successfully");
        when(userService.updateUser(userDTO.getId(), userDTO)).thenReturn(userDTO);
        ResponseEntity<?> responseEntity = userController.updateUser(userDTO.getId(), userDTO);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(apiResponse, responseEntity.getBody());
    }
    @Test
    void updateUser_InvalidUser(){
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(new FieldError("user", "name", "User's name cannot be empty."));
        ResponseEntity<?> responseEntity = userController.updateUser(userDTO.getId(), userDTO);
        assertEquals("User's name cannot be empty.", ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }

    @Test
    void getAllUsers() {
        List<UserDTO> userDTOs = List.of(userDTO);
        when(userService.getAllUsers()).thenReturn(userDTOs);
        ResponseEntity<?> responseEntity = userController.getAllUsers();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDTOs, responseEntity.getBody());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById() {
        lenient().when(userService.getUserByEmail(userDTO.getId())).thenReturn(userDTO);
        ResponseEntity<UserDTO> responseEntity = userController.getUserByEmail(userDTO.getId());
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            assertEquals(userDTO, responseEntity.getBody());
        }else{
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }
    }
    @Test
    void getUserById_UserNotFound(){
        lenient().when(userService.isUserExistByEmail(userDTO.getEmail())).thenReturn(false);
        ResponseEntity<UserDTO> responseEntity = userController.getUserByEmail(userDTO.getId());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void deleteUser() {
        ResponseEntity<?> responseEntity = userController.deleteUser(userDTO.getId());
        verify(userService, times(1)).deleteUser(userDTO.getId());
        assertEquals("User deleted Successfully.", ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
    }
}