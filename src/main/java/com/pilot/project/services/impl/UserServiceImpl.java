package com.pilot.project.services.impl;

import com.pilot.project.entities.User;
import com.pilot.project.exceptions.ResourceNotFoundException;
import com.pilot.project.payloads.UserDTO;
import com.pilot.project.repositories.UserRepository;
import com.pilot.project.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER = "User";
    private static final String ID = "ID";

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO saveUser(UserDTO userDTO) {
        userDTO.setUserId(UUID.randomUUID().toString());
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        User user = userRepository.save(
                modelMapper.map(userDTO, User.class)
        );
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(USER, ID, id)
                );
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setAbout(userDTO.getAbout());
        return modelMapper.map(
                userRepository.save(user), UserDTO.class
        );
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER, ID, id));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user-> modelMapper.map(user, UserDTO.class)).toList();
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(USER, ID, id));
        userRepository.delete(user);
    }

    @Override
    public Boolean isUserExistByEmail(String email) {
            return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean isUserExistByUserid(String id) {
        return userRepository.existsById(id);
    }
}
