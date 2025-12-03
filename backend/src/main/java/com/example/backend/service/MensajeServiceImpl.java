package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.Mensaje;
import com.example.sistema_tareas.model.Tarea;
import com.example.sistema_tareas.model.Usuario;
import com.example.sistema_tareas.repository.MensajeRepository;
import com.example.sistema_tareas.repository.TareaRepository;
import com.example.sistema_tareas.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MensajeServiceImpl implements MensajeService {

    private final MensajeRepository repo;
    private final TareaRepository tareaRepo;
    private final UsuarioRepository usuarioRepo;

    public MensajeServiceImpl(MensajeRepository repo,
                              TareaRepository tareaRepo,
                              UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.tareaRepo = tareaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public List<Mensaje> listarPorTarea(Long tareaId) {
        return repo.findByTarea_IdOrderByFechaHoraAsc(tareaId);
    }

    @Override
    public Mensaje guardar(Mensaje mensaje) {

        Tarea tarea = tareaRepo.findById(mensaje.getTareaId())
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));
        mensaje.setTarea(tarea);

        Usuario usuario = usuarioRepo.findById(mensaje.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        mensaje.setUsuario(usuario);

        mensaje.setFechaHora(LocalDateTime.now());

        return repo.save(mensaje);
    }
}