package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.model.dto.AuthResponse;
import com.sanmarcos.promecal.model.dto.LoginRequest;
import com.sanmarcos.promecal.model.dto.RegisterRequest;
import com.sanmarcos.promecal.model.entity.Usuario;
import com.sanmarcos.promecal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getNombreusuario(), request.getContrasena()));
        UserDetails user = usuarioRepository.findByNombreusuario(request.getNombreusuario()).orElseThrow();
        String token = jwtService.getToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }
    public AuthResponse register(RegisterRequest request) {
        Usuario usuario= new Usuario();
        usuario.setNombreusuario(request.getNombreusuario());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setNombrecompleto(request.getNombrecompleto());
        usuario.setCorreoelectronico(request.getCorreoelectronico());
        usuario.setRol(request.getRol());
        usuarioRepository.save(usuario);

        return AuthResponse.builder().token(jwtService.getToken(usuario)).build();
    }
}
