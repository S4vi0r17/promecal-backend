package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "nombreusuario", nullable = false, unique = true)
    private String nombreusuario;
    @Column(name = "nombrecompleto")
    private String nombrecompleto;
    @Column(name = "correoelectronico")
    private String correoelectronico;
    @Column(name = "contrasena")
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Rol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Agregamos ROLE_ al nombre del rol para cumplir con la convención de Spring Security
        return List.of(new SimpleGrantedAuthority(rol.name()));
    }
    // Métodos obligatorios de UserDetails en inglés
    @Override
    public String getUsername() {
        return nombreusuario;
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}