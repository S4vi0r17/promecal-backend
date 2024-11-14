package com.sanmarcos.promecal.repository;

import com.sanmarcos.promecal.model.entity.InformeDiagnostico;
import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformeDiagnosticoRepository extends JpaRepository<InformeDiagnostico, Long> {
    List<InformeDiagnostico> findByCodigoOrdenTrabajo(String codigoOrdenTrabajo);
}
