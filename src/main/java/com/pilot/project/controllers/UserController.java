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
@CrossOrigin
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Perform all the operation for adding, fetching, deleting and modifying the Details of users.")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @Operation(summary = "Add User", description = "Add user in the system.")
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

    @Operation(summary = "Update User By Id", description = "Update the details of a User using it's ID.")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id,@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()){
            return new ResponseEntity<>(
                    new ApiResponse(
                            Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(),false),
                            HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(this.userService.updateUser(id, userDTO), HttpStatus.OK);
    }

    @Operation(summary = "Get All Users", description = "Fetch details of all the Users.")
    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(this.userService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Get the User by Id", description = "Fetch details of the User by it's ID.")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        if(this.userService.isUserExistByUserid(id)) {
            return new ResponseEntity<>(this.userService.getUserById(id), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete User By Id", description = "Delete the details of a User using it's ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
            this.userService.deleteUser(id);
            return new ResponseEntity<>(new ApiResponse("User deleted Successfully.", true), HttpStatus.OK);
    }

}
