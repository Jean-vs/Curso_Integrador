package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.Servicio;
import com.example.sistema_tareas.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServicioServiceImpl implements ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    @Override
    public Servicio guardar(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Override
    public void eliminar(Long id) {
        servicioRepository.deleteById(id);
    }

    @Override
    public Optional<Servicio> obtenerPorId(Long id) {
        return servicioRepository.findById(id);
    }
}