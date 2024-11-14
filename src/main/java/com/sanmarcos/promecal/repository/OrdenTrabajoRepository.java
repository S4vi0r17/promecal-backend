package com.sanmarcos.promecal.repository;

import com.sanmarcos.promecal.model.entity.Cliente;
import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, Long> , JpaSpecificationExecutor<OrdenTrabajo> {
    // Metodo para verificar si existe una OrdenTrabajo con el código dado
    boolean existsByCodigo(String codigo);
    // Metodo para buscar por 'codigo'
    OrdenTrabajo findByCodigo(String codigo);

    // Metodo para encontrar todas las ordenes de trabajo por cliente
    List<OrdenTrabajo> findByCliente(Cliente cliente);
}
