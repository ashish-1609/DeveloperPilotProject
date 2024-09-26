package com.pilot.project.services;

import com.pilot.project.payloads.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO saveUser(UserDTO userDTO);
    UserDTO updateUser(String id, UserDTO userDTO);
    UserDTO getUserByEmail(String email);
    List<UserDTO> getAllUsers();
    void deleteUser(String email);
    Boolean isUserExistByEmail(String email);
    Boolean isUserExistByUserid(String id);
}
