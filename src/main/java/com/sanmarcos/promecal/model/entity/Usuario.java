package com.sanmarcos.promecal.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data // Lombok: Genera automáticamente getters, setters, toString, etc.
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombreusuario", nullable = false, unique = true, length = 20)
    private String nombreusuario;

    @Column(name = "nombrecompleto", length = 50, nullable = false)
    private String nombrecompleto;

    @Column(name = "correoelectronico", length = 30, nullable = false)
    private String correoelectronico;

    @Column(name = "contrasena", nullable = false, length = 100)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 30, nullable = false)
    private Rol rol;

    // Métodos de UserDetails para Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ROLE_ se agrega al rol para seguir la convención de Spring Security
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

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
        return true; // Cambiar si se necesita lógica específica
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Cambiar si se necesita lógica específica
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Cambiar si se necesita lógica específica
    }

    @Override
    public boolean isEnabled() {
        return true; // Cambiar si se necesita lógica específica
    }
}
