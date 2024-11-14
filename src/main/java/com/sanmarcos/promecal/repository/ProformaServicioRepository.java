package com.sanmarcos.promecal.repository;

import com.sanmarcos.promecal.model.entity.OrdenTrabajo;
import com.sanmarcos.promecal.model.entity.ProformaServicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProformaServicioRepository extends JpaRepository<ProformaServicio, Long> {
    ProformaServicio findByOrdenTrabajo(OrdenTrabajo ordenTrabajo);
    List<ProformaServicio> findAllByOrdenTrabajo(OrdenTrabajo ordenTrabajo);
}
