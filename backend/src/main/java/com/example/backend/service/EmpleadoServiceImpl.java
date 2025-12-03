package com.example.sistema_tareas.service;

import com.example.sistema_tareas.model.Empleado;
import com.example.sistema_tareas.model.Equipo;
import com.example.sistema_tareas.model.Usuario;
import com.example.sistema_tareas.repository.EquipoRepository;
import com.example.sistema_tareas.repository.EmpleadoRepository;
import com.example.sistema_tareas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Empleado registrarEmpleado(Empleado empleado) {

        if (empleadoRepository.existsByDni(empleado.getDni())) {
            throw new RuntimeException("El DNI ya fue ingresado.");
        }

        return empleadoRepository.save(empleado);
    }


    @Override
    public Empleado guardarEmpleado(Empleado empleado) {


        Empleado encontrado = empleadoRepository.findByDni(empleado.getDni());

        if (encontrado != null && !encontrado.getId().equals(empleado.getId())) {
            throw new RuntimeException("El DNI ya fue ingresado.");
        }

        Empleado empleadoGuardado = empleadoRepository.save(empleado);


        if (empleado.getUsuario() != null) {

            Usuario user = empleado.getUsuario();

            if ("LIDER".equals(empleado.getRolEmpleado())) {
                user.setRol("LIDER");
            } else {
                user.setRol("EMPLEADO");
                user.setCorreo(null); 
            }

            usuarioRepository.save(user);
        }

        return empleadoGuardado;
    }

    // ------------------------------------------------------------
    @Override
    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> buscarPorId(Long id) {
        return empleadoRepository.findById(id);
    }


    @Override
    public void eliminarEmpleado(Long id) {

        usuarioRepository.findByEmpleado_Id(id).ifPresent(usuario -> {
            usuario.setEmpleado(null);
            usuarioRepository.save(usuario);
            usuarioRepository.delete(usuario);
        });

        List<Equipo> equiposLider = equipoRepository.findByLider_Id(id);
        for (Equipo equipo : equiposLider) {
            equipo.setLider(null);
            equipoRepository.save(equipo);
        }

        List<Equipo> equiposMiembro = equipoRepository.findByEmpleados_Id(id);
        for (Equipo equipo : equiposMiembro) {
            equipo.getEmpleados().removeIf(e -> e.getId().equals(id));
            equipoRepository.save(equipo);
        }

        empleadoRepository.deleteById(id);
    }

    @Override
    public List<Empleado> obtenerLideres() {
        return empleadoRepository.findByRolEmpleado("LIDER");
    }

    @Override
    public List<Empleado> obtenerLideresDisponibles() {
        return empleadoRepository.findByRolEmpleado("LIDER")
                .stream()
                .filter(l -> !equipoRepository.existsByLider_Id(l.getId()))
                .toList();
    }

    @Override
    public List<Empleado> obtenerSoloEmpleados() {
        return empleadoRepository.findByRolEmpleado("EMPLEADO");
    }

    @Override
    public List<Empleado> obtenerEmpleadosDisponiblesParaCrear() {
        List<Empleado> empleados = empleadoRepository.findByRolEmpleado("EMPLEADO");

        List<Long> ocupados = equipoRepository.findAll()
                .stream()
                .flatMap(eq -> eq.getEmpleados().stream())
                .map(Empleado::getId)
                .toList();

        return empleados.stream()
                .filter(e -> !ocupados.contains(e.getId()))
                .toList();
    }

    @Override
    public List<Empleado> obtenerEmpleadosDisponiblesParaEditar(Long equipoId) {
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        List<Empleado> empleados = empleadoRepository.findByRolEmpleado("EMPLEADO");

        List<Long> ocupados = equipoRepository.findAll()
                .stream()
                .filter(eq -> !eq.getId().equals(equipoId))
                .flatMap(eq -> eq.getEmpleados().stream())
                .map(Empleado::getId)
                .toList();

        return empleados.stream()
                .filter(emp -> !ocupados.contains(emp.getId()) || equipo.getEmpleados().contains(emp))
                .toList();
    }

    
    @Override
    public void validarDniParaActualizar(String dni, Long idEmpleado) {

        Empleado existente = empleadoRepository.findByDni(dni);

        if (existente != null && !existente.getId().equals(idEmpleado)) {
            throw new RuntimeException("El DNI ya fue ingresado.");
        }
    }
}