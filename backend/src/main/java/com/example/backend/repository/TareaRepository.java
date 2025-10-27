package com.example.backend.repository;

import com.example.backend.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    List<Tarea> findByUsuario_NombreUsuario(String nombreUsuario);
}

