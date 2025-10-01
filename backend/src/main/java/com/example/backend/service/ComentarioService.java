package com.example.backend.service;

import com.example.backend.entity.Comentario;
import com.example.backend.entity.Tarea;
import com.example.backend.repository.ComentarioRepository;
import com.example.backend.repository.TareaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TareaRepository tareaRepository;

    public List<Comentario> getComentariosByTarea(Long tareaId) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        return tarea.getComentarios();
    }

    public Comentario addComentario(Long tareaId, Comentario comentario) {
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        comentario.setTarea(tarea);
        return comentarioRepository.save(comentario);
    }
}