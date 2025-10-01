package com.example.backend.service;

import com.example.backend.entity.EstadoTarea;
import com.example.backend.entity.Tarea;
import com.example.backend.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;

    public List<Tarea> getAllTareas() {
        return tareaRepository.findAll();
    }

    public Optional<Tarea> getTareaById(Long id) {
        return tareaRepository.findById(id);
    }

    public Tarea createTarea(Tarea tarea) {
        if (tarea.getEstado() == null) {
            tarea.setEstado(EstadoTarea.INCOMPLETO); 
               }
        return tareaRepository.save(tarea);
    }

    public Tarea updateTarea(Long id, Tarea tareaDetails) {
        return tareaRepository.findById(id)
                .map(t -> {
                    t.setTitulo(tareaDetails.getTitulo());
                    t.setDescripcion(tareaDetails.getDescripcion());
                    t.setFechaCreacion(tareaDetails.getFechaCreacion());
                    t.setFechaLimite(tareaDetails.getFechaLimite());
                    t.setEstado(tareaDetails.getEstado());
                    return tareaRepository.save(t);
                }).orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
    }

    public void deleteTarea(Long id) {
        tareaRepository.deleteById(id);
    }
}