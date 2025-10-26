package com.example.sistema_tareas.controller;

import com.example.sistema_tareas.model.Tarea;
import com.example.sistema_tareas.model.Usuario;
import com.example.sistema_tareas.model.Equipo;
import com.example.sistema_tareas.model.Servicio;
import com.example.sistema_tareas.service.TareaService;
import com.example.sistema_tareas.service.UsuarioService;
import com.example.sistema_tareas.service.EquipoService;
import com.example.sistema_tareas.service.ServicioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private ServicioService servicioService; // ✅ Nuevo servicio para manejar los servicios

    // 🔹 Mostrar solo las tareas del usuario autenticado
    @GetMapping
    public String listarTareas(Model model, Principal principal) {
        if (principal != null) {
            String nombreUsuario = principal.getName();
            model.addAttribute("nombreUsuario", nombreUsuario);
            model.addAttribute("tareas", tareaService.listarTareasPorUsuario(nombreUsuario));
        } else {
            model.addAttribute("tareas", tareaService.listarTareas());
        }

        // ✅ Agregar los equipos y servicios disponibles al modelo
        List<Equipo> equipos = equipoService.listarEquipos();
        List<Servicio> servicios = servicioService.listarServicios();
        model.addAttribute("equipos", equipos);
        model.addAttribute("servicios", servicios);

        model.addAttribute("tarea", new Tarea());
        return "tareas";
    }

    // 🔹 Guardar nueva tarea
    @PostMapping("/guardar")
    public String guardarTarea(@ModelAttribute("tarea") Tarea tarea,
                               @RequestParam("equipoId") Long equipoId,
                               @RequestParam("servicioId") Long servicioId,
                               Principal principal) {
        if (principal != null) {
            Optional<Usuario> usuarioOpt = usuarioService.buscarPorUsuario(principal.getName());
            usuarioOpt.ifPresent(tarea::setUsuario);
        }

        // ✅ Asociar el equipo seleccionado y cambiar su estado a "Ocupado"
        Optional<Equipo> equipoOpt = equipoService.obtenerPorId(equipoId);
        if (equipoOpt.isPresent()) {
            Equipo equipo = equipoOpt.get();
            equipo.setEstado("Ocupado");
            equipoService.guardar(equipo);
            tarea.setEquipo(equipo);
        }

        // ✅ Asociar el servicio seleccionado
        Optional<Servicio> servicioOpt = servicioService.obtenerPorId(servicioId);
        servicioOpt.ifPresent(tarea::setServicio);

        tareaService.guardarTarea(tarea);
        return "redirect:/tareas";
    }

    // 🔹 Editar tarea
    @GetMapping("/editar/{id}")
    public String editarTarea(@PathVariable Long id, Model model, Principal principal) {
        Tarea tarea = tareaService.obtenerPorId(id);
        if (principal != null && tarea != null &&
                tarea.getUsuario() != null &&
                tarea.getUsuario().getNombreUsuario().equals(principal.getName())) {

            model.addAttribute("nombreUsuario", principal.getName());
            model.addAttribute("tarea", tarea);
            model.addAttribute("tareas", tareaService.listarTareasPorUsuario(principal.getName()));
            model.addAttribute("equipos", equipoService.listarEquipos());
            model.addAttribute("servicios", servicioService.listarServicios()); // ✅ Para el combo
            return "tareas";
        }
        return "redirect:/tareas";
    }

    // 🔹 Eliminar tarea
    @GetMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id, Principal principal) {
        Tarea tarea = tareaService.obtenerPorId(id);
        if (tarea != null && principal != null &&
                tarea.getUsuario() != null &&
                tarea.getUsuario().getNombreUsuario().equals(principal.getName())) {
            tareaService.eliminarTarea(id);
        }
        return "redirect:/tareas";
    }
}
