package com.example.electro_shop.modules.auth.controllers;

import com.example.electro_shop.modules.auth.dtos.LoginRequest;
import com.example.electro_shop.modules.auth.dtos.LoginResponse;
import com.example.electro_shop.modules.auth.dtos.RegisterRequest;
import com.example.electro_shop.modules.auth.dtos.RegisterResponse;
import com.example.electro_shop.modules.auth.services.AuthService;
import com.example.electro_shop.modules.user.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService userService, AuthenticationManager authenticationManager) {
        this.authService = userService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.userRegister(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.userLogin(request));
    }

    @GetMapping("/check-status")
    public ResponseEntity<LoginResponse> checkStatus(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(authService.checkStatus(user));
    }
}
