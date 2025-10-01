package com.example.backend.controller;

import com.example.backend.entity.Comentario;
import com.example.backend.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class ComentarioController {

    private final ComentarioService comentarioService;

    @GetMapping("/tarea/{tareaId}")
    public List<Comentario> getByTarea(@PathVariable Long tareaId) {
        return comentarioService.getComentariosByTarea(tareaId);
    }

    @PostMapping("/tarea/{tareaId}")
    public ResponseEntity<Comentario> addComentario(@PathVariable Long tareaId, @RequestBody Comentario comentario) {
        return ResponseEntity.ok(comentarioService.addComentario(tareaId, comentario));
    }
}