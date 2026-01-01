package com.example.electro_shop.modules.auth.services;

import com.example.electro_shop.config.JwtService;
import com.example.electro_shop.modules.auth.dtos.LoginRequest;
import com.example.electro_shop.modules.auth.dtos.LoginResponse;
import com.example.electro_shop.modules.auth.dtos.RegisterRequest;
import com.example.electro_shop.modules.auth.dtos.RegisterResponse;
import com.example.electro_shop.modules.auth.mappers.AuthMapper;
import com.example.electro_shop.modules.auth.models.User;
import com.example.electro_shop.modules.auth.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public RegisterResponse userRegister(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }

        User user = AuthMapper.INSTANCE.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(savedUser);

        RegisterResponse response = AuthMapper.INSTANCE.toRegisterResponse(savedUser);
        response.setToken(token);

        return response;
    }

    public LoginResponse userLogin(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado tras autenticación"));

        String token = jwtService.generateToken(user);

        LoginResponse response = AuthMapper.INSTANCE.toLoginResponse(user);
        response.setToken(token);

        return response;
    }
}
