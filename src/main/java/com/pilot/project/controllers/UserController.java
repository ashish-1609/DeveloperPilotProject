package com.pilot.project.controllers;

import com.pilot.project.payloads.ApiResponse;
import com.pilot.project.payloads.UserDTO;
import com.pilot.project.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Perform all the operation for adding, fetching, deleting and modifying the Details of users.")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Add User", description = "Add user in the system.")
    @PostMapping("/")
    public ResponseEntity<ApiResponse> saveUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if(Boolean.TRUE.equals(userService.isUserExistByEmail(userDTO.getEmail()))) {
            return new ResponseEntity<>(new ApiResponse("User Already Exist."), HttpStatus.BAD_REQUEST);
        }
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new ApiResponse(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()), HttpStatus.BAD_REQUEST);
        }
        userService.saveUser(userDTO);
        return new ResponseEntity<>(new ApiResponse("User Added Successfully"), HttpStatus.CREATED);
    }

    @Operation(summary = "Update User By Email", description = "Update the details of a User using it's Email.")
    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable String email,@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(
                    new ApiResponse(
                            Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()),
                            HttpStatus.BAD_REQUEST);
        }
        userService.updateUser(email, userDTO);
        return new ResponseEntity<>(new ApiResponse("User Updated Successfully"), HttpStatus.OK);
    }

    @Operation(summary = "Get All Users", description = "Fetch details of all the Users.")
    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Get the User by Email", description = "Fetch details of the User by it's Email.")
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        if(Boolean.TRUE.equals(userService.isUserExistByEmail(email))) {
            return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete User By Id", description = "Delete the details of a User using it's ID.")
    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable String email) {
            userService.deleteUser(email);
            return new ResponseEntity<>(new ApiResponse("User deleted Successfully."), HttpStatus.OK);
    }

}
