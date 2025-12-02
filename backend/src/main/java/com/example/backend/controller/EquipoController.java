package com.example.sistema_tareas.controller;

import com.example.sistema_tareas.model.Empleado;
import com.example.sistema_tareas.model.Equipo;
import com.example.sistema_tareas.service.EquipoService;
import com.example.sistema_tareas.service.EmpleadoService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipos")
public class EquipoController {

    private final EquipoService equipoService;
    private final EmpleadoService empleadoService;

    public EquipoController(EquipoService equipoService, EmpleadoService empleadoService) {
        this.equipoService = equipoService;
        this.empleadoService = empleadoService;
    }

    @GetMapping
    public String listarEquipos(Model model) {
        model.addAttribute("equipos", equipoService.listarEquipos());
        model.addAttribute("equipo", new Equipo());

        // Líderes disponibles
        model.addAttribute("lideresDisponibles", empleadoService.obtenerLideresDisponibles());

        // Empleados disponibles SOLO para crear (libres)
        model.addAttribute("empleados", empleadoService.obtenerEmpleadosDisponiblesParaCrear());

        return "equipos";
    }

    @PostMapping("/guardar")
    public String guardarEquipo(
            @ModelAttribute("equipo") Equipo equipo,
            @RequestParam(value = "empleadosIds", required = false) List<Long> empleadosIds) {

        // Estado automático al crear
        if (equipo.getId() == null) {
            equipo.setEstado("Libre");
        }

        // Asignar empleados seleccionados
        if (empleadosIds != null && !empleadosIds.isEmpty()) {
            List<Empleado> empleados = empleadoService.listarEmpleados()
                    .stream()
                    .filter(e -> empleadosIds.contains(e.getId()))
                    .toList();

            equipo.setEmpleados(empleados);
        } else {
            equipo.setEmpleados(new ArrayList<>());
        }

        equipoService.guardarEquipo(equipo);

        return "redirect:/equipos";
    }

    @GetMapping("/editar/{id}")
    public String editarEquipo(@PathVariable Long id, Model model) {
        Equipo equipo = equipoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido"));

        model.addAttribute("equipo", equipo);

        // Líderes disponibles (+ el líder actual)
        var lideresDisponibles = empleadoService.obtenerLideresDisponibles();
        if (!lideresDisponibles.contains(equipo.getLider())) {
            lideresDisponibles.add(equipo.getLider());
        }
        model.addAttribute("lideresDisponibles", lideresDisponibles);

        // Empleados disponibles para EDITAR (libres + los actuales del equipo)
        model.addAttribute("empleados", empleadoService.obtenerEmpleadosDisponiblesParaEditar(id));

        return "editar_equipo";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEquipo(@PathVariable Long id) {
        equipoService.eliminarEquipo(id);
        return "redirect:/equipos";
    }
}
