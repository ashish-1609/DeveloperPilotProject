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
        User user = this.userRepository.save(
                this.modelMapper.map(userDTO, User.class)
        );
        return this.modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        User user = this.userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User", "id",id )
                );
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setAbout(userDTO.getAbout());
        return this.modelMapper.map(
                this.userRepository.save(user), UserDTO.class
        );
    }

    @Override
    public UserDTO getUserById(String id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return this.modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        return users.stream().map(user-> this.modelMapper.map(user, UserDTO.class)).toList();
    }

    @Override
    public void deleteUser(String id) {
        User user = this.userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        this.userRepository.delete(user);
    }

    @Override
    public Boolean isUserExistByEmail(String email) {
            return this.userRepository.existsByEmail(email);
    }

    @Override
    public Boolean isUserExistByUserid(String id) {
        return this.userRepository.existsById(id);
    }
}
