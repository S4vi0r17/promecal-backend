package com.sanmarcos.promecal.service;

import com.sanmarcos.promecal.exception.ContraseniaIncorrectaException;
import com.sanmarcos.promecal.exception.UsuarioNoEncontradoException;
import com.sanmarcos.promecal.model.dto.AuthResponse;
import com.sanmarcos.promecal.model.dto.LoginRequest;
import com.sanmarcos.promecal.model.dto.RegisterRequest;
import com.sanmarcos.promecal.model.entity.Usuario;
import com.sanmarcos.promecal.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /*public AuthResponse login(LoginRequest request) {
        // Autenticación usando el nombre de usuario y la contraseña en texto plano
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getNombreusuario(), request.getContrasena())
        );

        // Buscar al usuario por su nombre de usuario
        UserDetails user = usuarioRepository.findByNombreusuario(request.getNombreusuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar el token JWT
        String token = jwtService.getToken(user,);

        return AuthResponse.builder()
                .token(token)
                .build();
    }*/
    public AuthResponse login(LoginRequest request) {
        // Buscar al usuario por su nombre de usuario
        Usuario usuario = usuarioRepository.findByNombreusuario(request.getNombreusuario())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario no encontrado"));

        // Verificar si la contraseña coincide
        if (!passwordEncoder.matches(request.getContrasena(), usuario.getPassword())) {
            throw new ContraseniaIncorrectaException("La contraseña es incorrecta");
        }

        // Autenticar al usuario usando el nombre de usuario y la contraseña
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getNombreusuario(), request.getContrasena())
        );

        // Generar el token JWT con el rol del usuario
        String token = jwtService.getToken(usuario, usuario.getRol(), usuario.getNombrecompleto());

        return AuthResponse.builder()
                .token(token)
                .rol(usuario.getRol())
                .build();
    }
    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombreusuario(request.getNombreusuario());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setNombrecompleto(request.getNombrecompleto());
        usuario.setCorreoelectronico(request.getCorreoelectronico());
        usuario.setRol(request.getRol());
        usuarioRepository.save(usuario);

        // Generar el token JWT después del registro
        String token = jwtService.getToken(usuario, usuario.getRol(), usuario.getNombrecompleto());

        return AuthResponse.builder().token(token).rol(usuario.getRol()).build();
    }
    /*
    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNombreusuario(request.getNombreusuario());
        usuario.setContrasena(passwordEncoder.encode(request.getContrasena()));
        usuario.setNombrecompleto(request.getNombrecompleto());
        usuario.setCorreoelectronico(request.getCorreoelectronico());
        usuario.setRol(request.getRol());
        usuarioRepository.save(usuario);

        return AuthResponse.builder().token(jwtService.getToken(usuario)).build();
    }*/
}
