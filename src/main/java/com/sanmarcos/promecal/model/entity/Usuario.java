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
@Table(name = "usuarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"nombreusuario"})})
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombreusuario;

    private String nombrecompleto;
    private String correoelectronico;
    private String contrasena;

    @Enumerated(EnumType.STRING)
    Rol rol;

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