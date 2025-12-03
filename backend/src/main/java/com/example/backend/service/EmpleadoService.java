package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.Empleado;
import com.example.sistema_tareas.model.Equipo;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    Empleado guardarEmpleado(Empleado empleado);

    List<Empleado> listarEmpleados();

    Optional<Empleado> buscarPorId(Long id);

    void eliminarEmpleado(Long id);


    Empleado registrarEmpleado(Empleado empleado);

    public List<Empleado> obtenerLideres();

    public List<Empleado> obtenerLideresDisponibles();

    List<Empleado> obtenerSoloEmpleados();

    List<Empleado> obtenerEmpleadosDisponiblesParaCrear();

    List<Empleado> obtenerEmpleadosDisponiblesParaEditar(Long equipoId);

    void validarDniParaActualizar(String dni, Long idEmpleado);


}