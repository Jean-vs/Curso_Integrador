package com.example.sistema_tareas.controller;

import com.example.sistema_tareas.model.Usuario;
import com.example.sistema_tareas.service.UsuarioService;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String nombreUsuario,
            @RequestParam String password,
            Model model) {

        return usuarioService.buscarPorUsuario(nombreUsuario)
                .map(usuario -> {
                    if (usuarioService.passwordCoincide(password, usuario.getPassword())) {
                        return "redirect:/dashboard";
                    } else {
                        model.addAttribute("error", "Contraseña incorrecta");
                        return "login";
                    }
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Usuario no encontrado");
                    return "login";
                });
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, Principal principal) {
        if (principal != null) {
            String nombreUsuario = principal.getName(); // obtiene el usuario autenticado
            model.addAttribute("nombreUsuario", nombreUsuario);
        }
        return "dashboard"; // nombre del HTML que muestra tu dashboard
    }

    /*@Controller
    public class TareasController {

        @GetMapping("/tareas")
        public String mostrarTareas(Model model, Principal principal) {
            if (principal != null) {
                String nombreUsuario = principal.getName();
                model.addAttribute("nombreUsuario", nombreUsuario);
            }
            return "tareas";
        }
    }*/

}
