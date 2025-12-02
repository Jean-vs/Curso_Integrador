package com.example.sistema_tareas.config;

import com.example.sistema_tareas.model.EstadoTarea;
import com.example.sistema_tareas.repository.EstadoTareaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initEstados(EstadoTareaRepository estadoTareaRepository) {
        return args -> {
            if (estadoTareaRepository.count() == 0) {
                estadoTareaRepository.save(new EstadoTarea("Pendiente"));
                estadoTareaRepository.save(new EstadoTarea("En progreso"));
                estadoTareaRepository.save(new EstadoTarea("Completada"));
                System.out.println("✅ Estados de tarea inicializados correctamente");
            }
        };
    }
}

