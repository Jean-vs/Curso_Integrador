package com.example.sistema_tareas.controller;

import com.example.sistema_tareas.model.Equipo;
import com.example.sistema_tareas.service.EquipoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public String listarEquipos(Model model) {
        model.addAttribute("equipos", equipoService.listarEquipos());
        model.addAttribute("equipo", new Equipo());
        return "equipos"; // nombre del template Thymeleaf
    }

    @PostMapping("/guardar")
    public String guardarEquipo(@ModelAttribute("equipo") Equipo equipo) {
        equipoService.guardarEquipo(equipo);
        return "redirect:/equipos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEquipo(@PathVariable Long id) {
        equipoService.eliminarEquipo(id);
        return "redirect:/equipos";
    }
}
