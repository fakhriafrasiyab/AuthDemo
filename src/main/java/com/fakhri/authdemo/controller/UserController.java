package com.fakhri.authdemo.controller;

import com.fakhri.authdemo.dto.ApiResponse;
import com.fakhri.authdemo.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public UserController(InMemoryUserDetailsManager userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse> publicPage() {
        return ResponseEntity.ok(new ApiResponse("Public endpoint – no authentication required."));
    }

    @GetMapping("/user/home")
    public ResponseEntity<ApiResponse> userHome(Authentication authentication) {
        return ResponseEntity.ok(new ApiResponse(
                "Hello," + authentication.getName() + ". You are successfully authenticated!"
        ));
    }

    @GetMapping("/admin/dashboard")
    public ResponseEntity<ApiResponse> adminDashboard(Authentication authentication) {
        return ResponseEntity.ok(new ApiResponse(
                "Welcome to the admin dashboard, " + authentication.getName() + "!"
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest registerRequest) {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();

        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Username and password must not be empty."));
        }

        if (userDetailsManager.userExists(username)) {
            return ResponseEntity.badRequest().body(new ApiResponse("Username already exists."));
        }

        UserDetails user = User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();

        userDetailsManager.createUser(user);

        return ResponseEntity.ok(new ApiResponse("User registered successfully."));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body(new ApiResponse("Unauthorized"));
        }

        return ResponseEntity.ok(new ApiResponse(
                "Authentiacted user profile: " + authentication.getName()
        ));
    }

}
