package com.example.backend.controller;

import com.example.backend.entity.Tarea;
import com.example.backend.service.TareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class TareaController {

    private final TareaService tareaService;

    @GetMapping
    public List<Tarea> getAll() {
        return tareaService.getAllTareas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarea> getById(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.getTareaById(id));
    }

    @PostMapping
    public ResponseEntity<Tarea> create(@RequestBody Tarea tarea) {
        return ResponseEntity.ok(tareaService.createTarea(tarea));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarea> update(@PathVariable Long id, @RequestBody Tarea tarea) {
        return ResponseEntity.ok(tareaService.updateTarea(id, tarea));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tareaService.deleteTarea(id);
        return ResponseEntity.noContent().build();
    }
}