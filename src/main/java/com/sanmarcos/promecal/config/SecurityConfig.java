package com.sanmarcos.promecal.config;

import com.sanmarcos.promecal.service.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(authRequest ->
                        authRequest.requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/api/usuarios/**").hasRole("ADMINISTRADOR")
                                .requestMatchers("/api/clientes/**").hasAuthority("ROLE_EJECUTIVO_DE_VENTAS")
                                .requestMatchers("/api/ordentrabajo/**").hasAuthority("ROLE_ASISTENTE_DE_RECEPCION")
                                .requestMatchers("/api/informediagnostico/**").hasAuthority("ROLE_ASISTENTE_TECNICO")
                                .requestMatchers("/api/proformaservicio/**").hasAuthority("ROLE_EJECUTIVO_DE_VENTAS")
                                //.requestMatchers(HttpMethod.GET, "/api/ordentrabajo", "/api/ordentrabajo/{id}/**").hasAuthority("ROLE_ASISTENTE_TECNICO")
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
