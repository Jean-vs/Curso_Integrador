package com.example.sistema_tareas.repository;

import com.example.sistema_tareas.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    List<Mensaje> findByTarea_IdOrderByFechaHoraAsc(Long tareaId);

}
