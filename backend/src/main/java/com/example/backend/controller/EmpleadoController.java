package com.example.sistema_tareas.controller;

import com.example.sistema_tareas.model.Empleado;
import com.example.sistema_tareas.model.Usuario;
import com.example.sistema_tareas.service.EmpleadoService;
import com.example.sistema_tareas.service.UsuarioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarEmpleados(Model model) {
        model.addAttribute("empleados", empleadoService.listarEmpleados());
        model.addAttribute("empleado", new Empleado());
        return "empleados";
    }

    @PostMapping("/guardar")
    public String guardarEmpleado(@ModelAttribute Empleado empleado) {
        empleadoService.guardarEmpleado(empleado);

        if ("LIDER".equalsIgnoreCase(empleado.getRolEmpleado())) {
            usuarioService.crearUsuarioDesdeEmpleado(empleado);
        }

        return "redirect:/empleados";
    }

    @PostMapping("/actualizar")
    public String actualizarEmpleado(@ModelAttribute Empleado empleadoActualizado) {

        Empleado empleado = empleadoService.buscarPorId(empleadoActualizado.getId())
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + empleadoActualizado.getId()));

        // ⭐ Actualizamos los datos
        empleado.setNombres(empleadoActualizado.getNombres());
        empleado.setApellidos(empleadoActualizado.getApellidos());
        empleado.setDni(empleadoActualizado.getDni());
        empleado.setTelefono(empleadoActualizado.getTelefono());
        empleado.setDireccion(empleadoActualizado.getDireccion());

        String rolAnterior = empleado.getRolEmpleado(); // Guardar rol previo
        empleado.setRolEmpleado(empleadoActualizado.getRolEmpleado());

        // ⭐ Guardar cambios en BD ANTES de crear usuario
        empleadoService.guardarEmpleado(empleado);

        // ⭐ Si ahora es LÍDER y antes no lo era → crear usuario
        if ("LIDER".equalsIgnoreCase(empleado.getRolEmpleado())
                && !"LIDER".equalsIgnoreCase(rolAnterior)) {

            usuarioService.crearUsuarioDesdeEmpleado(empleado);
        }

        return "redirect:/empleados";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Long id) {
        empleadoService.eliminarEmpleado(id);
        return "redirect:/empleados";
    }

    @GetMapping("/empleados/lideres")
    public List<Empleado> obtenerLideres() {
        return empleadoService.obtenerLideres();
    }

}
