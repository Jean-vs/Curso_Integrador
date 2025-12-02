package com.example.sistema_tareas.controller;

import com.example.sistema_tareas.model.Empleado;
import com.example.sistema_tareas.model.Tarea;
import com.example.sistema_tareas.model.Usuario;
import com.example.sistema_tareas.service.TareaService;
import com.example.sistema_tareas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class LiderController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TareaService tareaService;

    // Vista para el líder
    @GetMapping("/lider/tareas")
    public String verTareasLider(Authentication auth, Model model) {

        String username = auth.getName();

        Usuario usuario = usuarioService.buscarPorCorreo(username)
                .orElseThrow(() -> new RuntimeException("No se encontró usuario con correo: " + username));

        Empleado empleado = usuario.getEmpleado();
        if (empleado == null) {
            throw new RuntimeException("Este usuario no tiene un empleado asociado");
        }

        List<Tarea> tareas = tareaService.listarTareasPorLider(empleado);

        Tarea tarea = tareas.isEmpty() ? null : tareas.get(0);

        model.addAttribute("tarea", tarea);
        model.addAttribute("tareas", tareas);
        model.addAttribute("lider", empleado);
        model.addAttribute("idUsuario", usuario.getId());

        return "tareas_lider";
    }

}
