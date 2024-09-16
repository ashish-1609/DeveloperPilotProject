package com.pilot.project.services;

import com.pilot.project.payloads.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);
    UserDTO updateUser(String id, UserDTO userDTO);
    UserDTO getUserById(String id);
    List<UserDTO> getAllUsers();
    void deleteUser(String id);
    Boolean isUserExistByEmail(String email);
    Boolean isUserExistByUserid(String id);
}
