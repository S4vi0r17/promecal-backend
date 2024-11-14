package com.sanmarcos.promecal.repository;

import com.sanmarcos.promecal.model.entity.OrdenTrabajoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenTrabajoHistorialRepository extends JpaRepository<OrdenTrabajoHistorial, Long> {
    List<OrdenTrabajoHistorial> findByOrdenTrabajoId(Long ordenTrabajoId);
}
