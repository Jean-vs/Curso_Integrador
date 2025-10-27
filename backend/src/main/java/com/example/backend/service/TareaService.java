package com.example.sistema_tareas.service;

import java.util.List;
import com.example.sistema_tareas.model.Tarea;

public interface TareaService {
    List<Tarea> listarTareas();
    List<Tarea> listarTareasPorUsuario(String nombreUsuario); 
    Tarea guardarTarea(Tarea tarea);
    void eliminarTarea(Long id);
    Tarea obtenerPorId(Long id);
}