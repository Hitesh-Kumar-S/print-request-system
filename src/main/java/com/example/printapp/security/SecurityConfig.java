package com.example.printapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
private CustomAuthenticationSuccessHandler
        customAuthenticationSuccessHandler;

    // =========================
    // Password Encoder
    // =========================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =========================
    // Authentication Manager
    // =========================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

    // =========================
    // Security Filter Chain
    // =========================

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

            // Disable CSRF
            .csrf(csrf -> csrf.disable())

            // =========================
            // Authorization Rules
            // =========================

            .authorizeHttpRequests(auth -> auth

                // Public pages
                .requestMatchers(
                        "/",
                        "/login",
                        "/signup",
                        "/auth/signup",
                        "/style.css",
                        "/theme.js",
                        "/error"
                ).permitAll()

                // Admin pages
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")

                // User pages
                .requestMatchers("/user/**")
                .hasAnyRole("USER", "ADMIN")

                // Request pages
                .requestMatchers(
                        "/request",
                        "/submitRequest",
                        "/confirmRequest",
                        "/viewFile/**",
                        "/downloadFile/**"
                ).hasAnyRole("USER", "ADMIN")

                // Everything else
                .anyRequest().authenticated()
            )

            // =========================
            // Login Configuration
            // =========================

            .formLogin(form -> form

                // Custom login page
                .loginPage("/login")

                // Login processing URL
                .loginProcessingUrl("/perform_login")

                .successHandler(customAuthenticationSuccessHandler)

                // Failed login
                .failureUrl("/login?error=true")

                .permitAll()
            )

            // =========================
            // Logout Configuration
            // =========================

            .logout(logout -> logout

                .logoutUrl("/logout")

                .logoutSuccessUrl("/login?logout=true")

                .permitAll()
            );

        return http.build();
    }
}