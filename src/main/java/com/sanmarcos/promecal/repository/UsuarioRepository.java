package com.sanmarcos.promecal.repository;

import com.sanmarcos.promecal.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
