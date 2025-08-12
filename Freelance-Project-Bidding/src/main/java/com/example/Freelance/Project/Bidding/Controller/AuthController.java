package com.example.Freelance.Project.Bidding.Controller;

import com.example.Freelance.Project.Bidding.Dto.AuthRequest;
import com.example.Freelance.Project.Bidding.Dto.AuthResponse;
import com.example.Freelance.Project.Bidding.Dto.RegisterRequest;
import com.example.Freelance.Project.Bidding.Controller.AuthService;
import com.example.Freelance.Project.Bidding.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}