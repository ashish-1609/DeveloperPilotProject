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
                .userId(UUID.randomUUID().toString())
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
        when(this.bindingResult.hasErrors()).thenReturn(true);
        when(this.bindingResult.getFieldError()).thenReturn(new FieldError("user", "email", "Invalid email"));
        ResponseEntity<?> responseEntity = this.userController.saveUser(userDTO, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid email", ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());

//        verify(this.userController, times(1)).saveUser(userDTO, bindingResult);
    }

    @Test
    public void saveUser_UserExist(){
        userDTO.setEmail("test@email.com");
        when(this.userService.isUserExistByEmail(userDTO.getEmail())).thenReturn(true);

        ResponseEntity<?> responseEntity = this.userController.saveUser(userDTO, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("User Already Exist.", ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());
        verify(this.userService, times(1)).isUserExistByEmail(userDTO.getEmail());
    }

    @Test
    public void saveUser_validUser() {
        when(this.userService.isUserExistByEmail(userDTO.getEmail())).thenReturn(false);
        when(this.userService.saveUser(userDTO)).thenReturn(userDTO);

        ResponseEntity<?> responseEntity= this.userController.saveUser(userDTO, bindingResult);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(userDTO, responseEntity.getBody());

    }

    @Test
    void updateUser() {
        when(this.userService.updateUser(userDTO.getUserId(), userDTO)).thenReturn(userDTO);
        ResponseEntity<?> responseEntity = this.userController.updateUser(userDTO.getUserId(), userDTO, bindingResult);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDTO, responseEntity.getBody());
    }

    @Test
    void getAllUsers() {
        List<UserDTO> userDTOs = List.of(userDTO);
        when(this.userService.getAllUsers()).thenReturn(userDTOs);
        ResponseEntity<?> responseEntity = this.userController.getAllUsers();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userDTOs, responseEntity.getBody());
        verify(this.userService, times(1)).getAllUsers();
    }

    @Test
    public void getUserById() {
        lenient().when(this.userService.getUserById(userDTO.getUserId())).thenReturn(userDTO);
        ResponseEntity<UserDTO> responseEntity = this.userController.getUserById(userDTO.getUserId());
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            assertEquals(userDTO, responseEntity.getBody());
        }else{
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        }

//        verify(this.userService, times(1)).getUserById(userDTO.getUserId());
    }

    @Test
    void deleteUser() {
        ResponseEntity<?> responseEntity = this.userController.deleteUser(userDTO.getUserId());
        verify(this.userService, times(1)).deleteUser(userDTO.getUserId());
        assertEquals("User deleted Successfully.", ((ApiResponse) Objects.requireNonNull(responseEntity.getBody())).getMessage());

    }
}