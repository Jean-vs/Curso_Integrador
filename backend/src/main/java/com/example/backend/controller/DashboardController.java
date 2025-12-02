package com.example.sistema_tareas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model; // ✔ IMPORTACIÓN CORRECTA

import com.example.sistema_tareas.service.TareaService;

@Controller
public class DashboardController {

    @Autowired
    private TareaService tareaService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        int total = tareaService.countTotal();
        int completadas = tareaService.countByEstado("Completada");
        int progreso = tareaService.countByEstado("En progreso");
        int pendientes = tareaService.countByEstado("Pendiente");

        model.addAttribute("total", total);
        model.addAttribute("completadas", completadas);
        model.addAttribute("progreso", progreso);
        model.addAttribute("pendientes", pendientes);

        return "dashboard";
    }
}
