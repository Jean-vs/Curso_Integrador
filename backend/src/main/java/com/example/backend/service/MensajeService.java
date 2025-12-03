package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.Mensaje;
import java.util.List;

public interface MensajeService {

    List<Mensaje> listarPorTarea(Long tareaId);

    Mensaje guardar(Mensaje mensaje);
}