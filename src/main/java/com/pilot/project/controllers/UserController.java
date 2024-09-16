package com.pilot.project.controllers;

import com.pilot.project.payloads.ApiResponse;
import com.pilot.project.payloads.UserDTO;
import com.pilot.project.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<?> saveUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if(this.userService.isUserExistByEmail(userDTO.getEmail())) {
            return new ResponseEntity<>(new ApiResponse("", false), HttpStatus.BAD_REQUEST);
        }
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ApiResponse(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),false), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(this.userService.saveUser(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id,@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
//        if(this.userService.isUserExistByUserid(id)) {
//            return new
//        }
        return null;
    }

}
