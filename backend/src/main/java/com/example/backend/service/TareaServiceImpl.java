package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.Tarea;
import com.example.sistema_tareas.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TareaServiceImpl implements TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    @Override
    public List<Tarea> listarTareas() {
        return tareaRepository.findAll();
    }

    @Override
    public List<Tarea> listarTareasPorUsuario(String nombreUsuario) {
        return tareaRepository.findByUsuario_NombreUsuario(nombreUsuario);
    }

    @Override
    public Tarea guardarTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    @Override
    public void eliminarTarea(Long id) {
        tareaRepository.deleteById(id);
    }

    @Override
    public Tarea obtenerPorId(Long id) {
        return tareaRepository.findById(id).orElse(null);
    }
}