package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.model.dto.AuthResponse;
import com.sanmarcos.promecal.model.dto.LoginRequest;
import com.sanmarcos.promecal.model.dto.RegisterRequest;
import com.sanmarcos.promecal.model.entity.Role;
import com.sanmarcos.promecal.model.entity.User;
import com.sanmarcos.promecal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder().username(request.getUsername())
                .password(request.getPassword())
                .firstName(request.getFirstName())
                .lastname(request.getLastName())
                .country(request.getCountry())
                .rol(Role.USER)
                .build();
        userRepository.save(user);
        return AuthResponse.builder().token(jwtService.getToken(user)).build();
    }
}
