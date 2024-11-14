package com.sanmarcos.promecal.repository;

import com.sanmarcos.promecal.model.entity.Cliente;
import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long>, JpaSpecificationExecutor<OrdenTrabajo> {
    // Método para verificar si existe una OrdenTrabajo con el código dado
    boolean existsByCodigo(String codigo);

    // Método para buscar por 'codigo' y devolver Optional
    Optional<OrdenTrabajo> findByCodigo(String codigo);

    // Método para encontrar todas las órdenes de trabajo por cliente
    List<OrdenTrabajo> findByCliente(Cliente cliente);
}
