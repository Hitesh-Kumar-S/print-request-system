package com.example.printapp.security;

import com.example.printapp.model.User;
import com.example.printapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        // Return Spring Security User
        return new org.springframework.security.core.userdetails.User(

                user.getEmail(),
                user.getPassword(),

                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        )
                )
        );
    }
}