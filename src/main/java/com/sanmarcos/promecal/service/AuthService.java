package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.model.dto.AuthResponse;
import com.sanmarcos.promecal.model.dto.LoginRequest;
import com.sanmarcos.promecal.model.dto.RegisterRequest;
import com.sanmarcos.promecal.model.entity.Role;
import com.sanmarcos.promecal.model.entity.User;
import com.sanmarcos.promecal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        return null;
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
