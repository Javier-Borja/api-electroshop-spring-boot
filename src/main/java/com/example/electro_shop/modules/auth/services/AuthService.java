package com.example.electro_shop.modules.auth.services;

import com.example.electro_shop.config.JwtService;
import com.example.electro_shop.modules.auth.dtos.LoginRequest;
import com.example.electro_shop.modules.auth.dtos.LoginResponse;
import com.example.electro_shop.modules.auth.dtos.RegisterRequest;
import com.example.electro_shop.modules.auth.dtos.RegisterResponse;
import com.example.electro_shop.modules.auth.mappers.AuthMapper;
import com.example.electro_shop.modules.user.model.User;
import com.example.electro_shop.modules.user.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, AuthMapper authMapper) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.authMapper = authMapper;
    }


    public RegisterResponse userRegister(RegisterRequest request) {
        if (userService.existsEmailUser(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso.");
        }

        User user = AuthMapper.INSTANCE.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saveUser = userService.saveUser(user);

        String token = jwtService.generateToken(saveUser);

        RegisterResponse response = AuthMapper.INSTANCE.toRegisterResponse(saveUser);
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

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        String token = jwtService.generateToken(user);

        LoginResponse response = AuthMapper.INSTANCE.toLoginResponse(user);
        response.setToken(token);

        return response;
    }

    public LoginResponse checkStatus(User user) {
        String newToken = jwtService.generateToken(user);
        LoginResponse response = authMapper.toLoginResponse(user);
        response.setToken(newToken);

        return response;
    }


}
