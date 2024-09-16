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

import java.util.List;
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
            return new ResponseEntity<>(new ApiResponse("User Already Exist.", false), HttpStatus.BAD_REQUEST);
        }
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ApiResponse(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),false), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(this.userService.saveUser(userDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id,@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()){
            return new ResponseEntity<>(
                    new ApiResponse(
                            Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),false),
                            HttpStatus.BAD_REQUEST);
        }
        if(this.userService.isUserExistByUserid(id)) {
            return new ResponseEntity<>(this.userService.updateUser(id, userDTO), HttpStatus.OK);
        }
        return new ResponseEntity<>(
                new ApiResponse("User Not Exist.", false), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        if(this.userService.isUserExistByUserid(id)) {
            return new ResponseEntity<>(this.userService.getUserById(id), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
            this.userService.deleteUser(id);
            return new ResponseEntity<>(new ApiResponse("User deleted Successfully.", true), HttpStatus.OK);
    }

}
