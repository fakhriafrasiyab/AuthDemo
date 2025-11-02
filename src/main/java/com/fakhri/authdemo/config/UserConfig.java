package com.fakhri.authdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserConfig {

    @Bean
    public InMemoryUserDetailsManager users(PasswordEncoder passwordEncoder) {

        UserDetails admin = User.withUsername("admin")
                .password("admin")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
