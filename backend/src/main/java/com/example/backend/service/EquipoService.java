package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.Equipo;
import com.example.sistema_tareas.repository.EquipoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public List<Equipo> listarEquipos() {
        return equipoRepository.findAll();
    }

    public void guardarEquipo(Equipo equipo) {
        equipoRepository.save(equipo);
    }


    public void eliminarEquipo(Long id) {
        equipoRepository.deleteById(id);
    }
    public Optional<Equipo> obtenerPorId(Long id) {
        return equipoRepository.findById(id);
    }

    public Equipo guardar(Equipo equipo) {
        return equipoRepository.save(equipo);
    }

}