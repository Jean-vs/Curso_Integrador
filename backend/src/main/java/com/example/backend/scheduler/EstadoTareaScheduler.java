package com.example.sistema_tareas.scheduler;

import com.example.sistema_tareas.model.EstadoTarea;
import com.example.sistema_tareas.model.Tarea;
import com.example.sistema_tareas.repository.EstadoTareaRepository;
import com.example.sistema_tareas.repository.TareaRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class EstadoTareaScheduler {

    private final TareaRepository tareaRepository;
    private final EstadoTareaRepository estadoTareaRepository;

    public EstadoTareaScheduler(TareaRepository tareaRepository, EstadoTareaRepository estadoTareaRepository) {
        this.tareaRepository = tareaRepository;
        this.estadoTareaRepository = estadoTareaRepository;
    }

    @Scheduled(fixedRate = 5000)
    @Transactional 
    public void actualizarEstadosDeTareas() {
        List<Tarea> tareas = tareaRepository.findAll();
        LocalDateTime ahora = LocalDateTime.now();

        System.out.println("Comprobando tareas en " + ahora + " (" + tareas.size() + " tareas)");

        for (Tarea tarea : tareas) {
            if (tarea.getFechaInicio() == null || tarea.getFechaFin() == null)
                continue;

            String nuevoEstado = null;
            if (ahora.isBefore(tarea.getFechaInicio())) {
                nuevoEstado = "Pendiente";
            } else if (ahora.isAfter(tarea.getFechaInicio()) && ahora.isBefore(tarea.getFechaFin())) {
                nuevoEstado = "En progreso";
            } else if (ahora.isAfter(tarea.getFechaFin())) {
                nuevoEstado = "Completada";
            }

            if (nuevoEstado != null) {
                Optional<EstadoTarea> estadoOpt = estadoTareaRepository.findByNombre(nuevoEstado);
                if (estadoOpt.isPresent() && (tarea.getEstado() == null ||
                        !tarea.getEstado().getNombre().equals(nuevoEstado))) {
                    tarea.setEstado(estadoOpt.get());
                    tareaRepository.save(tarea);
                    System.out.println("Tarea '" + tarea.getNombre() + "' actualizada a: " + nuevoEstado);
                }
            }
        }
    }
}