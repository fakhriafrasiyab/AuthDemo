package com.fakhri.authdemo.controller;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = (InMemoryUserDetailsManager) userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/public")
    public String publicPage() {
        return "Public endpoint – no authentication required.";
    }

    @GetMapping("/user/home")
    public String userHome() {
        return "Hello USER – You are successfully authenticated!";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Welcome ADMIN – You have full access.";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        if (userDetailsManager.userExists(username)) {
            return "❌ User already exists!";
        }
        UserDetails newUser = User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build();
        userDetailsManager.createUser(newUser);
        return "✅ User registered successfully!";
    }
}
