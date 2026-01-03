package com.example.electro_shop.modules.user.service;

import com.example.electro_shop.modules.user.dto.UserProfileDto;
import com.example.electro_shop.modules.user.mapper.UserMapper;
import com.example.electro_shop.modules.user.model.User;
import com.example.electro_shop.modules.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public Boolean existsEmailUser(String email) {
        return userRepository.existsByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserProfileDto getUserProfile(User user) {
        return userMapper.toUserProfileDto(user);
    }
}
