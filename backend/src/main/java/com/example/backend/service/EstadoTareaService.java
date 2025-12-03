package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.EstadoTarea;
import com.example.sistema_tareas.repository.EstadoTareaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstadoTareaService {

    private final EstadoTareaRepository estadoTareaRepository;

    public EstadoTareaService(EstadoTareaRepository estadoTareaRepository) {
        this.estadoTareaRepository = estadoTareaRepository;
    }

    public Optional<EstadoTarea> obtenerPorNombre(String nombre) {
        return estadoTareaRepository.findByNombre(nombre);
    }

    public EstadoTarea crearEstadoSiNoExiste(String nombre) {
        return estadoTareaRepository.findByNombre(nombre)
                .orElseGet(() -> estadoTareaRepository.save(new EstadoTarea(nombre)));
    }
}