package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.Servicio;
import java.util.List;
import java.util.Optional;

public interface ServicioService {
    List<Servicio> listarServicios();
    Servicio guardar(Servicio servicio);
    void eliminar(Long id);
    Optional<Servicio> obtenerPorId(Long id);
}