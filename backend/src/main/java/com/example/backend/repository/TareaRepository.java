package com.example.backend.repository;

import com.example.backend.entity.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // ✅ Cambiado para funcionar con la relación Usuario → Tarea
    List<Tarea> findByUsuario_NombreUsuario(String nombreUsuario);
}

